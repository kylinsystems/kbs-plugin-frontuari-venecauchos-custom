package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Msg;

public class MLVEReturnGuaranteeLine extends X_LVE_ReturnGuaranteeLine {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6256227138098987906L;

	public MLVEReturnGuaranteeLine(Properties ctx, int LVE_ReturnGuaranteeLine_ID, String trxName) {
		super(ctx, LVE_ReturnGuaranteeLine_ID, trxName);
	}
	
	public MLVEReturnGuaranteeLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/** Parent						*/
	private MLVEReturnGuarantee	m_parent = null;

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MLVEReturnGuarantee getParent()
	{
		if (m_parent == null)
			m_parent = new MLVEReturnGuarantee(getCtx(), getLVE_ReturnGuarantee_ID(), get_TrxName());
		return m_parent;
	}	//	getParent

	/**
	 * 	Parent Constructor
	 * 	@param returnguarantee parent
	 */
	public MLVEReturnGuaranteeLine (MLVEReturnGuarantee returnguarantee)
	{
		this (returnguarantee.getCtx(), 0, returnguarantee.get_TrxName());
		if (returnguarantee.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(returnguarantee.getAD_Client_ID(), returnguarantee.getAD_Org_ID());
		setLVE_ReturnGuarantee_ID(returnguarantee.getLVE_ReturnGuarantee_ID());
	}	//	MReturnGuaranteeLine
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (log.isLoggable(Level.FINE)) log.fine("New=" + newRecord);

		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM LVE_MajorPlanLine WHERE LVE_MajorPlan_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getLVE_ReturnGuarantee_ID());
			setLine (ii);
		}
		
		if(getQtyEntered().compareTo(BigDecimal.ZERO) == 0) {
			log.saveError("qty.returned.not.zero", Msg.translate(getCtx(), "LVE_ReturnGuaranteeLine"));
			return false;
		}
		
		//	Get All Qty Returned
		String sql = "SELECT SUM(QtyEntered) FROM LVE_ReturnGuaranteeLine WHERE M_InOutLine_ID = ? AND LVE_ReturnGuaranteeLine_ID <> ?";
		BigDecimal qty = DB.getSQLValueBD(get_TrxName(), sql, new Object[]{(Integer)getM_InOutLine_ID(),getLVE_ReturnGuaranteeLine_ID()});
		if(qty == null)
			qty = BigDecimal.ZERO;
		
		//	Check that not exceeded
		if(getM_InOutLine().getMovementQty().compareTo(getQtyEntered().add(qty)) <= 0) {
			log.saveError("qty.returned.not.better", Msg.translate(getCtx(), "LVE_ReturnGuaranteeLine"));
			return false;
		}
		
		return true;
	}	//	beforeSave
}
