/**
 * 
 */
package org.frontuari.events;

import java.math.BigDecimal;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

/**
 * Event Handler to Validate Payment from POS Payment
 * @autor Jorge Colmenarez, 20 sept. 2017, jcolmenarez@frontuari.com, Frontuari, C.A.
 *
 */
public class FTUEventsHandler extends AbstractEventHandler {
	
	CLogger log = CLogger.getCLogger(FTUEventsHandler.class);
	
	protected void initialize() {
		
		registerTableEvent(IEventTopics.DOC_AFTER_PREPARE, MOrder.Table_Name);
	}

	protected void doHandleEvent(Event event) {
		PO po = getPO(event);
		String type = event.getTopic();
		log.info(po.get_TableName() + " Type: " + type);
		if(po instanceof MOrder){
			if(type.equalsIgnoreCase(IEventTopics.DOC_AFTER_PREPARE)){
				MOrder order = (MOrder)po;
				if(order.getPaymentRule().equals(order.PAYMENTRULE_MixedPOSPayment)){
					BigDecimal PaidAmt = Env.ZERO;
					String sql = "SELECT SUM(PayAmt) AS PaidAmt "
							+ "FROM C_POSPayment "
							+ "WHERE C_Order_ID = ? ";
					PaidAmt = DB.getSQLValueBD(null, sql, order.getC_Order_ID());
					if(PaidAmt != null){
						//	Validate PaidAmt from POSPayment
						if(order.getGrandTotal().subtract(PaidAmt).compareTo(Env.ZERO) != 0){
							throw new AdempiereException("@POSPaymentDiffers@"
									+ " @GrandTotal@: "+order.getGrandTotal()
									+ " @PaidAmt@: "+PaidAmt
									+ " @amount.difference@: "+order.getGrandTotal().subtract(PaidAmt));
						}
					}
				}
			}
		}
	}

}
