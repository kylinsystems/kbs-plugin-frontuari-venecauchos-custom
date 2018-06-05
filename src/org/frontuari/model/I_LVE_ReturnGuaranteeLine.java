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

/** Generated Interface for LVE_ReturnGuaranteeLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_LVE_ReturnGuaranteeLine 
{

    /** TableName=LVE_ReturnGuaranteeLine */
    public static final String Table_Name = "LVE_ReturnGuaranteeLine";

    /** AD_Table_ID=1000044 */
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

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

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

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name LVE_ReturnGuarantee_ID */
    public static final String COLUMNNAME_LVE_ReturnGuarantee_ID = "LVE_ReturnGuarantee_ID";

	/** Set Return Guarantee	  */
	public void setLVE_ReturnGuarantee_ID (int LVE_ReturnGuarantee_ID);

	/** Get Return Guarantee	  */
	public int getLVE_ReturnGuarantee_ID();

	public org.frontuari.model.I_LVE_ReturnGuarantee getLVE_ReturnGuarantee() throws RuntimeException;

    /** Column name LVE_ReturnGuaranteeLine_ID */
    public static final String COLUMNNAME_LVE_ReturnGuaranteeLine_ID = "LVE_ReturnGuaranteeLine_ID";

	/** Set Return Guarantee Line	  */
	public void setLVE_ReturnGuaranteeLine_ID (int LVE_ReturnGuaranteeLine_ID);

	/** Get Return Guarantee Line	  */
	public int getLVE_ReturnGuaranteeLine_ID();

    /** Column name M_InOutLine_ID */
    public static final String COLUMNNAME_M_InOutLine_ID = "M_InOutLine_ID";

	/** Set Shipment/Receipt Line.
	  * Line on Shipment or Receipt document
	  */
	public void setM_InOutLine_ID (int M_InOutLine_ID);

	/** Get Shipment/Receipt Line.
	  * Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID();

	public org.compiere.model.I_M_InOutLine getM_InOutLine() throws RuntimeException;

    /** Column name Model */
    public static final String COLUMNNAME_Model = "Model";

	/** Set Model	  */
	public void setModel (String Model);

	/** Get Model	  */
	public String getModel();

    /** Column name M_RMAType_ID */
    public static final String COLUMNNAME_M_RMAType_ID = "M_RMAType_ID";

	/** Set RMA Type.
	  * Return Material Authorization Type
	  */
	public void setM_RMAType_ID (int M_RMAType_ID);

	/** Get RMA Type.
	  * Return Material Authorization Type
	  */
	public int getM_RMAType_ID();

	public org.compiere.model.I_M_RMAType getM_RMAType() throws RuntimeException;

    /** Column name PercentUseAnalisys */
    public static final String COLUMNNAME_PercentUseAnalisys = "PercentUseAnalisys";

	/** Set Percent Use Analisys	  */
	public void setPercentUseAnalisys (BigDecimal PercentUseAnalisys);

	/** Get Percent Use Analisys	  */
	public BigDecimal getPercentUseAnalisys();

    /** Column name PosTire */
    public static final String COLUMNNAME_PosTire = "PosTire";

	/** Set Position Tire	  */
	public void setPosTire (String PosTire);

	/** Get Position Tire	  */
	public String getPosTire();

    /** Column name QtyEntered */
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";

	/** Set Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered);

	/** Get Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered();

    /** Column name ResAnalisys */
    public static final String COLUMNNAME_ResAnalisys = "ResAnalisys";

	/** Set mm/32 Res. Analisys	  */
	public void setResAnalisys (String ResAnalisys);

	/** Get mm/32 Res. Analisys	  */
	public String getResAnalisys();

    /** Column name SerialNo */
    public static final String COLUMNNAME_SerialNo = "SerialNo";

	/** Set Serial Number	  */
	public void setSerialNo (String SerialNo);

	/** Get Serial Number	  */
	public String getSerialNo();

    /** Column name TireMeasure */
    public static final String COLUMNNAME_TireMeasure = "TireMeasure";

	/** Set Tire Measure	  */
	public void setTireMeasure (String TireMeasure);

	/** Get Tire Measure	  */
	public String getTireMeasure();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
