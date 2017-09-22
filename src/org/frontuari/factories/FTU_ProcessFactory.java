/**
 * 
 */
package org.frontuari.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.frontuari.process.VoidFiscalInvoiced;

/**
 * @author jcolmenarez,21 sept. 2017
 *
 */
public class FTU_ProcessFactory implements IProcessFactory {
	
	public ProcessCall newProcessInstance(String className) {
		
		if(className.equals("org.frontuari.process.VoidFiscalInvoiced"))
			return new VoidFiscalInvoiced();
		
		return null;
	}

}
