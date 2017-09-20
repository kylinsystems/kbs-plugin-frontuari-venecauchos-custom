/**
 * 
 */
package org.frontuari.factories;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.frontuari.callouts.CallOutOrderFeatures;
import org.frontuari.callouts.CalloutOrderLine;

/**
 * @author jcolmenarez,20 sept. 2017
 *
 */
public class FTUCallOutsFactory implements IColumnCalloutFactory {
	
	List<IColumnCallout> list = new ArrayList<IColumnCallout>();
	
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {
		
		if(tableName.equalsIgnoreCase(MOrder.Table_Name) 
				&& columnName.equalsIgnoreCase(MOrder.COLUMNNAME_AD_Org_ID))
			list.add(new CallOutOrderFeatures());
		
		if(tableName.equalsIgnoreCase(MInvoice.Table_Name) 
				&& columnName.equalsIgnoreCase(MInvoice.COLUMNNAME_AD_Org_ID))
			list.add(new CallOutOrderFeatures());
		
		if(tableName.equalsIgnoreCase(MOrderLine.Table_Name) 
				&& columnName.equalsIgnoreCase(MOrderLine.COLUMNNAME_M_AttributeSetInstance_ID))
			list.add(new CalloutOrderLine());
		
		if(tableName.equalsIgnoreCase(MInvoiceLine.Table_Name) 
				&& columnName.equalsIgnoreCase(MInvoiceLine.COLUMNNAME_M_AttributeSetInstance_ID))
			list.add(new CalloutOrderLine());
		
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}
}
