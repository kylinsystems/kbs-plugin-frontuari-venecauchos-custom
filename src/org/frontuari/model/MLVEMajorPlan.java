package org.frontuari.model;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MSequence;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	Major Plan Model.
 * 	They are set in the process() method.
 *
 *  @author Jorge Colmenarez, jcolmenarez@frontuari.com, Frontuari, C.A. http://www.frontuari.com
 *  @version $Id: MLVE_MajorPlan.java,v 1.0 2018/03/12 15:54 ftujcolmenarez Exp $
 */
public class MLVEMajorPlan extends X_LVE_MajorPlan implements DocAction,DocOptions {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7963886873641556695L;

	public MLVEMajorPlan(Properties ctx, int LVE_MajorPlan_ID, String trxName) {
		super(ctx, LVE_MajorPlan_ID, trxName);
		if(LVE_MajorPlan_ID == 0){
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			setDocAction (DOCACTION_Complete);

			setDateDoc (new Timestamp (System.currentTimeMillis ()));
			setAmount(Env.ZERO);
			super.setProcessed (false);
		}
	}

	public MLVEMajorPlan(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * get DocNo from Secuence
	 */
	
	
	/**	Major Plan Lines			*/
	private MLVEMajorPlanLine[]	m_lines;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	public MLVEMajorPlanLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "LVE_MajorPlan_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MLVEMajorPlanLine> list = new Query(getCtx(), I_LVE_MajorPlanLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlan_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(I_LVE_MajorPlanLine.COLUMNNAME_Line)
										.list();
		return list.toArray(new MLVEMajorPlanLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get Major Plan Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MLVEMajorPlanLine[] getLines (boolean requery)
	{
		if (m_lines == null || m_lines.length == 0 || requery)
			m_lines = getLines(null);
		set_TrxName(m_lines, get_TrxName());
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MLVEMajorPlanLine[] getLines()
	{
		return getLines(false);
	}	//	getLines
	
	/**	Major Plan Pay Bank Charges Allocated			*/
	private MPayment[]	m_payment;

	/**
	 * 	Get Pays Bank Charges of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MPayment[] getBCAllocated (String whereClause)
	{
		String whereClauseFinal = "C_Charge_ID IS NOT NULL AND LVE_MajorPlan_ID IS NOT NULL AND EXISTS (SELECT 1 FROM LVE_MajorPlan WHERE LVE_MajorPlan.LVE_MajorPlan_ID = C_Payment.LVE_MajorPlan_ID AND LVE_MajorPlan.LVE_MajorPlan_ID=?) ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MInvoice> list = new Query(getCtx(), MPayment.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlan_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(MPayment.COLUMNNAME_DocumentNo)
										.list();
		return list.toArray(new MPayment[list.size()]);
	}	//	getBCAllocated

	/**
	 * 	Get Pays Bank Charges
	 * 	@param requery
	 * 	@return lines
	 */
	public MPayment[] getBCAllocated (boolean requery)
	{
		if (m_payment == null || m_payment.length == 0 || requery)
			m_payment = getBCAllocated(null);
		set_TrxName(m_payment, get_TrxName());
		return m_payment;
	}	//	getBCAllocated

	/**
	 * 	Get Pays Bank Charges
	 * 	@return lines
	 */
	public MPayment[] getBCAllocated()
	{
		return getBCAllocated(false);
	}	//	getBCAllocated
	
	/**
	 * 	Set Processed.
	 * 	Propergate to Lines
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder set = new StringBuilder("SET Processed='")
		.append((processed ? "Y" : "N"))
		.append("' WHERE LVE_MajorPlan_ID=").append(getLVE_MajorPlan_ID());
		
		StringBuilder msgdb = new StringBuilder("UPDATE LVE_MajorPlanLine ").append(set);
		int noLine = DB.executeUpdate(msgdb.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MLVE_MajorPlan[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append(",Amount=").append(getAmount());
		if (m_lines != null)
			sb.append(" (#").append(m_lines.length).append(")");
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Client/Org Check
		if (getAD_Org_ID() == 0)
		{
			int context_AD_Org_ID = Env.getAD_Org_ID(getCtx());
			if (context_AD_Org_ID != 0)
			{
				setAD_Org_ID(context_AD_Org_ID);
				log.warning("Changed Org to Context=" + context_AD_Org_ID);
			}
		}
		if (getAD_Client_ID() == 0)
		{
			m_processMsg = "AD_Client_ID = 0";
			return false;
		}
		//	Set definitive sequence
		if(getDocumentNo().contains("<") && getDocumentNo().contains(">")){
			MSequence seq = new MSequence(getCtx(), getLVE_MajorPlanType().getDocNoSequence_ID(), get_TrxName());
			setDocumentNo(MSequence.getDocumentNoFromSeq(seq, get_TrxName(), null));
		}

		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		if (is_ValueChanged("AD_Org_ID"))
		{
			StringBuilder sql = new StringBuilder("UPDATE LVE_MajorPlanLine mpl")
				.append(" SET AD_Org_ID =")
					.append("(SELECT AD_Org_ID")
					.append(" FROM LVE_MajorPlan mp WHERE mpl.LVE_MajorPlan_ID=mp.LVE_MajorPlan_ID) ")
				.append("WHERE LVE_MajorPlan_ID=").append(getLVE_MajorPlan_ID());
			int no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Lines -> #" + no);
		}
		return true;
	}	//	afterSave
	
	/**
	 * Before Delete
	 * @author Jorge Colmenarez,jcolmenarez@frontuari.com, http://www.frontuari.com
	 * @return success 
	 */
	protected boolean beforeDelete ()
	{
		if (isProcessed()) {
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDeleteTrx"));
			return false;
		}

		for (MLVEMajorPlanLine line : getLines(null)) {
			line.deleteEx(true);
		}
		return true;
	}	// beforeDelete
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds)
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Check Major Plan Type
		if (getLVE_MajorPlanType_ID() == 0)
		{
			m_processMsg = "@No@ @LVE_MajorPlanType_ID@";
			return DocAction.STATUS_Invalid;
		}
		
		//	Lines
		MLVEMajorPlanLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		
		//	Check that not exceeded approval amt
		String sql = "SELECT MAX(mpt.AmtApproval)-SUM(COALESCE(mpl.Amount,0)) AS AmtAvailable "
				+ "FROM LVE_MajorPlanType mpt "
				+ "LEFT JOIN LVE_MajorPlan mp ON mpt.LVE_MajorPlanType_ID = mp.LVE_MajorPlanType_ID "
				+ "LEFT JOIN LVE_MajorPlanLine mpl ON mp.LVE_MajorPlan_ID = mpl.LVE_MajorPlan_ID AND mpl.IsPaid = 'N'"
				+ "WHERE mp.LVE_MajorPlanType_ID = ? AND mp.DocStatus IN ('CO','CL') ";
		BigDecimal amtAvailable = DB.getSQLValueBD(get_TrxName(), sql, getLVE_MajorPlanType_ID());
		if(getAmount().compareTo(amtAvailable) > 0){
			m_processMsg = "@majorplan.creditamt.exceeded@";
			return DocAction.STATUS_Invalid;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Add up Amounts
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		if (log.isLoggable(Level.INFO)) log.info(toString());
		StringBuilder info = new StringBuilder();
		
		//	Create Bank Movements
		String sql = "SELECT mptl.C_BPartner_ID,i.Amount "
				+ "FROM LVE_MajorPlanTypeLine mptl "
				+ "INNER JOIN LVE_MajorPlan mp ON mptl.LVE_MajorPlanType_ID = mp.LVE_MajorPlanType_ID "
				+ "INNER JOIN (SELECT mpl.LVE_MajorPlan_ID,i.C_BPartner_ID,SUM(Amount) AS Amount "
				+ "FROM LVE_MajorPlanLine mpl "
				+ "INNER JOIN C_Invoice i ON mpl.C_Invoice_ID = i.C_Invoice_ID "
				+ "GROUP BY mpl.LVE_MajorPlan_ID,i.C_BPartner_ID) i ON i.LVE_MajorPlan_ID = mp.LVE_MajorPlan_ID AND i.C_BPartner_ID = mptl.C_BPartner_ID "
				+ "WHERE mp.LVE_MajorPlan_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getLVE_MajorPlan_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				//	Create Receipt by BPartner
				createReceipt(rs.getBigDecimal("Amount"),info);
				//	Create Payment by BPartner
				createPayment(rs.getInt("C_BPartner_ID"),rs.getBigDecimal("Amount"),info);
				//	Create Bank Charges
				createBankCharge(rs.getBigDecimal("Amount"),info);
			}
		}
		catch(Exception e){
			throw new AdempiereException(e);
		}

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			if (info.length() > 0)
				info.append(" - ");
			info.append(valid);
			m_processMsg = info.toString();
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		m_processMsg = info.toString();
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			// Before Void
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;
			
			//	Set lines to 0
			MLVEMajorPlanLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MLVEMajorPlanLine line = lines[i];
				line.setAmount(Env.ZERO);
				line.saveEx(get_TrxName());
			}
			setC_Payment_ID(0);
			setAmount(BigDecimal.ZERO);
		}
		else{
			//	Verified if have payment
			if(getC_Payment_ID() != 0){
				MPayment pay = new MPayment(getCtx(), getC_Payment_ID(), get_TrxName());
				if(pay.reverseCorrectIt()){
					pay.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
					pay.saveEx(get_TrxName());
				}
			}

			//	Check if have bank charges allocated
			MPayment[] pays = getBCAllocated(" AND DocStatus IN ('CO','CL')");
			if(pays.length > 0){
				for(MPayment payBC : pays){
					if(payBC.reverseCorrectIt()){
						payBC.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
						payBC.saveEx(get_TrxName());
					}
				}
			}
			
			//	Set lines to 0
			MLVEMajorPlanLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MLVEMajorPlanLine line = lines[i];
				line.setAmount(Env.ZERO);
				line.setIsPaid(false);
				// Verified if have payment
				if(line.getC_Payment_ID() != 0){
					MPayment payVendor = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
					if(payVendor.getDocStatus().equalsIgnoreCase(MPayment.DOCACTION_Complete)){
						if(payVendor.reverseCorrectIt()){
							payVendor.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
							payVendor.saveEx(get_TrxName());
						}
					}
				}
				line.setC_Payment_ID(0);
				line.saveEx(get_TrxName());
				//	Verify if have Pays allocated
				MAllocationLine[] allLines = line.getAllocationLines();
				for(MAllocationLine allLine : allLines){
					MPayment payAlloc = new MPayment(getCtx(), allLine.getC_Payment_ID(), get_TrxName());
					if(payAlloc.getDocStatus().equalsIgnoreCase(MPayment.DOCSTATUS_Completed)){
						if(payAlloc.reverseCorrectIt()){
							payAlloc.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
							payAlloc.saveEx(get_TrxName());
						}
					}
				}
			}
			setC_Payment_ID(0);
			setAmount(BigDecimal.ZERO);
		}

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Re-activate.
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		//	Verified if have payment
		if(getC_Payment_ID() != 0){
			MPayment pay = new MPayment(getCtx(), getC_Payment_ID(), get_TrxName());
			if(pay.reverseCorrectIt()){
				pay.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
				pay.saveEx(get_TrxName());
			}
		}
		
		//	Check if have bank charges allocated
		MPayment[] pays = getBCAllocated(" AND DocStatus IN ('CO','CL')");
		if(pays.length > 0){
			for(MPayment payBC : pays){
				if(payBC.reverseCorrectIt()){
					payBC.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
					payBC.saveEx(get_TrxName());
				}
			}
		}
		
		//	Set lines to 0
		MLVEMajorPlanLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MLVEMajorPlanLine line = lines[i];
			line.setIsPaid(false);
			line.setProcessed(false);
			//	Verified if have payment
			if(line.getC_Payment_ID() != 0){
				MPayment payVendor = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
				if(payVendor.getDocStatus().equalsIgnoreCase(MPayment.DOCACTION_Complete)){
					if(payVendor.reverseCorrectIt()){
						payVendor.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
						payVendor.saveEx(get_TrxName());
					}
				}
			}
			line.setC_Payment_ID(0);
			line.saveEx(get_TrxName());
			//	Verify if have Pays allocated
			MAllocationLine[] allLines = line.getAllocationLines();
			for(MAllocationLine allLine : allLines){
				MPayment payAlloc = new MPayment(getCtx(), allLine.getC_Payment_ID(), get_TrxName());
				if(payAlloc.getDocStatus().equalsIgnoreCase(MPayment.DOCSTATUS_Completed)){
					if(payAlloc.reverseCorrectIt()){
						payAlloc.addDescription(Msg.translate(getCtx(),"Voided")+" "+Msg.translate(getCtx(),"from")+" "+Msg.translate(getCtx(),"LVE_MajorPlan_ID")+": "+getDocumentNo());
						payAlloc.saveEx(get_TrxName());
					}
				}
			}
		}
		setC_Payment_ID(0);
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}	//	reActivateIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}	//	closeIt

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * Set process message
	 * @param processMsg
	 */
	public void setProcessMessage(String processMsg)
	{
		m_processMsg = processMsg;
	}

	@Override
	public boolean processIt(String processAction) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}

	@Override
	public boolean unlockIt() {
		return false;
	}

	@Override
	public boolean invalidateIt() {
		return false;
	}

	@Override
	public boolean approveIt() {
		return false;
	}

	@Override
	public boolean rejectIt() {
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		return false;
	}

	@Override
	public String getSummary() {
		return null;
	}

	@Override
	public String getDocumentInfo() {
		return null;
	}

	@Override
	public File createPDF() {
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.ORDER, getLVE_MajorPlan_ID(), get_TrxName());
		if (re == null)
			return null;
		MPrintFormat format = re.getPrintFormat();
		// We have a Jasper Print Format
		// ==============================
		if(format.getJasperProcess_ID() > 0)
		{
			ProcessInfo pi = new ProcessInfo ("", format.getJasperProcess_ID());
			pi.setRecord_ID ( getLVE_MajorPlan_ID() );
			pi.setIsBatch(true);
			
			ServerProcessCtl.process(pi, null);
			
			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return re.getPDF(file);
	}	//	createPDF

	@Override
	public int getDoc_User_ID() {
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return null;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		if (options == null)
			throw new IllegalArgumentException("Option array parameter is null");
		if (docAction == null)
			throw new IllegalArgumentException("Doc action array parameter is null");

		// If a document is drafted or invalid, the users are able to complete, prepare or void
		if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_Invalid)) {
			options[index++] = DocumentEngine.ACTION_Prepare;

			// If the document is already completed, we also want to be able to reactivate or void it instead of only closing it
		} else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
			options[index++] = DocumentEngine.ACTION_ReActivate;
			options[index++] = DocumentEngine.ACTION_Void;
		}

		return index;
	}
	/**
	 * Create receipt from Major Plan
	 * @author Jorge Colmenarez,jcolmenarez@frontuari.com,http://www.frontuari.com
	 * @param info 
	 * @param ReceiptAmt
	 * @return Receipt
	 */
	private void createReceipt(BigDecimal receiptAmt, StringBuilder info){
		try{
			MPayment receipt = new MPayment(getCtx(), 0, get_TrxName());
			receipt.setAD_Org_ID(getAD_Org_ID());
			receipt.setDateTrx(getDateDoc());
			receipt.setDateAcct(getDateDoc());
			receipt.setIsReceipt(true);
			receipt.setC_Currency_ID(Env.getContextAsInt(getCtx(), "$C_Currency_ID"));
			receipt.setC_DocType_ID(getLVE_MajorPlanType().getC_DocType_ID());
			receipt.setC_BankAccount_ID(getLVE_MajorPlanType().getC_BankAccount_ID());
			receipt.setC_Charge_ID(getLVE_MajorPlanType().getC_Charge_ID());
			receipt.setC_BPartner_ID(getLVE_MajorPlanType().getC_BPartner_ID());
			receipt.setTenderType(MPayment.TENDERTYPE_Account);
			receipt.setPayAmt(receiptAmt);
			receipt.processIt(MPayment.ACTION_Complete);
			receipt.saveEx(get_TrxName());
			//	Set Receipt into Major Plan Header
			setC_Payment_ID(receipt.getC_Payment_ID());
			saveEx(get_TrxName());
			log.info("["+Msg.translate(getCtx(), "C_DocType_ID")+" => "+receipt.getC_DocType().getName()+" "+Msg.translate(getCtx(), "DocumentNo") + " : " +receipt.getDocumentNo()+"] \n");
			info.append("@C_Payment_ID@: ").append(receipt.getDocumentNo());
			String msg = receipt.getProcessMsg();
			if (msg != null && msg.length() > 0)
				info.append(" (").append(msg).append(")");
		}
		catch(Exception e){
			throw new AdempiereException(e);
		}
	}//	createReceipt
	
	/**
	 * Create vendor payment from Major Plan
	 * @author Jorge Colmenarez,jcolmenarez@frontuari.com, http://www.frontuari.com
	 * @param info 
	 * @param PayAmt
	 * @return Payment 
	 */
	private void createPayment(int BPartner_ID,BigDecimal payAmt, StringBuilder info){
		//	Create Vendor Payment
		MPayment pay = new MPayment(getCtx(), 0, get_TrxName());
		pay.setAD_Org_ID(getAD_Org_ID());
		pay.setDateTrx(getDateDoc());
		pay.setDateAcct(getDateDoc());
		pay.setIsReceipt(false);
		pay.setC_Currency_ID(Env.getContextAsInt(getCtx(), "$C_Currency_ID"));
		pay.setC_DocType_ID(getLVE_MajorPlanType().getC_DocTypeTarget_ID());
		pay.setC_BankAccount_ID(getLVE_MajorPlanType().getC_BankAccount_ID());
		pay.setC_BPartner_ID(BPartner_ID);
		pay.setTenderType(MPayment.TENDERTYPE_Account);
		pay.setPayAmt(payAmt);
		//	Set Major Plan Reference
		pay.set_ValueOfColumn("LVE_MajorPlan_ID", getLVE_MajorPlan_ID());
		pay.saveEx(get_TrxName());
		//	Create Payment Lines
		String where = "AND EXISTS(SELECT 1 FROM C_Invoice "
				+ "WHERE C_Invoice.C_Invoice_ID = LVE_MajorPlanLine.C_Invoice_ID "
				+ "AND C_Invoice.C_BPartner_ID = "+BPartner_ID+")";
		MLVEMajorPlanLine[] lines = getLines(where);
		for(MLVEMajorPlanLine line : lines){
			MPaymentAllocate payAll = new MPaymentAllocate(getCtx(), 0, get_TrxName());
			payAll.setC_Payment_ID(pay.getC_Payment_ID());
			payAll.setC_Invoice_ID(line.getC_Invoice_ID());
			payAll.setInvoiceAmt(line.getAmount());
			payAll.setAmount(line.getAmount());
			payAll.saveEx(get_TrxName());
		}
		//	Complete payment
		pay.processIt(MPayment.ACTION_Complete);
		pay.saveEx(get_TrxName());
		//	Set Payment Vendor into Major Plan Line
		for(MLVEMajorPlanLine line : lines){
			line.setC_Payment_ID(pay.getC_Payment_ID());
			line.saveEx(get_TrxName());
		}
		log.info("["+Msg.translate(getCtx(), "C_DocType_ID")+" => "+pay.getC_DocType().getName()+" "+Msg.translate(getCtx(), "DocumentNo") + " : " +pay.getDocumentNo()+"] \n");
		info.append(Env.NL+"@C_Payment_ID@: ").append(pay.getDocumentNo());
		String msg = pay.getProcessMsg();
		if (msg != null && msg.length() > 0)
			info.append(" (").append(msg).append(")");
	}// createPayment	
	
	/**
	 * Create Bank Charges from MajorPlanType
	 * @author Jorge Colmenarez,jcolmenarez@frontuari.com, http://www.frontuari.com 
	 * @param info 
	 * @param PayAmt
	 * @return Payments 
	 */
	private void createBankCharge(BigDecimal amt, StringBuilder info)
	{
		MLVEMajorPlanType mpt = new MLVEMajorPlanType(getCtx(), getLVE_MajorPlanType_ID(), get_TrxName());
		MLVEMajorPlanBankCharge[] bankcharges = mpt.getBankCharges();
		if(bankcharges.length == 0){
			log.warning("No "+Msg.translate(getCtx(),"LVE_MajorPlanBankCharge"));
		}else{
			//	Create new Payment or Receipt from bank charge
			for(MLVEMajorPlanBankCharge bankcharge : bankcharges){
				MPayment bchPayment = new MPayment(getCtx(), 0, get_TrxName());
				bchPayment.setAD_Org_ID(getAD_Org_ID());
				bchPayment.setDateTrx(getDateDoc());
				bchPayment.setDateAcct(getDateDoc());
				bchPayment.setDescription(bankcharge.getName());
				bchPayment.setC_Currency_ID(Env.getContextAsInt(getCtx(), "$C_Currency_ID"));
				bchPayment.setC_DocType_ID(bankcharge.getC_DocType_ID());
				bchPayment.setIsReceipt(bankcharge.getC_DocType().isSOTrx());
				bchPayment.setC_BankAccount_ID(mpt.getC_BankAccount_ID());
				bchPayment.setC_BPartner_ID(mpt.getC_BPartner_ID());
				bchPayment.setC_Charge_ID(bankcharge.getC_Charge_ID());
				bchPayment.setTenderType(MPayment.TENDERTYPE_Account);
				BigDecimal bchAmt = amt.multiply(bankcharge.getRate().divide(new BigDecimal(100)));
				//	Truncate PayAmt
				bchPayment.setPayAmt(bchAmt.setScale(2, RoundingMode.DOWN));
				//	Set Major Plan Reference
				bchPayment.set_ValueOfColumn("LVE_MajorPlan_ID", getLVE_MajorPlan_ID());
				bchPayment.set_ValueOfColumn("IsChargeBank", bankcharge.isChargeBank());
				bchPayment.saveEx(get_TrxName());
				bchPayment.processIt(MPayment.ACTION_Complete);
				bchPayment.saveEx(get_TrxName());
				info.append(Env.NL+"@C_Payment_ID@: ").append(bchPayment.getDocumentNo());
				String msg = bchPayment.getProcessMsg();
				if (msg != null && msg.length() > 0)
					info.append(" (").append(msg).append(")");
			}	
		}
	}
}
