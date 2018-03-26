package org.frontuari.factories;

import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;
import org.compiere.model.GridTab;
import org.frontuari.model.MLVEMajorPlan;
import org.frontuari.webui.apps.form.WCreateFromMajorPlanUI;

public class FTUCreateFromFactory implements ICreateFromFactory {

	@Override
	public ICreateFrom create(GridTab mTab) {
		String TableName = mTab.getTableName();
		
		if(TableName.equalsIgnoreCase(MLVEMajorPlan.Table_Name)){
			return new WCreateFromMajorPlanUI(mTab);
		}
		
		return null;
	}

}
