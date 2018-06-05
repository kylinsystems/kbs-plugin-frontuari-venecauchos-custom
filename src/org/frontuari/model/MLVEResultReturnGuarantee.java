package org.frontuari.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEResultReturnGuarantee extends X_LVE_ResultReturnGuarantee {

	public MLVEResultReturnGuarantee(Properties ctx, int LVE_ResultReturnGuarantee_ID, String trxName) {
		super(ctx, LVE_ResultReturnGuarantee_ID, trxName);
	}

	public MLVEResultReturnGuarantee(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3137105368654597458L;

}
