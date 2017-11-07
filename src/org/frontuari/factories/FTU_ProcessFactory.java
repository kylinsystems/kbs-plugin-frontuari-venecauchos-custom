/**
 * 
 */
package org.frontuari.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.frontuari.process.Aging;
import org.frontuari.process.CommissionCalc;
import org.frontuari.process.VoidFiscalInvoiced;

/**
 * @author jcolmenarez,21 sept. 2017
 *
 */
public class FTU_ProcessFactory implements IProcessFactory {
	
	public ProcessCall newProcessInstance(String className) {
		
		if(className.equals("org.frontuari.process.VoidFiscalInvoiced"))
			return new VoidFiscalInvoiced();
		
		if(className.equals("org.frontuari.process.Aging"))
			return new Aging();
		
		if(className.equals("org.frontuari.process.CommissionCalc"))
			return new CommissionCalc();
		
		return null;
	}

}
