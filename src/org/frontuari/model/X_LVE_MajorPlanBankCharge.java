/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LVE_MajorPlanBankCharge
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_MajorPlanBankCharge extends PO implements I_LVE_MajorPlanBankCharge, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180326L;

    /** Standard Constructor */
    public X_LVE_MajorPlanBankCharge (Properties ctx, int LVE_MajorPlanBankCharge_ID, String trxName)
    {
      super (ctx, LVE_MajorPlanBankCharge_ID, trxName);
      /** if (LVE_MajorPlanBankCharge_ID == 0)
        {
			setC_Charge_ID (0);
			setC_DocType_ID (0);
			setLVE_MajorPlanType_ID (0);
			setName (null);
			setRate (Env.ZERO);
// 0
        } */
    }

    /** Load Constructor */
    public X_LVE_MajorPlanBankCharge (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LVE_MajorPlanBankCharge[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Charge Bank.
		@param IsChargeBank Charge Bank	  */
	public void setIsChargeBank (boolean IsChargeBank)
	{
		set_Value (COLUMNNAME_IsChargeBank, Boolean.valueOf(IsChargeBank));
	}

	/** Get Charge Bank.
		@return Charge Bank	  */
	public boolean isChargeBank () 
	{
		Object oo = get_Value(COLUMNNAME_IsChargeBank);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Major Plan Bank Charge.
		@param LVE_MajorPlanBankCharge_ID Major Plan Bank Charge	  */
	public void setLVE_MajorPlanBankCharge_ID (int LVE_MajorPlanBankCharge_ID)
	{
		if (LVE_MajorPlanBankCharge_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanBankCharge_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanBankCharge_ID, Integer.valueOf(LVE_MajorPlanBankCharge_ID));
	}

	/** Get Major Plan Bank Charge.
		@return Major Plan Bank Charge	  */
	public int getLVE_MajorPlanBankCharge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_MajorPlanBankCharge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_MajorPlanBankCharge_UU.
		@param LVE_MajorPlanBankCharge_UU LVE_MajorPlanBankCharge_UU	  */
	public void setLVE_MajorPlanBankCharge_UU (String LVE_MajorPlanBankCharge_UU)
	{
		set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanBankCharge_UU, LVE_MajorPlanBankCharge_UU);
	}

	/** Get LVE_MajorPlanBankCharge_UU.
		@return LVE_MajorPlanBankCharge_UU	  */
	public String getLVE_MajorPlanBankCharge_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_MajorPlanBankCharge_UU);
	}

	public org.frontuari.model.I_LVE_MajorPlanType getLVE_MajorPlanType() throws RuntimeException
    {
		return (org.frontuari.model.I_LVE_MajorPlanType)MTable.get(getCtx(), org.frontuari.model.I_LVE_MajorPlanType.Table_Name)
			.getPO(getLVE_MajorPlanType_ID(), get_TrxName());	}

	/** Set Major Plan Type.
		@param LVE_MajorPlanType_ID Major Plan Type	  */
	public void setLVE_MajorPlanType_ID (int LVE_MajorPlanType_ID)
	{
		if (LVE_MajorPlanType_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanType_ID, Integer.valueOf(LVE_MajorPlanType_ID));
	}

	/** Get Major Plan Type.
		@return Major Plan Type	  */
	public int getLVE_MajorPlanType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_MajorPlanType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate)
	{
		set_Value (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}