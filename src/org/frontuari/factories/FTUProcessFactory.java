/**
 * 
 */
package org.frontuari.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.frontuari.process.Aging;
import org.frontuari.process.CommissionCalc;
import org.frontuari.process.CreateDocFromReclaim;
import org.frontuari.process.CreateInputReclaim;
import org.frontuari.process.CreateOutputReclaim;
import org.frontuari.process.ImportInventory;
import org.frontuari.process.VoidFiscalInvoiced;

/**
 * @author jcolmenarez,21 sept. 2017
 *
 */
public class FTUProcessFactory implements IProcessFactory {
	
	public ProcessCall newProcessInstance(String className) {
		
		if(className.equals("org.frontuari.process.VoidFiscalInvoiced"))
			return new VoidFiscalInvoiced();
		
		if(className.equals("org.frontuari.process.Aging"))
			return new Aging();
		
		if(className.equals("org.frontuari.process.CommissionCalc"))
			return new CommissionCalc();
		
		if(className.equals("org.frontuari.process.ImportInventory"))
			return new ImportInventory();
		
		if(className.equals("org.frontuari.process.CreateInputReclaim"))
			return new CreateInputReclaim();
		
		if(className.equals("org.frontuari.process.CreateOutputReclaim"))
			return new CreateOutputReclaim();
		
		if(className.equals("org.frontuari.process.CreateDocFromReclaim"))
			return new CreateDocFromReclaim();
		
		return null;
	}

}
