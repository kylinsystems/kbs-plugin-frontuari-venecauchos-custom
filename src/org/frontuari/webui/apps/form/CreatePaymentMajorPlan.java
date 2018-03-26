/******************************************************************************
 * Copyright (C) 2018 Jorge Colmenarez                                        *
 * Copyright (C) 2018 Frontuari, C.A.										  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.     *
 *****************************************************************************/
package org.frontuari.webui.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *  Create Payment from Major Plan
 *
 *  @author Jorge Colmenarez,jcolmenarez@frontuari.com, http://www.frontuari.com 
 *  @version Id: CreatePaymentMajorPlan.java,v 1.0 2018-03-25 09:11:36 jcolmenarez Exp
 */
public class CreatePaymentMajorPlan
{
	/**	Window No			*/
	public int         m_WindowNo = 0;

	/** MajorPlan     */
	public int      m_LVE_MajorPlan_ID = 0;
	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(CreatePaymentMajorPlan.class);
	
	public void dynInit() throws Exception
	{
	}

	/**
	 *  Query Info
	 */
	public Vector<Vector<Object>> getData()
	{
		log.info("");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		//  Create SQL
		StringBuffer sql = new StringBuffer("SELECT mpl.C_Invoice_ID,i.DocumentNo,COALESCE(tit.Name,'')||bp.TaxID||' '||bp.Name AS BPartner, ")	// 1..3
				.append(DB.TO_CHAR("mp.DateDoc", DisplayType.Date, Env.getAD_Language(Env.getCtx()))+",")	//	4
				.append(DB.TO_CHAR("mpl.DueDate", DisplayType.Date, Env.getAD_Language(Env.getCtx())))	//	5
				.append(",mpl.Amount,ROUND((mpl.Amount * ((mptl.InterestPercent / 100) / EXTRACT(DAY FROM (Now() - mp.DateDoc))::numeric)),2) AS InterestTranscurred, ") // 6..7
				.append("ROUND((mpl.Amount + (mpl.Amount * ((mptl.InterestPercent / 100) / EXTRACT(DAY FROM (Now() - mp.DateDoc))::numeric))),2) AS LineNetAmt ") // 8
				.append("FROM LVE_MajorPlanLine mpl ")
				.append("INNER JOIN LVE_MajorPlan mp ON (mpl.LVE_MajorPlan_ID = mp.LVE_MajorPlan_ID) ")
				.append("INNER JOIN C_Invoice i ON (mpl.C_Invoice_ID = i.C_Invoice_ID) ")
				.append("INNER JOIN LVE_MajorPlanTypeLine mptl ON (mp.LVE_MajorPlanType_ID = mptl.LVE_MajorPlanType_ID AND i.C_BPartner_ID = mptl.C_BPartner_ID) ")
				.append("INNER JOIN C_BPartner bp ON (i.C_BPartner_ID = bp.C_BPartner_ID) ")
				.append("LEFT JOIN LCO_TaxIdType tit ON (bp.LCO_TaxIdType_ID = tit.LCO_TaxIdType_ID) ")
				.append("WHERE mp.LVE_MajorPlan_ID = ? AND mpl.IsPaid = 'N'");
		//  Execute
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_LVE_MajorPlan_ID);
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));           //  0-Selection
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2)); 
				line.add(pp);					// 1-Invoice
				line.add(rs.getString(3));		// 2-BPartner
				line.add(rs.getString(4));		// 3-DateDoc
				line.add(rs.getString(5));		// 4-DueDate
				line.add(rs.getBigDecimal(6));	// 5-Amount
				line.add(rs.getBigDecimal(7));	// 6-InterestAmt
				//line.add(rs.getBigDecimal(8));	// 7-TotalLines
				//
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs,pstmt);
			rs = null;
			pstmt = null;
		}
		return data;
	}   //  getData

	public Vector<String> getColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "DateDoc"));
		columnNames.add(Msg.translate(Env.getCtx(), "DueDate"));
		columnNames.add(Msg.translate(Env.getCtx(), "Amt"));
		columnNames.add(Msg.translate(Env.getCtx(), "InterestAmt"));
		//columnNames.add(Msg.translate(Env.getCtx(), "TotalLines"));
		
		return columnNames;
	}
	
	public void setColumnClass(IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, String.class, true);		//	1-Invoice
		miniTable.setColumnClass(2, String.class, true);		//	2-BPartner
		miniTable.setColumnClass(3, String.class, true);		//	3-DateDoc
		miniTable.setColumnClass(4, String.class, true);		//	4-DueDate
		miniTable.setColumnClass(5, BigDecimal.class, false);	//	5-Amount
		miniTable.setColumnClass(6, BigDecimal.class, false);	//	6-InterestAmt
		//miniTable.setColumnClass(7, BigDecimal.class, true);	//	7-TotalLines
		//  Table UI
		miniTable.autoSize();
	}	
}   //  CreatePaymentMajorPlan