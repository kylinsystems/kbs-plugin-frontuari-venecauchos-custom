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
package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LVE_ReturnGuarantee
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_LVE_ReturnGuarantee 
{

    /** TableName=LVE_ReturnGuarantee */
    public static final String Table_Name = "LVE_ReturnGuarantee";

    /** AD_Table_ID=1000043 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name CreateConfirm */
    public static final String COLUMNNAME_CreateConfirm = "CreateConfirm";

	/** Set Create Confirm	  */
	public void setCreateConfirm (String CreateConfirm);

	/** Get Create Confirm	  */
	public String getCreateConfirm();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name DriverName */
    public static final String COLUMNNAME_DriverName = "DriverName";

	/** Set Driver Name	  */
	public void setDriverName (String DriverName);

	/** Get Driver Name	  */
	public String getDriverName();

    /** Column name DriverTaxID */
    public static final String COLUMNNAME_DriverTaxID = "DriverTaxID";

	/** Set Driver Tax ID	  */
	public void setDriverTaxID (String DriverTaxID);

	/** Get Driver Tax ID	  */
	public String getDriverTaxID();

    /** Column name DriverVehiclePlate */
    public static final String COLUMNNAME_DriverVehiclePlate = "DriverVehiclePlate";

	/** Set Driver Vehicle Plate	  */
	public void setDriverVehiclePlate (String DriverVehiclePlate);

	/** Get Driver Vehicle Plate	  */
	public String getDriverVehiclePlate();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsSOTrx */
    public static final String COLUMNNAME_IsSOTrx = "IsSOTrx";

	/** Set Sales Transaction.
	  * This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx);

	/** Get Sales Transaction.
	  * This is a Sales Transaction
	  */
	public boolean isSOTrx();

    /** Column name LVE_ReturnGuarantee_ID */
    public static final String COLUMNNAME_LVE_ReturnGuarantee_ID = "LVE_ReturnGuarantee_ID";

	/** Set Return Guarantee	  */
	public void setLVE_ReturnGuarantee_ID (int LVE_ReturnGuarantee_ID);

	/** Get Return Guarantee	  */
	public int getLVE_ReturnGuarantee_ID();

    /** Column name LVE_Vendor_ID */
    public static final String COLUMNNAME_LVE_Vendor_ID = "LVE_Vendor_ID";

	/** Set Vendor	  */
	public void setLVE_Vendor_ID (int LVE_Vendor_ID);

	/** Get Vendor	  */
	public int getLVE_Vendor_ID();

	public org.compiere.model.I_C_BPartner getLVE_Vendor() throws RuntimeException;

    /** Column name MileageCar */
    public static final String COLUMNNAME_MileageCar = "MileageCar";

	/** Set Mileage Car	  */
	public void setMileageCar (BigDecimal MileageCar);

	/** Get Mileage Car	  */
	public BigDecimal getMileageCar();

    /** Column name MileageTire */
    public static final String COLUMNNAME_MileageTire = "MileageTire";

	/** Set Mileage Tire	  */
	public void setMileageTire (BigDecimal MileageTire);

	/** Get Mileage Tire	  */
	public BigDecimal getMileageTire();

    /** Column name M_Inventory_ID */
    public static final String COLUMNNAME_M_Inventory_ID = "M_Inventory_ID";

	/** Set Phys.Inventory.
	  * Parameters for a Physical Inventory
	  */
	public void setM_Inventory_ID (int M_Inventory_ID);

	/** Get Phys.Inventory.
	  * Parameters for a Physical Inventory
	  */
	public int getM_Inventory_ID();

	public org.compiere.model.I_M_Inventory getM_Inventory() throws RuntimeException;

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name Ref_Inventory_ID */
    public static final String COLUMNNAME_Ref_Inventory_ID = "Ref_Inventory_ID";

	/** Set Referenced Inventory	  */
	public void setRef_Inventory_ID (int Ref_Inventory_ID);

	/** Get Referenced Inventory	  */
	public int getRef_Inventory_ID();

	public org.compiere.model.I_M_Inventory getRef_Inventory() throws RuntimeException;

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UseofTire */
    public static final String COLUMNNAME_UseofTire = "UseofTire";

	/** Set Use of Tire	  */
	public void setUseofTire (String UseofTire);

	/** Get Use of Tire	  */
	public String getUseofTire();

    /** Column name VehicleGuaranteeEO */
    public static final String COLUMNNAME_VehicleGuaranteeEO = "VehicleGuaranteeEO";

	/** Set Vehicle Guarantee E.O.	  */
	public void setVehicleGuaranteeEO (String VehicleGuaranteeEO);

	/** Get Vehicle Guarantee E.O.	  */
	public String getVehicleGuaranteeEO();

    /** Column name VehicleMark */
    public static final String COLUMNNAME_VehicleMark = "VehicleMark";

	/** Set Vehicle Mark	  */
	public void setVehicleMark (String VehicleMark);

	/** Get Vehicle Mark	  */
	public String getVehicleMark();

    /** Column name VehicleModel */
    public static final String COLUMNNAME_VehicleModel = "VehicleModel";

	/** Set Vehicle Model	  */
	public void setVehicleModel (String VehicleModel);

	/** Get Vehicle Model	  */
	public String getVehicleModel();

    /** Column name VehiclePlate */
    public static final String COLUMNNAME_VehiclePlate = "VehiclePlate";

	/** Set Vehicle Plate	  */
	public void setVehiclePlate (String VehiclePlate);

	/** Get Vehicle Plate	  */
	public String getVehiclePlate();

    /** Column name VehicleYear */
    public static final String COLUMNNAME_VehicleYear = "VehicleYear";

	/** Set Vehicle Year	  */
	public void setVehicleYear (String VehicleYear);

	/** Get Vehicle Year	  */
	public String getVehicleYear();
}
