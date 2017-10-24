/**
 * 
 */
package org.frontuari.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MSequence;
import org.compiere.model.PO;
import org.compiere.model.X_M_RMAType;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * This Process void a Fiscal Invoice from Credit Note
 * @autor Jorge Colmenarez, 21 sept. 2017, jcolmenarez@frontuari.com, Frontuari, C.A.
 *
 */
public class VoidFiscalInvoiced extends SvrProcess{
	/*	Organization Trx 				*/
	private int p_AD_Org_ID			=	0;
	/*	Invoice 						*/
	private int p_C_Invoice_ID		=	0;
	/*	DocType for Credit Note			*/
	private int p_C_DocType_ID		=	0;
	/*	Is Service						*/
	private boolean p_IsService		=	true;
	/*	RMA Doc Type					*/
	private int p_C_RMADocType_ID	=	0;
	/*	RMA Type						*/
	private int p_M_RMAType_ID		=	0;
	/*	RM Doc Type						*/
	private int p_C_RMDocType_ID	=	0;
	/*	Credit Note Object				*/
	MInvoice m_CreditNote			= 	null;
	/*	Return Material Object			*/
	MInOut m_RM						=	null;
	
	protected void prepare() {
		ProcessInfoParameter[] params = getParameter();
		
		for (ProcessInfoParameter parameter : params) {
			String name = parameter.getParameterName();
			if (parameter.getParameter() == null)
				continue;
			if (name.equalsIgnoreCase("AD_Org_ID")){
				p_AD_Org_ID = parameter.getParameterAsInt();
			}
			else if (name.equalsIgnoreCase("C_Invoice_ID")){
				p_C_Invoice_ID = parameter.getParameterAsInt();
			}
			else if(name.equalsIgnoreCase("C_DocType_ID")){
				p_C_DocType_ID = parameter.getParameterAsInt();
			}
			else if(name.equalsIgnoreCase("IsService")){
				p_IsService = parameter.getParameterAsBoolean();
			}
			else if(name.equalsIgnoreCase("C_DocTypeTarget_ID")){
				p_C_RMADocType_ID = parameter.getParameterAsInt();
			}
			else if(name.equalsIgnoreCase("M_RMAType_ID")){
				p_M_RMAType_ID = parameter.getParameterAsInt();
			}
			else if(name.equalsIgnoreCase("C_DocTypeShipment_ID")){
				p_C_RMDocType_ID = parameter.getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter:" + name);
		 }
		
	}
	
	protected String doIt() throws Exception {
		//	Get Invoice Object
		MInvoice m_Invoice = new MInvoice(getCtx(), p_C_Invoice_ID, get_TrxName());
		if(p_IsService){
			//	Reverse Correct Allocations if Exists
			ReverseAllocation(p_C_Invoice_ID);
			//	Create Credit Note
			CreateCN(m_Invoice,m_RM);
			//	Allocate Invoice with Credit Note
			CreateAllocation(m_Invoice, m_CreditNote);
		}
		else{
			//	Reverse Correct Allocations if Exists
			ReverseAllocation(p_C_Invoice_ID);
			//	Get Order Info
			MOrder m_Order = new MOrder(getCtx(), m_Invoice.getC_Order_ID(), get_TrxName());
			//	Get Receipts 
			MInOut[] m_Outs = m_Order.getShipments();
			for(MInOut outs : m_Outs){
				if(outs.getDocStatus().equals(MInOut.DOCSTATUS_Completed)){
					//	Create Authorization
					CreateRMA(outs, p_M_RMAType_ID);
				}
			}
			//	Create Customer Return Material
			CreateRM(m_Invoice);
			//	Create Credit Note
			CreateCN(m_Invoice,m_RM);
			//	Allocate Invoice with Credit Note
			CreateAllocation(m_Invoice, m_CreditNote);
		}
		
		//	Display Credit Note Document
		addLog(m_CreditNote.getC_Invoice_ID(), m_CreditNote.getDateInvoiced(), m_CreditNote.getGrandTotal(), m_CreditNote.getDocumentNo(), m_CreditNote.get_Table_ID(), m_CreditNote.getC_Invoice_ID());
		return m_CreditNote.getDocumentNo();
	}
	
	/**
	 * This method reverse correct all allocations from invoice
	 * @autor Jorge Colmenarez, 21 sept. 2017, jcolmenarez@frontuari.com, Frontuari, C.A.
	 * @param Invoice_ID
	 */
	public void ReverseAllocation(int Invoice_ID){
		//		Get AllocationHdr 
		String sql = "SELECT DISTINCT ah.C_AllocationHdr_ID "
				+ "FROM C_AllocationLine al "
				+ "INNER JOIN C_AllocationHdr ah ON al.C_AllocationHdr_ID = ah.C_AllocationHdr_ID "
				+ "WHERE ah.DocStatus = 'CO' AND al.C_Invoice_ID = ? ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, get_TrxName());
			ps.setInt(1, Invoice_ID);
			rs = ps.executeQuery();
			while (rs.next())
			{
				MAllocationHdr m_AllHdr = new MAllocationHdr(getCtx(), rs.getInt("C_AllocationHdr_ID"), get_TrxName());
				if(m_AllHdr.processIt(MAllocationHdr.ACTION_Reverse_Correct)){
					m_AllHdr.saveEx();
				}
				else{
					throw new AdempiereException(m_AllHdr.getProcessMsg());
				}
			}
		} catch (SQLException e) {
			addLog(e.getMessage());
		} finally {
			DB.close(rs, ps);
			rs = null; ps = null;
		}
	}
	
