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

/** Generated Model for LVE_ReturnGuaranteeLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_ReturnGuaranteeLine extends PO implements I_LVE_ReturnGuaranteeLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180529L;

    /** Standard Constructor */
    public X_LVE_ReturnGuaranteeLine (Properties ctx, int LVE_ReturnGuaranteeLine_ID, String trxName)
    {
      super (ctx, LVE_ReturnGuaranteeLine_ID, trxName);
      /** if (LVE_ReturnGuaranteeLine_ID == 0)
        {
			setC_Invoice_ID (0);
			setLine (0);
// @SQL=SELECT MAX(COALESCE(Line,0))+10 FROM LVE_ReturnGuaranteeLine WHERE LVE_ReturnGuaranteeLine.LVE_ReturnGuarantee_ID = @LVE_ReturnGuarantee_ID@
			setLVE_ReturnGuarantee_ID (0);
			setLVE_ReturnGuaranteeLine_ID (0);
			setM_InOutLine_ID (0);
			setModel (null);
			setM_RMAType_ID (0);
			setSerialNo (null);
			setTireMeasure (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_LVE_ReturnGuaranteeLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_ReturnGuaranteeLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Return Guarantee Line.
		@param LVE_ReturnGuaranteeLine_ID Return Guarantee Line	  */
	public void setLVE_ReturnGuaranteeLine_ID (int LVE_ReturnGuaranteeLine_ID)
	{
		if (LVE_ReturnGuaranteeLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_ReturnGuaranteeLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_ReturnGuaranteeLine_ID, Integer.valueOf(LVE_ReturnGuaranteeLine_ID));
	}

	/** Get Return Guarantee Line.
		@return Return Guarantee Line	  */
	public int getLVE_ReturnGuaranteeLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_ReturnGuaranteeLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_InOutLine getM_InOutLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_InOutLine)MTable.get(getCtx(), org.compiere.model.I_M_InOutLine.Table_Name)
			.getPO(getM_InOutLine_ID(), get_TrxName());	}

	/** Set Shipment/Receipt Line.
		@param M_InOutLine_ID 
		Line on Shipment or Receipt document
	  */
	public void setM_InOutLine_ID (int M_InOutLine_ID)
	{
		if (M_InOutLine_ID < 1) 
			set_Value (COLUMNNAME_M_InOutLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_InOutLine_ID, Integer.valueOf(M_InOutLine_ID));
	}

	/** Get Shipment/Receipt Line.
		@return Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOutLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Model.
		@param Model Model	  */
	public void setModel (String Model)
	{
		set_Value (COLUMNNAME_Model, Model);
	}

	/** Get Model.
		@return Model	  */
	public String getModel () 
	{
		return (String)get_Value(COLUMNNAME_Model);
	}

	public org.compiere.model.I_M_RMAType getM_RMAType() throws RuntimeException
    {
		return (org.compiere.model.I_M_RMAType)MTable.get(getCtx(), org.compiere.model.I_M_RMAType.Table_Name)
			.getPO(getM_RMAType_ID(), get_TrxName());	}

	/** Set RMA Type.
		@param M_RMAType_ID 
		Return Material Authorization Type
	  */
	public void setM_RMAType_ID (int M_RMAType_ID)
	{
		if (M_RMAType_ID < 1) 
			set_Value (COLUMNNAME_M_RMAType_ID, null);
		else 
			set_Value (COLUMNNAME_M_RMAType_ID, Integer.valueOf(M_RMAType_ID));
	}

	/** Get RMA Type.
		@return Return Material Authorization Type
	  */
	public int getM_RMAType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_RMAType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Percent Use Analisys.
		@param PercentUseAnalisys Percent Use Analisys	  */
	public void setPercentUseAnalisys (BigDecimal PercentUseAnalisys)
	{
		set_Value (COLUMNNAME_PercentUseAnalisys, PercentUseAnalisys);
	}

	/** Get Percent Use Analisys.
		@return Percent Use Analisys	  */
	public BigDecimal getPercentUseAnalisys () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PercentUseAnalisys);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Front Right = FR */
	public static final String POSTIRE_FrontRight = "FR";
	/** Front Left = FL */
	public static final String POSTIRE_FrontLeft = "FL";
	/** Frist Right Rear = 1RR */
	public static final String POSTIRE_FristRightRear = "1RR";
	/** First Rear Left  = 1RL */
	public static final String POSTIRE_FirstRearLeft = "1RL";
	/** Second Right Rear = 2RR */
	public static final String POSTIRE_SecondRightRear = "2RR";
	/** Third Right Rear = 3RR */
	public static final String POSTIRE_ThirdRightRear = "3RR";
	/** Fourth Right Rear = 4RR */
	public static final String POSTIRE_FourthRightRear = "4RR";
	/** Second Rear Left  = 2RL */
	public static final String POSTIRE_SecondRearLeft = "2RL";
	/** Third Rear Left  = 3RL */
	public static final String POSTIRE_ThirdRearLeft = "3RL";
	/** Fourth Rear Left  = 4RL */
	public static final String POSTIRE_FourthRearLeft = "4RL";
	/** Set Position Tire.
		@param PosTire Position Tire	  */
	public void setPosTire (String PosTire)
	{

		set_Value (COLUMNNAME_PosTire, PosTire);
	}

	/** Get Position Tire.
		@return Position Tire	  */
	public String getPosTire () 
	{
		return (String)get_Value(COLUMNNAME_PosTire);
	}

	/** Set Quantity.
		@param QtyEntered 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		set_Value (COLUMNNAME_QtyEntered, QtyEntered);
	}

	/** Get Quantity.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set mm/32 Res. Analisys.
		@param ResAnalisys mm/32 Res. Analisys	  */
	public void setResAnalisys (String ResAnalisys)
	{
		set_Value (COLUMNNAME_ResAnalisys, ResAnalisys);
	}

	/** Get mm/32 Res. Analisys.
		@return mm/32 Res. Analisys	  */
	public String getResAnalisys () 
	{
		return (String)get_Value(COLUMNNAME_ResAnalisys);
	}

	/** Set Serial Number.
		@param SerialNo Serial Number	  */
	public void setSerialNo (String SerialNo)
	{
		set_Value (COLUMNNAME_SerialNo, SerialNo);
	}

	/** Get Serial Number.
		@return Serial Number	  */
	public String getSerialNo () 
	{
		return (String)get_Value(COLUMNNAME_SerialNo);
	}

	/** Set Tire Measure.
		@param TireMeasure Tire Measure	  */
	public void setTireMeasure (String TireMeasure)
	{
		set_Value (COLUMNNAME_TireMeasure, TireMeasure);
	}

	/** Get Tire Measure.
		@return Tire Measure	  */
	public String getTireMeasure () 
	{
		return (String)get_Value(COLUMNNAME_TireMeasure);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}