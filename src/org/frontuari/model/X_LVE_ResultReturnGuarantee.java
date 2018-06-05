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

/** Generated Model for LVE_ResultReturnGuarantee
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_ResultReturnGuarantee extends PO implements I_LVE_ResultReturnGuarantee, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180529L;

    /** Standard Constructor */
    public X_LVE_ResultReturnGuarantee (Properties ctx, int LVE_ResultReturnGuarantee_ID, String trxName)
    {
      super (ctx, LVE_ResultReturnGuarantee_ID, trxName);
      /** if (LVE_ResultReturnGuarantee_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setLVE_ResultReturnGuarantee_ID (0);
			setLVE_ReturnGuarantee_ID (0);
			setResult (null);
// OK
        } */
    }

    /** Load Constructor */
    public X_LVE_ResultReturnGuarantee (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_ResultReturnGuarantee[")
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

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Invoice Document No.
		@param InvoiceNo Invoice Document No	  */
	public void setInvoiceNo (String InvoiceNo)
	{
		set_Value (COLUMNNAME_InvoiceNo, InvoiceNo);
	}

	/** Get Invoice Document No.
		@return Invoice Document No	  */
	public String getInvoiceNo () 
	{
		return (String)get_Value(COLUMNNAME_InvoiceNo);
	}

	/** Set Result Return Guarantee.
		@param LVE_ResultReturnGuarantee_ID Result Return Guarantee	  */
	public void setLVE_ResultReturnGuarantee_ID (int LVE_ResultReturnGuarantee_ID)
	{
		if (LVE_ResultReturnGuarantee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_ResultReturnGuarantee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_ResultReturnGuarantee_ID, Integer.valueOf(LVE_ResultReturnGuarantee_ID));
	}

	/** Get Result Return Guarantee.
		@return Result Return Guarantee	  */
	public int getLVE_ResultReturnGuarantee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_ResultReturnGuarantee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.frontuari.model.I_LVE_ReturnGuarantee getLVE_ReturnGuarantee() throws RuntimeException
    {
		return (org.frontuari.model.I_LVE_ReturnGuarantee)MTable.get(getCtx(), org.frontuari.model.I_LVE_ReturnGuarantee.Table_Name)
			.getPO(getLVE_ReturnGuarantee_ID(), get_TrxName());	}

	/** Set Return Guarantee.
		@param LVE_ReturnGuarantee_ID Return Guarantee	  */
	public void setLVE_ReturnGuarantee_ID (int LVE_ReturnGuarantee_ID)
	{
		if (LVE_ReturnGuarantee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_ReturnGuarantee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_ReturnGuarantee_ID, Integer.valueOf(LVE_ReturnGuarantee_ID));
	}

	/** Get Return Guarantee.
		@return Return Guarantee	  */
	public int getLVE_ReturnGuarantee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_ReturnGuarantee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (BigDecimal Percent)
	{
		set_Value (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Approved = OK */
	public static final String RESULT_Approved = "OK";
	/** Rejected = RJ */
	public static final String RESULT_Rejected = "RJ";
	/** Set Result.
		@param Result 
		Result of the action taken
	  */
	public void setResult (String Result)
	{

		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public String getResult () 
	{
		return (String)get_Value(COLUMNNAME_Result);
	}
}