	/**
	 * This method create a Credit Note from Fiscal Invoice to void
	 * @autor Jorge Colmenarez, 21 sept. 2017, jcolmenarez@frontuari.com, Frontuari, C.A.
	 * @param inv
	 * @param crm
	 */
	public void CreateCN(MInvoice inv, MInOut crm){
		//	Create new Object
		m_CreditNote = new MInvoice(getCtx(),0,get_TrxName());
		//	Copy Info from Fiscal Invoice
		PO.copyValues(inv, m_CreditNote);
		//	Set DocumentType
		m_CreditNote.setAD_Org_ID(p_AD_Org_ID);
		m_CreditNote.setDocumentNo(MSequence.getDocumentNo(p_C_DocType_ID, get_TrxName(), false, null));
		m_CreditNote.setC_DocTypeTarget_ID(p_C_DocType_ID);
		//	Set DocumentType New
		m_CreditNote.setC_DocType_ID(0);
		m_CreditNote.setDateInvoiced(new Timestamp(System.currentTimeMillis()));
		m_CreditNote.setDateAcct(new Timestamp(System.currentTimeMillis()));
		m_CreditNote.set_ValueOfColumn("LVE_InvoiceAffected_ID", inv.getC_Invoice_ID());
		m_CreditNote.setPaymentRule(MInvoice.PAYMENTRULE_OnCredit);
		m_CreditNote.setC_Payment_ID(0);
		m_CreditNote.setDocStatus(MInvoice.DOCSTATUS_Drafted);
		m_CreditNote.setIsPaid(false);
		m_CreditNote.setProcessed(false);
		m_CreditNote.setPosted(false);
		m_CreditNote.saveEx();
		//	Copy Lines
		int no = m_CreditNote.copyLinesFrom (inv, false, false);
		log.info("@Copied@=" + no);
		if(crm!=null){
			MInvoiceLine[] cnLines = m_CreditNote.getLines();
			MInOutLine[] crmLines = crm.getLines();
			for(int i = 0; i < cnLines.length; i++){
				MInvoiceLine line = cnLines[i];
				MInOutLine crmline = crmLines[i];
				if(crmline.getM_Product_ID()!=0){
					if(line.getM_Product_ID() == crmline.getM_Product_ID())
						line.setM_InOutLine_ID(crmline.getM_InOutLine_ID());	
				}
				else{
					if(line.getC_Charge_ID() == crmline.getC_Charge_ID())
						line.setM_InOutLine_ID(crmline.getM_InOutLine_ID());
				}
				line.saveEx(get_TrxName());
			}
		}
		//	Save with lines
		m_CreditNote.saveEx();
		//	Complete Credit Note
		if(m_CreditNote.processIt(MInvoice.ACTION_Complete)){
			m_CreditNote.saveEx();
		}
		else{
			rollback();
			throw new AdempiereException(m_CreditNote.getProcessMsg());
		}
	}
	
