package org.frontuari.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEMajorPlanTypeLine extends X_LVE_MajorPlanTypeLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 223867206932601028L;

	public MLVEMajorPlanTypeLine(Properties ctx, int LVE_MajorPlanTypeLine_ID, String trxName) {
		super(ctx, LVE_MajorPlanTypeLine_ID, trxName);
	}
	
	public MLVEMajorPlanTypeLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
}
