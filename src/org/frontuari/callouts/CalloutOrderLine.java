package org.frontuari.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.X_C_Order;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class CalloutOrderLine implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(CalloutOrderLine.class);

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue) {
		if(Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y") && 
				((Integer)mTab.getValue("M_Product_ID") != 0 || (Integer)mTab.getValue("M_Product_ID") != null ))
		{
			log.warning("Calculate PriceList");
			//	Get Parameters for search price calculated
			Integer productASI_ID = (Integer)mTab.getValue("M_AttributeSetInstance_ID") != null ? (Integer)mTab.getValue("M_AttributeSetInstance_ID") : 0 ;
			Integer product_ID = (Integer)mTab.getValue("M_Product_ID");
			int priceList_ID;
			Timestamp DateTrx;
			int priceList_Version_ID;
			int currency_ID;
			BigDecimal priceActual = (BigDecimal)mTab.getValue("PriceActual");
			
			//	Get PriceList and Version from Invoice
			if((Integer)mTab.getValue("C_Invoice_ID") != null){
				Integer invoice_ID = (Integer)mTab.getValue("C_Invoice_ID");
				MInvoice inv = MInvoice.get(ctx, invoice_ID);
				currency_ID = inv.getC_Currency_ID();
				priceList_ID = inv.getM_PriceList_ID();
				DateTrx = inv.getDateInvoiced();
				
				String sql = "SELECT plv.M_PriceList_Version_ID "
						+ "FROM M_PriceList_Version plv "
						+ "WHERE plv.M_PriceList_ID=? "						//	1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";
				//	Use newest price list - may not be future

				priceList_Version_ID = DB.getSQLValueEx(null, sql, priceList_ID, DateTrx);
				
			}
			//	Get PriceList and Version from Order
			else{
				Integer order_ID = (Integer)mTab.getValue("C_Order_ID");
				X_C_Order order = new X_C_Order(ctx, order_ID, null);
				currency_ID = order.getC_Currency_ID();
				priceList_ID = order.getM_PriceList_ID();
				DateTrx = order.getDateOrdered();
				
				String sql = "SELECT plv.M_PriceList_Version_ID "
						+ "FROM M_PriceList_Version plv "
						+ "WHERE plv.M_PriceList_ID=? "						//	1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";
				//	Use newest price list - may not be future

				priceList_Version_ID = DB.getSQLValueEx(null, sql, priceList_ID, DateTrx);
			}
			//	Get Price Calculated 
			String sql = "SELECT "
					+ "ROUND(MAX(CASE dsl.List_Base "
					+ "WHEN 'P' THEN ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) - (ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) * (dsl.List_Discount / 100)) "
					+ "WHEN 'L' THEN pp.PriceList - (pp.PriceList - (dsl.List_Discount / 100)) "
					+ "WHEN 'S' THEN pp.PriceStd - (pp.PriceStd - (dsl.List_Discount / 100)) "
					+ "WHEN 'X' THEN pp.PriceLimit - (pp.PriceLimit - (dsl.List_Discount / 100)) "
					+ "WHEN 'F' THEN dsl.List_Fixed END),(SELECT StdPrecision FROM C_Currency WHERE C_Currency_ID = ?)) AS PriceList, "
					+ "ROUND(MAX(CASE dsl.Std_Base "
					+ "WHEN 'P' THEN ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) - (ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) * (dsl.Std_Discount / 100)) "
					+ "WHEN 'L' THEN pp.PriceList - (pp.PriceList - (dsl.Std_Discount / 100)) "
					+ "WHEN 'S' THEN pp.PriceStd - (pp.PriceStd - (dsl.Std_Discount / 100)) "
					+ "WHEN 'X' THEN pp.PriceLimit - (pp.PriceLimit - (dsl.Std_Discount / 100)) "
					+ "WHEN 'F' THEN dsl.Std_Fixed END),(SELECT StdPrecision FROM C_Currency WHERE C_Currency_ID = ?)) AS PriceStd, "
					+ "ROUND(MAX(CASE dsl.Limit_Base "
					+ "WHEN 'P' THEN ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) - (ProductAsiCostPriceAt(pp.M_Product_ID,(CASE WHEN NOW() > plv.ValidFrom THEN NOW() ELSE plv.ValidFrom END)::date,COALESCE(s.M_AttributeSetInstance_ID,0)) * (dsl.Limit_Discount / 100)) "
					+ "WHEN 'L' THEN pp.PriceList - (pp.PriceList - (dsl.Limit_Discount / 100)) "
					+ "WHEN 'S' THEN pp.PriceStd - (pp.PriceStd - (dsl.Limit_Discount / 100)) "
					+ "WHEN 'X' THEN pp.PriceLimit - (pp.PriceLimit - (dsl.Limit_Discount / 100)) "
					+ "WHEN 'F' THEN dsl.Limit_Fixed END),(SELECT StdPrecision FROM C_Currency WHERE C_Currency_ID = ?)) AS PriceLimit "
					+ "FROM M_PriceList pl "
					+ "INNER JOIN M_PriceList_Version plv ON pl.M_PriceList_ID = plv.M_PriceList_ID "
					+ "INNER JOIN M_ProductPrice pp ON plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID "
					+ "INNER JOIN (SELECT p.M_Product_ID,p.Value,p.Name,p.M_Product_Category_ID, "
					+ "(SELECT MAX(t.Rate) FROM C_Tax t WHERE p.C_TaxCategory_ID = t.C_TaxCategory_ID) AS Rate "
					+ "FROM M_Product p) p ON pp.M_Product_ID = p.M_Product_ID "
					+ "LEFT JOIN RV_Storage s ON pp.M_Product_ID = s.M_Product_ID "
					+ "LEFT JOIN M_DiscountSchemaLine dsl ON ((dsl.M_Product_ID IS NULL AND p.M_Product_Category_ID = dsl.M_Product_Category_ID) OR (dsl.M_Product_ID IS NOT NULL AND dsl.M_Product_ID = p.M_Product_ID)) "
					+ "WHERE pl.M_PriceList_ID = ? AND plv.M_PriceList_Version_ID = ? AND pp.M_Product_ID = ? AND COALESCE(s.M_AttributeSetInstance_ID,0) = ? "
					+ "GROUP BY pl.M_PriceList_ID,plv.M_PriceList_Version_ID,pp.M_Product_ID,s.M_AttributeSetInstance_ID ";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, currency_ID);
				pstmt.setInt(2, currency_ID);
				pstmt.setInt(3, currency_ID);
				pstmt.setInt(4, priceList_ID);
				pstmt.setInt(5, priceList_Version_ID);
				pstmt.setInt(6, product_ID);
				pstmt.setInt(7, productASI_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					if(rs.getBigDecimal("PriceStd").compareTo(priceActual) != 0){
						log.warning("Update PriceList because the cost for this product and this ASI is major");
						mTab.setValue("PriceList", rs.getBigDecimal("PriceList"));
						mTab.setValue("PriceLimit", rs.getBigDecimal("PriceLimit"));
						mTab.setValue("PriceActual", rs.getBigDecimal("PriceStd"));
						mTab.setValue("PriceEntered", rs.getBigDecimal("PriceStd"));
					}
				}
			}
			catch (SQLException e){
				return e.getLocalizedMessage();
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

}