	/**
	 * This method create allocations between Invoice an Credit Note
	 * @autor Jorge Colmenarez, 21 sept. 2017, jcolmenarez@frontuari.com, Frontuari, C.A.
	 * @param inv
	 * @param creditNote
	 */
	public void CreateAllocation(MInvoice inv, MInvoice creditNote){
		//	Create AllocationHdr Object
		MAllocationHdr AHeader = new MAllocationHdr(getCtx(), 0, get_TrxName());
		AHeader.setAD_Org_ID(p_AD_Org_ID);
		AHeader.setDateTrx(new Timestamp(System.currentTimeMillis()));
		AHeader.setDateAcct(new Timestamp(System.currentTimeMillis()));
		AHeader.setC_Currency_ID(inv.getC_Currency_ID());
		
		AHeader.saveEx(get_TrxName());
		
		//	Allocation Invoice Line
		MAllocationLine ALineInv = new MAllocationLine(AHeader);
		ALineInv.setAD_Org_ID(p_AD_Org_ID);
		ALineInv.setC_BPartner_ID(inv.getC_BPartner_ID());
		ALineInv.setC_Invoice_ID(inv.getC_Invoice_ID());
		ALineInv.setAmount(inv.getGrandTotal());		
		ALineInv.saveEx(get_TrxName());
		
		//	Allocation Credit Note Line
		MAllocationLine ALineCN = new MAllocationLine(AHeader);
		ALineCN.setAD_Org_ID(p_AD_Org_ID);
		ALineCN.setC_BPartner_ID(creditNote.getC_BPartner_ID());
		ALineCN.setC_Invoice_ID(creditNote.getC_Invoice_ID());
		ALineCN.setAmount(creditNote.getGrandTotal().negate());		
		ALineCN.saveEx(get_TrxName());
		//	Complete Allocation
		if(AHeader.processIt(MAllocationHdr.ACTION_Complete))
			AHeader.saveEx(get_TrxName());
		else{
			rollback();
			throw new AdempiereException(AHeader.getProcessMsg());
		}
	}
	
	/**
	 * This method create RMA from InOut
	 * @param inout
	 * @param RMAType
	 */
	public void CreateRMA(MInOut inout, int RMAType){
		X_M_RMAType m_RMAType = new X_M_RMAType(getCtx(), RMAType, get_TrxName()); 
		MRMA m_RMA = new MRMA(getCtx(), 0, get_TrxName());
		m_RMA.setAD_Org_ID(p_AD_Org_ID);
		m_RMA.setC_DocType_ID(p_C_RMADocType_ID);
		m_RMA.setIsSOTrx(inout.isSOTrx());
		m_RMA.setName(m_RMAType.getName());
		m_RMA.setM_RMAType_ID(RMAType);
		m_RMA.setM_InOut_ID(inout.getM_InOut_ID());
		m_RMA.setC_BPartner_ID(inout.getC_BPartner_ID());
		m_RMA.setSalesRep_ID(inout.getSalesRep_ID());
		//	save
		m_RMA.saveEx(get_TrxName());
		//	Get InOut Lines
		MInOutLine[] ioLines = inout.getLines();
		//	Create RMA Lines
		for(MInOutLine line: ioLines){
			MRMALine m_RMALine = new MRMALine(getCtx(), 0, get_TrxName());
			m_RMALine.setM_RMA_ID(m_RMA.getM_RMA_ID());
			m_RMALine.setAD_Org_ID(p_AD_Org_ID);
			m_RMALine.setM_InOutLine_ID(line.getM_InOutLine_ID());
			//	Set Product or Charge
			if(line.getM_Product_ID()!=0)
				m_RMALine.setM_Product_ID(line.getM_Product_ID());
			else
				m_RMALine.setC_Charge_ID(line.getC_Charge_ID());
			m_RMALine.setQty(line.getMovementQty());
			m_RMALine.setAmt(line.getC_OrderLine().getPriceEntered());
			m_RMALine.setC_Tax_ID(line.getC_OrderLine().getC_Tax_ID());
			m_RMALine.setLineNetAmt(line.getC_OrderLine().getLineNetAmt());
			//	save
			m_RMALine.saveEx(get_TrxName());
		}
		//	Complete RMA
		if(m_RMA.processIt(MRMA.ACTION_Complete)){
			m_RMA.saveEx(get_TrxName());
		}
		else{
			rollback();
			throw new AdempiereException(m_RMA.getProcessMsg());
		}
	}
	
