package org.frontuari.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MLVEReturnGuarantee extends X_LVE_ReturnGuarantee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1703039730761819777L;

	public MLVEReturnGuarantee(Properties ctx, int LVE_ReturnGuarantee_ID, String trxName) {
		super(ctx, LVE_ReturnGuarantee_ID, trxName);
		if(LVE_ReturnGuarantee_ID == 0) {
			setDateDoc(new Timestamp (System.currentTimeMillis ()));
			setProcessing(false);
			setCreateConfirm("N");
		}
	}
	
	public MLVEReturnGuarantee(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**	Return Guarantee Lines			*/
	private MLVEReturnGuaranteeLine[]	m_lines;

	/**
	 * 	Get Return Guarantee Lines of Return Guarantee
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	public MLVEReturnGuaranteeLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "LVE_ReturnGuarantee_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MLVEReturnGuaranteeLine> list = new Query(getCtx(), I_LVE_ReturnGuaranteeLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_ReturnGuarantee_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(I_LVE_MajorPlanLine.COLUMNNAME_Line)
										.list();
		return list.toArray(new MLVEReturnGuaranteeLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get Return Guarantee Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MLVEReturnGuaranteeLine[] getLines (boolean requery)
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
	public MLVEReturnGuaranteeLine[] getLines()
	{
		return getLines(false);
	}	//	getLines
	
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
			log.warning("AD_Client_ID = 0");
			return false;
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
			StringBuilder sql = new StringBuilder("UPDATE LVE_ReturnGuaranteeLine rgl")
				.append(" SET AD_Org_ID =")
					.append("(SELECT AD_Org_ID")
					.append(" FROM LVE_ReturnGuarantee rg WHERE rgl.LVE_ReturnGuarantee_ID=rg.LVE_ReturnGuarantee_ID) ")
				.append("WHERE LVE_ReturnGuarantee_ID=").append(getLVE_ReturnGuarantee_ID());
			int no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Lines -> #" + no);
		}
		return true;
	}	//	afterSave
	
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
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.ORDER, getLVE_ReturnGuarantee_ID(), get_TrxName());
		if (re == null)
			return null;
		MPrintFormat format = re.getPrintFormat();
		// We have a Jasper Print Format
		// ==============================
		if(format.getJasperProcess_ID() > 0)
		{
			ProcessInfo pi = new ProcessInfo ("", format.getJasperProcess_ID());
			pi.setRecord_ID ( getLVE_ReturnGuarantee_ID() );
			pi.setIsBatch(true);
			
			ServerProcessCtl.process(pi, null);
			
			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return re.getPDF(file);
	}	//	createPDF
}
