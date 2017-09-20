/**
 * 
 */
package org.frontuari.callouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author jcolmenarez,20 sept. 2017
 *
 */
public class CallOutOrderFeatures implements IColumnCallout  {
	
	CLogger log = CLogger.getCLogger(CallOutOrderFeatures.class);

	public String start(Properties ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue) {
		
		if(mTab.getTableName().equals(MInvoice.Table_Name) 
				|| mTab.getTableName().equals(MOrder.Table_Name)){
			doctype(ctx,WindowNo,mTab,mField,value,oldValue);
		}
		
		return null;
	}
	
	public String doctype(Properties ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue){
		
		if(Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y"))
		{
			log.info("Set default Document Type from Org");
			
			int C_DocType_ID = 0;
			String DocBaseType = "";
			//	Get Default C_DocType_ID from Org
			String sql = "SELECT C_DocType_ID "
					+ "FROM C_DocType  "
					+ "WHERE IsSoTrx = 'Y' "
					+ "AND AD_Org_ID = ? "		//	1
					+ "AND DocBaseType = ? "	//	2
					+ "ORDER BY IsDefault DESC ";	
			//	Set docbasetype from trx
			if(mTab.getTableName().equals(MInvoice.Table_Name)){
				DocBaseType = "ARI";
			}else{
				DocBaseType = "SOO";
			}
			C_DocType_ID = DB.getSQLValue(null, sql, mTab.getValue("AD_Org_ID"), DocBaseType);
			//	Set C_DocTypeTarget_ID from result
			mTab.setValue("C_DocTypeTarget_ID", C_DocType_ID);
		}
		
		return null; 
	}
}
