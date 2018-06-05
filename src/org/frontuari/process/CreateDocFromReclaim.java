package org.frontuari.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MSequence;
import org.compiere.model.PO;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.frontuari.model.MLVEResultReturnGuarantee;

public class CreateDocFromReclaim extends SvrProcess {
	
	/*	Organization Trx 				*/
	private int p_AD_Org_ID			=	0;
	/*	Invoice 						*/
	private int p_C_Invoice_ID		=	0;
	/*	DocType for Credit Note			*/
	private int p_C_DocType_ID		=	0;
	/*	Charge for Credit Note			*/
	private int p_C_Charge_ID		=	0;
	/*	Credit Note Object				*/
	MInvoice m_CreditNote			= 	null;
	/*	Result Return Guarantee 		*/
	MLVEResultReturnGuarantee rrg	=	null;

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
			else if(name.equalsIgnoreCase("C_Charge_ID")){
				p_C_Charge_ID = parameter.getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter:" + name);
		 }
	}

	@Override
	protected String doIt() throws Exception {
		rrg = new MLVEResultReturnGuarantee(getCtx(), getRecord_ID(), get_TrxName());
		//	Get Invoice Object
		MInvoice m_Invoice = new MInvoice(getCtx(), p_C_Invoice_ID, get_TrxName());
		//	Create Credit Note
		CreateCN(m_Invoice);
		//	Allocate Invoice with Credit Note
		CreateAllocation(m_Invoice, m_CreditNote);
		//	Save Documents
		rrg.setC_Invoice_ID(m_CreditNote.getC_Invoice_ID());
		rrg.saveEx();
		//	Display Credit Note Document
		addLog(m_CreditNote.getC_Invoice_ID(), m_CreditNote.getDateInvoiced(), m_CreditNote.getGrandTotal(), m_CreditNote.getDocumentNo(), m_CreditNote.get_Table_ID(), m_CreditNote.getC_Invoice_ID());
		return m_CreditNote.getDocumentNo();
	}
	
	/**
	 * This method create a Credit Note from Fiscal Invoice to allocated
	 * @autor Jorge Colmenarez, 29 may. 2018, jcolmenarez@frontuari.com, Frontuari, C.A.
	 * @param inv
	 */
	public void CreateCN(MInvoice inv){
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
		//	Create Line
		MInvoiceLine cnl = new MInvoiceLine(m_CreditNote);
		cnl.setC_Charge_ID(p_C_Charge_ID);
		cnl.setC_UOM_ID(Env.getContextAsInt(getCtx(), "#C_UOM_ID"));
		cnl.setQty(BigDecimal.ONE);
		cnl.setPrice(rrg.getAmount());
		cnl.saveEx();
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
		ALineInv.setAmount(creditNote.getGrandTotal());		
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
	
	

}
