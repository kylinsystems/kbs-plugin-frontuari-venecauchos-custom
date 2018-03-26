package org.frontuari.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;

public class MLVEMajorPlanType extends X_LVE_MajorPlanType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2412320324069167905L;

	public MLVEMajorPlanType(Properties ctx, int LVE_MajorPlanType_ID, String trxName) {
		super(ctx, LVE_MajorPlanType_ID, trxName);
	}
	
	public MLVEMajorPlanType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**	Major Plan Type Lines			*/
	private MLVEMajorPlanTypeLine[]	m_lines;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MLVEMajorPlanTypeLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "LVE_MajorPlanType_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MLVEMajorPlanTypeLine> list = new Query(getCtx(), I_LVE_MajorPlanTypeLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlanType_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(I_LVE_MajorPlanTypeLine.COLUMNNAME_ValidFrom)
										.list();
		return list.toArray(new MLVEMajorPlanTypeLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get Major Plan Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MLVEMajorPlanTypeLine[] getLines (boolean requery)
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
	public MLVEMajorPlanTypeLine[] getLines()
	{
		return getLines(false);
	}	//	getLines
	
	/**	Major Plan Bank Charges			*/
	private MLVEMajorPlanBankCharge[]	m_bankcharges;

	/**
	 * 	Get Major Plan Lines of Major Plan
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MLVEMajorPlanBankCharge[] getBankCharges (String whereClause)
	{
		String whereClauseFinal = "LVE_MajorPlanType_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MLVEMajorPlanBankCharge> list = new Query(getCtx(), I_LVE_MajorPlanBankCharge.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getLVE_MajorPlanType_ID())
										.setOnlyActiveRecords(true)
										.setOrderBy(I_LVE_MajorPlanBankCharge.COLUMNNAME_Name)
										.list();
		return list.toArray(new MLVEMajorPlanBankCharge[list.size()]);
	}	//	getLines

	/**
	 * 	Get Major Plan Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MLVEMajorPlanBankCharge[] getBankCharges (boolean requery)
	{
		if (m_bankcharges == null || m_bankcharges.length == 0 || requery)
			m_bankcharges = getBankCharges(null);
		set_TrxName(m_bankcharges, get_TrxName());
		return m_bankcharges;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MLVEMajorPlanBankCharge[] getBankCharges()
	{
		return getBankCharges(false);
	}	//	getLines
}
