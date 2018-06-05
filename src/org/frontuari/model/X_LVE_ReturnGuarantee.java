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

/** Generated Model for LVE_ReturnGuarantee
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_ReturnGuarantee extends PO implements I_LVE_ReturnGuarantee, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180529L;

    /** Standard Constructor */
    public X_LVE_ReturnGuarantee (Properties ctx, int LVE_ReturnGuarantee_ID, String trxName)
    {
      super (ctx, LVE_ReturnGuarantee_ID, trxName);
      /** if (LVE_ReturnGuarantee_ID == 0)
        {
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocumentNo (null);
			setLVE_ReturnGuarantee_ID (0);
			setLVE_Vendor_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LVE_ReturnGuarantee (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_ReturnGuarantee[")
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

	/** Set Create Confirm.
		@param CreateConfirm Create Confirm	  */
	public void setCreateConfirm (String CreateConfirm)
	{
		set_Value (COLUMNNAME_CreateConfirm, CreateConfirm);
	}

	/** Get Create Confirm.
		@return Create Confirm	  */
	public String getCreateConfirm () 
	{
		return (String)get_Value(COLUMNNAME_CreateConfirm);
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
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

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
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

	/** Set Driver Name.
		@param DriverName Driver Name	  */
	public void setDriverName (String DriverName)
	{
		set_Value (COLUMNNAME_DriverName, DriverName);
	}

	/** Get Driver Name.
		@return Driver Name	  */
	public String getDriverName () 
	{
		return (String)get_Value(COLUMNNAME_DriverName);
	}

	/** Set Driver Tax ID.
		@param DriverTaxID Driver Tax ID	  */
	public void setDriverTaxID (String DriverTaxID)
	{
		set_Value (COLUMNNAME_DriverTaxID, DriverTaxID);
	}

	/** Get Driver Tax ID.
		@return Driver Tax ID	  */
	public String getDriverTaxID () 
	{
		return (String)get_Value(COLUMNNAME_DriverTaxID);
	}

	/** Set Driver Vehicle Plate.
		@param DriverVehiclePlate Driver Vehicle Plate	  */
	public void setDriverVehiclePlate (String DriverVehiclePlate)
	{
		set_Value (COLUMNNAME_DriverVehiclePlate, DriverVehiclePlate);
	}

	/** Get Driver Vehicle Plate.
		@return Driver Vehicle Plate	  */
	public String getDriverVehiclePlate () 
	{
		return (String)get_Value(COLUMNNAME_DriverVehiclePlate);
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_Value (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

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

	public org.compiere.model.I_C_BPartner getLVE_Vendor() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getLVE_Vendor_ID(), get_TrxName());	}

	/** Set Vendor.
		@param LVE_Vendor_ID Vendor	  */
	public void setLVE_Vendor_ID (int LVE_Vendor_ID)
	{
		if (LVE_Vendor_ID < 1) 
			set_Value (COLUMNNAME_LVE_Vendor_ID, null);
		else 
			set_Value (COLUMNNAME_LVE_Vendor_ID, Integer.valueOf(LVE_Vendor_ID));
	}

	/** Get Vendor.
		@return Vendor	  */
	public int getLVE_Vendor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_Vendor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Mileage Car.
		@param MileageCar Mileage Car	  */
	public void setMileageCar (BigDecimal MileageCar)
	{
		set_Value (COLUMNNAME_MileageCar, MileageCar);
	}

	/** Get Mileage Car.
		@return Mileage Car	  */
	public BigDecimal getMileageCar () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MileageCar);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Mileage Tire.
		@param MileageTire Mileage Tire	  */
	public void setMileageTire (BigDecimal MileageTire)
	{
		set_Value (COLUMNNAME_MileageTire, MileageTire);
	}

	/** Get Mileage Tire.
		@return Mileage Tire	  */
	public BigDecimal getMileageTire () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MileageTire);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_M_Inventory getM_Inventory() throws RuntimeException
    {
		return (org.compiere.model.I_M_Inventory)MTable.get(getCtx(), org.compiere.model.I_M_Inventory.Table_Name)
			.getPO(getM_Inventory_ID(), get_TrxName());	}

	/** Set Phys.Inventory.
		@param M_Inventory_ID 
		Parameters for a Physical Inventory
	  */
	public void setM_Inventory_ID (int M_Inventory_ID)
	{
		if (M_Inventory_ID < 1) 
			set_Value (COLUMNNAME_M_Inventory_ID, null);
		else 
			set_Value (COLUMNNAME_M_Inventory_ID, Integer.valueOf(M_Inventory_ID));
	}

	/** Get Phys.Inventory.
		@return Parameters for a Physical Inventory
	  */
	public int getM_Inventory_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Inventory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.compiere.model.I_M_Inventory getRef_Inventory() throws RuntimeException
    {
		return (org.compiere.model.I_M_Inventory)MTable.get(getCtx(), org.compiere.model.I_M_Inventory.Table_Name)
			.getPO(getRef_Inventory_ID(), get_TrxName());	}

	/** Set Referenced Inventory.
		@param Ref_Inventory_ID Referenced Inventory	  */
	public void setRef_Inventory_ID (int Ref_Inventory_ID)
	{
		if (Ref_Inventory_ID < 1) 
			set_Value (COLUMNNAME_Ref_Inventory_ID, null);
		else 
			set_Value (COLUMNNAME_Ref_Inventory_ID, Integer.valueOf(Ref_Inventory_ID));
	}

	/** Get Referenced Inventory.
		@return Referenced Inventory	  */
	public int getRef_Inventory_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ref_Inventory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Long Distance = LD */
	public static final String USEOFTIRE_LongDistance = "LD";
	/** Regional = RG */
	public static final String USEOFTIRE_Regional = "RG";
	/** Urban-City = UC */
	public static final String USEOFTIRE_Urban_City = "UC";
	/** Mixed Service = MS */
	public static final String USEOFTIRE_MixedService = "MS";
	/** Off Road = OR */
	public static final String USEOFTIRE_OffRoad = "OR";
	/** Set Use of Tire.
		@param UseofTire Use of Tire	  */
	public void setUseofTire (String UseofTire)
	{

		set_Value (COLUMNNAME_UseofTire, UseofTire);
	}

	/** Get Use of Tire.
		@return Use of Tire	  */
	public String getUseofTire () 
	{
		return (String)get_Value(COLUMNNAME_UseofTire);
	}

	/** Set Vehicle Guarantee E.O..
		@param VehicleGuaranteeEO Vehicle Guarantee E.O.	  */
	public void setVehicleGuaranteeEO (String VehicleGuaranteeEO)
	{
		set_Value (COLUMNNAME_VehicleGuaranteeEO, VehicleGuaranteeEO);
	}

	/** Get Vehicle Guarantee E.O..
		@return Vehicle Guarantee E.O.	  */
	public String getVehicleGuaranteeEO () 
	{
		return (String)get_Value(COLUMNNAME_VehicleGuaranteeEO);
	}

	/** Set Vehicle Mark.
		@param VehicleMark Vehicle Mark	  */
	public void setVehicleMark (String VehicleMark)
	{
		set_Value (COLUMNNAME_VehicleMark, VehicleMark);
	}

	/** Get Vehicle Mark.
		@return Vehicle Mark	  */
	public String getVehicleMark () 
	{
		return (String)get_Value(COLUMNNAME_VehicleMark);
	}

	/** Set Vehicle Model.
		@param VehicleModel Vehicle Model	  */
	public void setVehicleModel (String VehicleModel)
	{
		set_Value (COLUMNNAME_VehicleModel, VehicleModel);
	}

	/** Get Vehicle Model.
		@return Vehicle Model	  */
	public String getVehicleModel () 
	{
		return (String)get_Value(COLUMNNAME_VehicleModel);
	}

	/** Set Vehicle Plate.
		@param VehiclePlate Vehicle Plate	  */
	public void setVehiclePlate (String VehiclePlate)
	{
		set_Value (COLUMNNAME_VehiclePlate, VehiclePlate);
	}

	/** Get Vehicle Plate.
		@return Vehicle Plate	  */
	public String getVehiclePlate () 
	{
		return (String)get_Value(COLUMNNAME_VehiclePlate);
	}

	/** Set Vehicle Year.
		@param VehicleYear Vehicle Year	  */
	public void setVehicleYear (String VehicleYear)
	{
		set_Value (COLUMNNAME_VehicleYear, VehicleYear);
	}

	/** Get Vehicle Year.
		@return Vehicle Year	  */
	public String getVehicleYear () 
	{
		return (String)get_Value(COLUMNNAME_VehicleYear);
	}
}