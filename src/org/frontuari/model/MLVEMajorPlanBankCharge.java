package org.frontuari.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEMajorPlanBankCharge extends X_LVE_MajorPlanBankCharge {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2068579446302588750L;

	public MLVEMajorPlanBankCharge(Properties ctx, int LVE_MajorPlanBankCharge_ID, String trxName) {
		super(ctx, LVE_MajorPlanBankCharge_ID, trxName);
	}
	
	public MLVEMajorPlanBankCharge(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
}