	/**
	 * This method create Customer Return Material from Invoice to Void 
	 * @param inv
	 */
	public void CreateRM(MInvoice inv){
		int RMA_ID = 0; 
		//	Create RM 
		m_RM = new MInOut(getCtx(),0,get_TrxName());
		m_RM.setAD_Org_ID(p_AD_Org_ID);
		m_RM.setC_DocType_ID(p_C_RMDocType_ID);
		m_RM.setMovementType(MInOut.MOVEMENTTYPE_CustomerReturns);
		m_RM.setIsSOTrx(inv.isSOTrx());
		m_RM.setMovementDate(new Timestamp(System.currentTimeMillis()));
		m_RM.setDateAcct(new Timestamp(System.currentTimeMillis()));
		m_RM.setC_BPartner_ID(inv.getC_BPartner_ID());
		m_RM.setC_BPartner_Location_ID(inv.getC_BPartner_Location_ID());
		m_RM.setM_Warehouse_ID(inv.getC_Order().getM_Warehouse_ID());
		m_RM.setSalesRep_ID(inv.getSalesRep_ID());
		m_RM.setPriorityRule(MInOut.PRIORITYRULE_Medium);
		m_RM.setFreightCostRule(MInOut.FREIGHTCOSTRULE_FreightIncluded);
		//	Save
		m_RM.saveEx(get_TrxName());		
		//	Search RMA and Lines
		String sql = "SELECT m.M_RMA_ID,ml.M_RMALine_ID "
				+ "FROM M_RMA m "
				+ "INNER JOIN M_RMALine ml ON m.M_RMA_ID = ml.M_RMA_ID "
				+ "INNER JOIN M_InOutLine iol ON ml.M_InOutLine_ID = iol.M_InOutLine_ID "
				+ "INNER JOIN C_OrderLine ol ON iol.C_OrderLine_Id = ol.C_OrderLine_ID "
				+ "INNER JOIN M_InOut io ON iol.M_InOut_ID = io.M_InOut_ID "
				+ "WHERE io.DocStatus = 'CO' AND ol.C_Order_ID = ? "
				+ "ORDER BY m.M_RMA_ID,ml.M_RMALine_ID ASC ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, get_TrxName());
			ps.setInt(1, inv.getC_Order_ID());
			rs = ps.executeQuery();
			while (rs.next())
			{
				//	Set RMA_ID
				RMA_ID = rs.getInt("M_RMA_ID");
				MInOutLine m_RMLine = new MInOutLine(m_RM);
				//	Set RMA Line from Result Set
				m_RMLine.setM_RMALine_ID(rs.getInt("M_RMALine_ID"));
				//	Get RMA Line Object
				MRMALine m_RMALine = new MRMALine(getCtx(), rs.getInt("M_RMALine_ID"), get_TrxName());
				//	Set Product or Charge
				if(m_RMALine.getM_Product_ID()!=0){
					m_RMLine.setM_Product_ID(m_RMALine.getM_Product_ID());
					m_RMLine.setM_AttributeSetInstance_ID(m_RMALine.getM_AttributeSetInstance_ID());
				}else{
					m_RMLine.setC_Charge_ID(m_RMALine.getC_Charge_ID());
				}
				m_RMLine.setC_UOM_ID(m_RMALine.getC_UOM_ID());
				m_RMLine.setM_Locator_ID(m_RMALine.getM_Locator_ID());
				m_RMLine.setMovementQty(m_RMALine.getQty());
				m_RMLine.setQtyEntered(m_RMALine.getQty());
				//	Save
				m_RMLine.saveEx(get_TrxName());
			}
		} catch (SQLException e) {
			addLog(e.getMessage());
		} finally {
			DB.close(rs, ps);
			rs = null; ps = null;
		}
		
		//	Set last M_RMA_ID
		m_RM.setM_RMA_ID(RMA_ID);
		m_RM.saveEx(get_TrxName());
		
		if(m_RM.processIt(MInOut.ACTION_Complete)){
			m_RM.saveEx(get_TrxName());
		}else{
			rollback();
			throw new AdempiereException(m_RM.getProcessMsg());
		}
	}

}
