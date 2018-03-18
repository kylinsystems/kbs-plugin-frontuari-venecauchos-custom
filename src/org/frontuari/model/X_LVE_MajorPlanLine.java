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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LVE_MajorPlanLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_MajorPlanLine extends PO implements I_LVE_MajorPlanLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180317L;

    /** Standard Constructor */
    public X_LVE_MajorPlanLine (Properties ctx, int LVE_MajorPlanLine_ID, String trxName)
    {
      super (ctx, LVE_MajorPlanLine_ID, trxName);
      /** if (LVE_MajorPlanLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_LVE_MajorPlanLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_MajorPlanLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Due Date.
		@param DueDate 
		Date when the payment is due
	  */
	public void setDueDate (Timestamp DueDate)
	{
		set_Value (COLUMNNAME_DueDate, DueDate);
	}

	/** Get Due Date.
		@return Date when the payment is due
	  */
	public Timestamp getDueDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DueDate);
	}

	/** Set Interest Amount.
		@param InterestAmt 
		Interest Amount
	  */
	public void setInterestAmt (BigDecimal InterestAmt)
	{
		set_Value (COLUMNNAME_InterestAmt, InterestAmt);
	}

	/** Get Interest Amount.
		@return Interest Amount
	  */
	public BigDecimal getInterestAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_InterestAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Paid.
		@param IsPaid 
		The document is paid
	  */
	public void setIsPaid (boolean IsPaid)
	{
		set_Value (COLUMNNAME_IsPaid, Boolean.valueOf(IsPaid));
	}

	/** Get Paid.
		@return The document is paid
	  */
	public boolean isPaid () 
	{
		Object oo = get_Value(COLUMNNAME_IsPaid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line Amount.
		@param LineNetAmt 
		Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt)
	{
		set_ValueNoCheck (COLUMNNAME_LineNetAmt, LineNetAmt);
	}

	/** Get Line Amount.
		@return Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineNetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.frontuari.model.I_LVE_MajorPlan getLVE_MajorPlan() throws RuntimeException
    {
		return (org.frontuari.model.I_LVE_MajorPlan)MTable.get(getCtx(), org.frontuari.model.I_LVE_MajorPlan.Table_Name)
			.getPO(getLVE_MajorPlan_ID(), get_TrxName());	}

	/** Set Major Plan.
		@param LVE_MajorPlan_ID Major Plan	  */
	public void setLVE_MajorPlan_ID (int LVE_MajorPlan_ID)
	{
		if (LVE_MajorPlan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlan_ID, Integer.valueOf(LVE_MajorPlan_ID));
	}

	/** Get Major Plan.
		@return Major Plan	  */
	public int getLVE_MajorPlan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_MajorPlan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_MajorPlanLine_ID.
		@param LVE_MajorPlanLine_ID LVE_MajorPlanLine_ID	  */
	public void setLVE_MajorPlanLine_ID (int LVE_MajorPlanLine_ID)
	{
		if (LVE_MajorPlanLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanLine_ID, Integer.valueOf(LVE_MajorPlanLine_ID));
	}

	/** Get LVE_MajorPlanLine_ID.
		@return LVE_MajorPlanLine_ID	  */
	public int getLVE_MajorPlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_MajorPlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_MajorPlanLine_UU.
		@param LVE_MajorPlanLine_UU LVE_MajorPlanLine_UU	  */
	public void setLVE_MajorPlanLine_UU (String LVE_MajorPlanLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_LVE_MajorPlanLine_UU, LVE_MajorPlanLine_UU);
	}

	/** Get LVE_MajorPlanLine_UU.
		@return LVE_MajorPlanLine_UU	  */
	public String getLVE_MajorPlanLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_MajorPlanLine_UU);
	}

	/** Set Payment date.
		@param PayDate 
		Date Payment made
	  */
	public void setPayDate (Timestamp PayDate)
	{
		set_Value (COLUMNNAME_PayDate, PayDate);
	}

	/** Get Payment date.
		@return Date Payment made
	  */
	public Timestamp getPayDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_PayDate);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}