package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 *	Major Plan Line Model.
 * 	They are set in the process() method.
 *
 *  @author Jorge Colmenarez, jcolmenarez@frontuari.com, Frontuari, C.A. http://www.frontuari.com
 *  @version $Id: MLVE_MajorPlan.java,v 1.0 2018/03/12 15:54 ftujcolmenarez Exp $
 */
public class MLVEMajorPlanLine extends X_LVE_MajorPlanLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7897307896202113334L;

	public MLVEMajorPlanLine(Properties ctx, int LVE_MajorPlanLine_ID, String trxName) {
		super(ctx, LVE_MajorPlanLine_ID, trxName);
	}
	
	public MLVEMajorPlanLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	/** Parent						*/
	private MLVEMajorPlan	m_parent = null;

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MLVEMajorPlan getParent()
	{
		if (m_parent == null)
			m_parent = new MLVEMajorPlan(getCtx(), getLVE_MajorPlan_ID(), get_TrxName());
		return m_parent;
	}	//	getParent

	/**
	 * 	Parent Constructor
	 * 	@param majorplan parent
	 */
	public MLVEMajorPlanLine (MLVEMajorPlan majorplan)
	{
		this (majorplan.getCtx(), 0, majorplan.get_TrxName());
		if (majorplan.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(majorplan.getAD_Client_ID(), majorplan.getAD_Org_ID());
		setLVE_MajorPlan_ID (majorplan.getLVE_MajorPlan_ID());
	}	//	MInvoiceLine
	
	/**	Major Plan Invoices			*/
	private MInvoice[]	m_invoices;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MInvoice[] getInvoices (String whereClause)
	{
		String whereClauseFinal = "EXISTS (SELECT 1 FROM LVE_MajorPlanLine WHERE LVE_MajorPlanLine.C_Invoice_ID = C_Invoice.C_Invoice_ID AND LVE_MajorPlan_ID=?) ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MInvoice> list = new Query(getCtx(), MInvoice.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlan_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(MInvoice.COLUMNNAME_DocumentNo)
										.list();
		return list.toArray(new MInvoice[list.size()]);
	}	//	getLines

	/**
	 * 	Get Major Plan Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MInvoice[] getInvoices (boolean requery)
	{
		if (m_invoices == null || m_invoices.length == 0 || requery)
			m_invoices = getInvoices(null);
		set_TrxName(m_invoices, get_TrxName());
		return m_invoices;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MInvoice[] getInvoices()
	{
		return getInvoices(false);
	}	//	getLines
	
	/**	Major Plan Allocation Lines			*/
	private MAllocationLine[]	m_allLines;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MAllocationLine[] getAllocationLines (String whereClause)
	{
		String whereClauseFinal = " EXISTS (SELECT 1 FROM C_AllocationHdr WHERE C_AllocationHdr.DocStatus IN ('CO','CL') AND C_AllocationHdr.C_AllocationHdr_ID = C_AllocationLine.C_AllocationHdr_ID) "
				+ "AND EXISTS (SELECT 1 FROM LVE_MajorPlanLine WHERE LVE_MajorPlanLine.LVE_MajorPlanLine_ID = C_AllocationLine.LVE_MajorPlanLine_ID AND LVE_MajorPlan_ID=?) ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MInvoice> list = new Query(getCtx(), MAllocationLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlan_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(MAllocationLine.COLUMNNAME_C_AllocationLine_ID)
										.list();
		return list.toArray(new MAllocationLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get Major Plan Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MAllocationLine[] getAllocationLines (boolean requery)
	{
		if (m_allLines == null || m_allLines.length == 0 || requery)
			m_allLines = getAllocationLines(null);
		set_TrxName(m_allLines, get_TrxName());
		return m_allLines;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MAllocationLine[] getAllocationLines()
	{
		return getAllocationLines(false);
	}	//	getLines
	
	/**
	 * get openAmt from Major Plan Line
	 * @author Jorge Colmenarez,jcolmenarez@frontuari.com, http://www.frontuari.com 
	 * @return openAmt
	 */
	public BigDecimal openAmt(){
		BigDecimal openAmt = BigDecimal.ZERO;
		
		String sql = "SELECT MAX(mpl.Amount)-SUM(COALESCE(ABS(al.Amount),0)) AS openAmt "
				+ "FROM LVE_MajorPlanLine mpl "
				+ "INNER JOIN LVE_MajorPlan mp ON(mpl.LVE_MajorPlan_ID = mp.LVE_MajorPlan_ID) "
				+ "INNER JOIN LVE_MajorPlanType mpt ON (mp.LVE_MajorPlanType_ID = mpt.LVE_MajorPlanType_ID) "
				+ "LEFT JOIN C_AllocationLine al ON(mpl.LVE_MajorPlanLine_ID = al.LVE_MajorPlanLine_ID AND mpt.C_Charge_ID = al.C_Charge_ID) "
				+ "LEFT JOIN C_AllocationHdr ah ON(al.C_AllocationHdr_ID = ah.C_AllocationHdr_ID AND ah.DocStatus IN ('CO','CL')) "
				+ "WHERE mpl.LVE_MajorPlanLine_ID = ? ";
		
		openAmt = DB.getSQLValueBD(get_TrxName(), sql, getLVE_MajorPlanLine_ID());
		
		return openAmt;
	}

	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (log.isLoggable(Level.FINE)) log.fine("New=" + newRecord);
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "LVE_MajorPlanLine"));
			return false;
		}

		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM LVE_MajorPlanLine WHERE LVE_MajorPlan_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getLVE_MajorPlan_ID());
			setLine (ii);
		}
		//	Set Header
		if((!newRecord && is_ValueChanged(COLUMNNAME_Amount)) || newRecord){
			getParent().setAmount(getParent().getAmount().add(getAmount()));
			getParent().saveEx();
		}
		//	Set DueDate
		if(getC_Invoice_ID() > 0 && getDueDate() == null){
			String sql = "SELECT paymenttermduedate(mpt.C_PaymentTerm_ID,mp.DateDoc) "
					+ "FROM LVE_MajorPlan mp "
					+ "INNER JOIN LVE_MajorPlanTypeLine mpt ON mp.LVE_MajorPlanType_ID = mpt.LVE_MajorPlanType_ID "
					+ "WHERE mp.LVE_MajorPlan_ID = ? "
					+ "AND C_BPartner_ID = (SELECT C_BPartner_ID FROM C_Invoice WHERE C_Invoice_ID = ?)";
	        Timestamp duedate = DB.getSQLValueTS(null, sql, new Object[]{(Integer)getLVE_MajorPlan_ID(),getC_Invoice_ID()});
	        setDueDate(duedate);
		}
		
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (getParent().isProcessed())
			return success;
		
		//	Update Parent Amount
		if(is_ValueChanged(COLUMNNAME_Amount)){
			getParent().setAmount(getParent().getAmount().add(getAmount()));
			getParent().saveEx();
		}
				
    	return true;
	}	//	afterSave


	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		//	Update Parent Amount
		getParent().setAmount(getParent().getAmount().subtract(getAmount()));
		getParent().saveEx();
				
    	return true;
	}	//	beforeDelete

}
