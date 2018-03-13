package org.frontuari.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;

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
			setProcessing(false);
		}
	}

	public MLVEMajorPlan(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**	Major Plan Lines			*/
	private MLVEMajorPlanLine[]	m_lines;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MLVEMajorPlanLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "LVE_MajorPlan_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MLVEMajorPlanLine> list = new Query(getCtx(), I_LVE_MajorPlanLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlan_ID())
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
		else{
			//	Calculate LineNetAmt and InterestAmt
			for (int i = 0; i < lines.length; i++)
			{
				MLVEMajorPlanLine line = lines[i];
				MLVEMajorPlanType mpt = new MLVEMajorPlanType(getCtx(), line.getLVE_MajorPlan().getLVE_MajorPlanType_ID(), get_TrxName());
				BigDecimal interestAmt = line.getAmount().multiply(mpt.getRate().divide(new BigDecimal(100)));
				BigDecimal lineNetAmt = line.getAmount().add(interestAmt);
				line.setInterestAmt(interestAmt);
				line.setLineNetAmt(lineNetAmt);
				line.saveEx();
			}	//	for all lines
		}
		
		//	Check that not exceeded approval amt 
		if(getAmount().compareTo(getAmtApproval()) > 0){
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

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
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
				line.setInterestAmt(Env.ZERO);
				line.setLineNetAmt(Env.ZERO);
				line.setIsPaid(false);
				line.setPayDate(null);

				//	Verified if have payment
				if(line.getC_Payment_ID() != 0){
					MPayment pay = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
					if(pay.getDocStatus().equals(MPayment.DOCSTATUS_Completed)){
						if(pay.reverseCorrectIt()){
							pay.addDescription("@Voided@ @from@ @LVE_MajorPlan_ID@: "+getDocumentNo());
							pay.saveEx();
						}
					}
					line.setC_Payment_ID(0);
				}
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
					pay.addDescription("@Voided@ @from@ @LVE_MajorPlan_ID@: "+getDocumentNo());
					pay.saveEx();
				}
			}
			
			//	Set lines to 0
			MLVEMajorPlanLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MLVEMajorPlanLine line = lines[i];
				line.setAmount(Env.ZERO);
				line.setInterestAmt(Env.ZERO);
				line.setLineNetAmt(Env.ZERO);
				line.setIsPaid(false);
				line.setPayDate(null);

				//	Verified if have payment
				if(line.getC_Payment_ID() != 0){
					MPayment pay = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
					if(pay.getDocStatus().equals(MPayment.DOCSTATUS_Completed)){
						if(pay.reverseCorrectIt()){
							pay.addDescription("@Voided@ @from@ @LVE_MajorPlan_ID@: "+getDocumentNo());
							pay.saveEx();
						}
					}
					line.setC_Payment_ID(0);
				}
				line.saveEx(get_TrxName());
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
				pay.addDescription("@Voided@ @from@ @LVE_MajorPlan_ID@: "+getDocumentNo());
				pay.saveEx();
			}
		}
		
		//	Set lines to 0
		MLVEMajorPlanLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MLVEMajorPlanLine line = lines[i];
			line.setIsPaid(false);
			line.setPayDate(null);

			//	Verified if have payment
			if(line.getC_Payment_ID() != 0){
				MPayment pay = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
				if(pay.getDocStatus().equals(MPayment.DOCSTATUS_Completed)){
					if(pay.reverseCorrectIt()){
						pay.addDescription("@Voided@ @from@ @LVE_MajorPlan_ID@: "+getDocumentNo());
						pay.saveEx();
					}
				}
				line.setC_Payment_ID(0);
			}
			line.saveEx(get_TrxName());
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
		return null;
	}

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
			options[index++] = DocumentEngine.ACTION_Complete;
			options[index++] = DocumentEngine.ACTION_Prepare;
			options[index++] = DocumentEngine.ACTION_Void;

			// If the document is already completed, we also want to be able to reactivate or void it instead of only closing it
		} else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
			options[index++] = DocumentEngine.ACTION_Void;
			options[index++] = DocumentEngine.ACTION_ReActivate;
		}

		return index;
	}

}
