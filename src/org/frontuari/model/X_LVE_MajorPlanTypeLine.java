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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LVE_MajorPlanTypeLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_MajorPlanTypeLine extends PO implements I_LVE_MajorPlanTypeLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180312L;

    /** Standard Constructor */
    public X_LVE_MajorPlanTypeLine (Properties ctx, int LVE_MajorPlanTypeLine_ID, String trxName)
    {
      super (ctx, LVE_MajorPlanTypeLine_ID, trxName);
      /** if (LVE_MajorPlanTypeLine_ID == 0)
        {
			setC_BPartner_ID (0);
			setLVE_MajorPlanType_ID (0);
			setLVE_MajorPlanTypeLine_ID (0);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_LVE_MajorPlanTypeLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_MajorPlanTypeLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Major Plan Type Line.
		@param LVE_MajorPlanTypeLine_ID Major Plan Type Line	  */
	public void setLVE_MajorPlanTypeLine_ID (int LVE_MajorPlanTypeLine_ID)
	{
		if (LVE_MajorPlanTypeLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanTypeLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanTypeLine_ID, Integer.valueOf(LVE_MajorPlanTypeLine_ID));
	}

	/** Get Major Plan Type Line.
		@return Major Plan Type Line	  */
	public int getLVE_MajorPlanTypeLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_MajorPlanTypeLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_MajorPlanTypeLine_UU.
		@param LVE_MajorPlanTypeLine_UU LVE_MajorPlanTypeLine_UU	  */
	public void setLVE_MajorPlanTypeLine_UU (String LVE_MajorPlanTypeLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanTypeLine_UU, LVE_MajorPlanTypeLine_UU);
	}

	/** Get LVE_MajorPlanTypeLine_UU.
		@return LVE_MajorPlanTypeLine_UU	  */
	public String getLVE_MajorPlanTypeLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_MajorPlanTypeLine_UU);
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}
}