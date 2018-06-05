package org.frontuari.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MLocator;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.frontuari.model.MLVEReturnGuarantee;
import org.frontuari.model.MLVEReturnGuaranteeLine;

public class CreateOutputReclaim extends SvrProcess {
	
	private int p_M_Locator_ID = 0;
	private int p_C_DocType_ID = 0;
	private MLVEReturnGuarantee m_rg;

	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		if (log.isLoggable(Level.INFO)) log.info("LVE_ReturnGuarantee_ID=" + getRecord_ID() + ", M_Locator_ID=" + p_M_Locator_ID);
		
		m_rg = new MLVEReturnGuarantee(getCtx(), getRecord_ID(), get_TrxName());
		if (m_rg.get_ID() == 0)
			throw new AdempiereUserError ("No Return Guarantee");
		//	Get Locator
		MLocator loc = new MLocator(getCtx(), p_M_Locator_ID, get_TrxName());
		//	Create Input
		MInventory inv = new MInventory(getCtx(), 0, get_TrxName());
		inv.setAD_Org_ID(m_rg.getAD_Org_ID());
		inv.setM_Warehouse_ID(loc.getM_Warehouse_ID());
		inv.setMovementDate(m_rg.getDateDoc());
		inv.setC_DocType_ID(p_C_DocType_ID);
		inv.setDescription("SALIDA POR BX NRO: "+m_rg.getDocumentNo()+" ENVIADO CON : "+m_rg.getDriverName()+" CI: "+m_rg.getDriverTaxID());
		inv.saveEx(get_TrxName());
		//	Get Lines
		MLVEReturnGuaranteeLine[] lines = m_rg.getLines();
		for(MLVEReturnGuaranteeLine line: lines) {
			BigDecimal qtyBook = setQtyBook(line.getM_InOutLine().getM_AttributeSetInstance_ID(), line.getM_InOutLine().getM_Product_ID(), p_M_Locator_ID);
			MInventoryLine il = new MInventoryLine(inv, p_M_Locator_ID, 
					line.getM_InOutLine().getM_Product_ID(), 
					line.getM_InOutLine().getM_AttributeSetInstance_ID(), 
					qtyBook,qtyBook.subtract(line.getQtyEntered()));
			il.setInventoryType(MInventoryLine.INVENTORYTYPE_InventoryDifference);
			il.saveEx(get_TrxName());
		}
		//	Complete Inventory
		if(inv.processIt(MInventory.ACTION_Complete)) {
			inv.saveEx(get_TrxName());
			m_rg.setRef_Inventory_ID(inv.getM_Inventory_ID());
			m_rg.saveEx(get_TrxName());
		}else {
			throw new AdempiereException(inv.getProcessMsg());
		}
		
		return "OK";
	}

	/**
	 * 
	 * Returns the current Book Qty for given parameters or 0
	 * 
	 * @param M_AttributeSetInstance_ID
	 * @param M_Product_ID
	 * @param M_Locator_ID
	 * @return
	 * @throws Exception
	 */
	private BigDecimal setQtyBook (int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID) throws Exception {
		// Set QtyBook from first storage location
		BigDecimal bd = null;
		String sql = "SELECT SUM(QtyOnHand) FROM M_StorageOnHand "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?"		//	2
			+ " AND M_AttributeSetInstance_ID=?"; //3
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				bd = rs.getBigDecimal(1);
				if (bd != null)
					return bd;
			} else {
				return Env.ZERO;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			throw new Exception(e.getLocalizedMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return Env.ZERO;
	}	
}