package org.frontuari.webui.apps.form;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.frontuari.model.MLVEMajorPlan;
import org.frontuari.model.MLVEMajorPlanLine;

public class WCreateFromMajorPlanUI extends CreateFrom implements EventListener<Event>, ValueChangeListener {

	private WCreateFromWindow window;
	
	public WCreateFromMajorPlanUI(GridTab gridTab) {
		super(gridTab);
		log.info(getGridTab().toString());
		
		window = new WCreateFromWindow(this, getGridTab().getWindowNo());
		
		p_WindowNo = getGridTab().getWindowNo();

		try
		{
			if (!dynInit())
				return;
			zkInit();
			setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.showWindow(window);
	}
	
	/** Window No               */
	private int p_WindowNo;

	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(getClass());
		
	protected Label bPartnerLabel = new Label();
	protected WEditor bPartnerField;
	
	public void showWindow()
	{
		window.setVisible(true);
	}
	
	public void closeWindow()
	{
		window.dispose();
	}

	public Object getWindow() {
		return window;
	}

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception {
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "LVE_MajorPlan_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
		
		window.setTitle(getTitle());
		
		initBPartner(true);
		bPartnerField.addValueChangeListener(this);
		
		return true;
	}
	
	protected void zkInit() throws Exception
	{
		bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
        
		Borderlayout parameterLayout = new Borderlayout();
		ZKUpdateUtil.setHeight(parameterLayout, "110px");
		ZKUpdateUtil.setWidth(parameterLayout, "100%");
    	Panel parameterPanel = window.getParameterPanel();
		parameterPanel.appendChild(parameterLayout);
		
		Grid parameterStdLayout = GridFactory.newGridLayout();
    	Panel parameterStdPanel = new Panel();
		parameterStdPanel.appendChild(parameterStdLayout);

		Center center = new Center();
		parameterLayout.appendChild(center);
		center.appendChild(parameterStdPanel);
		
		Rows rows = (Rows) parameterStdLayout.newRows();
		Row row = rows.newRow();
		row.appendChild(bPartnerLabel.rightAlign());
		if (bPartnerField != null)
			row.appendChild(bPartnerField.getComponent());
	}
	
	private boolean 	m_actionActive = false;
	
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		BigDecimal lineNetAmt = BigDecimal.ZERO;
		//	Set LineNetAmt
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				lineNetAmt = lineNetAmt.add((BigDecimal)miniTable.getValueAt(i, 5));
			}
		}
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		String LineNetAmount = nf.format(lineNetAmt);
		statusBar.setStatusLine(Msg.translate(Env.getAD_Language(Env.getCtx()),"LineNetAmt")+": "+LineNetAmount);
	}
	
	/**
	 *  Save - Create Major Plan Lines
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName) {
		
		int LVE_MajorPlan_ID = ((Integer)getGridTab().getValue("LVE_MajorPlan_ID")).intValue();
		MLVEMajorPlan majorplan = new MLVEMajorPlan (Env.getCtx(), LVE_MajorPlan_ID, trxName);
		if (log.isLoggable(Level.CONFIG)) log.config(majorplan.toString());
		
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				//
				KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 1);				//  2-Invoice
				int C_Invoice_ID = pp.getKey();
				//  variable values
				BigDecimal openAmt = (BigDecimal)miniTable.getValueAt(i, 5);            //  5-OpenAmt
				if (log.isLoggable(Level.FINE)) log.fine("Line Invoice=" +" Amount= ");
				//	Create new Major Plan Lines
				MLVEMajorPlanLine mpLine = new MLVEMajorPlanLine(majorplan);
				mpLine.setC_Invoice_ID(C_Invoice_ID);
				mpLine.setAmount(openAmt);
				mpLine.saveEx();
			}
		}
		return true;
	}

	public void onEvent(Event e) throws Exception {
		if (m_actionActive)
			return;
	}
	
	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void valueChange (ValueChangeEvent e)
	{
		if (log.isLoggable(Level.CONFIG)) log.config(e.getPropertyName() + "=" + e.getNewValue());

		//  BPartner - load Invoice
		if (e.getPropertyName().equals("C_BPartner_ID"))
		{
			int C_BPartner_ID = ((Integer)e.getNewValue()).intValue();
			initBPInvoiceDetails(C_BPartner_ID,Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateDoc"),
					Env.getContextAsInt(Env.getCtx(), p_WindowNo, "LVE_MajorPlan_ID"),Env.getContextAsInt(Env.getCtx(), p_WindowNo, "LVE_MajorPlanType_ID"));
		}
		window.tableChanged(null);
	}   //  vetoableChange	
	
	/**************************************************************************
	 *  Load BPartner Field
	 *  @param forInvoice true if Invoices are to be created, false receipts
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		//  load BPartner
		String sqlExists = " EXISTS(SELECT 1 FROM LVE_MajorPlanTypeLine mptl "
				+ "WHERE (mptl.C_BPartner_ID = C_BPartner.C_BPartner_ID))";
		
		MLookup lookup = null;
		try {
			lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, DisplayType.TableDir, Env.getLanguage(Env.getCtx()), 
						"C_BPartner_ID", 0, false, sqlExists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bPartnerField = new WSearchEditor ("C_BPartner_ID", true, false, true, lookup);
		//
		String sql = "SELECT MAX(C_BPartner_ID) AS C_BPartner_ID "
				+ "FROM LVE_MajorPlanTypeLine mptl "
				+ "INNER JOIN LVE_MajorPlan mp ON mptl.LVE_MajorPlanType_ID = mp.LVE_MajorPlanType_ID "
				+ "WHERE mp.DateDoc BETWEEN mptl.ValidFrom AND COALESCE(mptl.ValidTo,mp.DateDoc) "
				+ "AND LVE_MajorPlan_ID = ? ";
		int C_BPartner_ID = DB.getSQLValue(null, sql, Env.getContextAsInt(Env.getCtx(), p_WindowNo, "LVE_MajorPlan_ID"));
		bPartnerField.setValue(new Integer(C_BPartner_ID));

		//  initial loading
		initBPInvoiceDetails(C_BPartner_ID,Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateDoc"),
				Env.getContextAsInt(Env.getCtx(), p_WindowNo, "LVE_MajorPlan_ID"),Env.getContextAsInt(Env.getCtx(), p_WindowNo, "LVE_MajorPlanType_ID"));
	}   //  initBPartner
	
	/**
	 *  Load PBartner dependent Invoice Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	protected void initBPInvoiceDetails (int C_BPartner_ID, Timestamp DateDoc, int LVE_MajorPlan_ID, int LVE_MajorPlanType_ID)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_BPartner_ID=" + C_BPartner_ID);
		loadTableOIS(getInvoiceData(C_BPartner_ID,DateDoc,LVE_MajorPlan_ID,LVE_MajorPlanType_ID));
	}   //  initBPartnerOIS
	
	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	protected Vector<Vector<Object>> getInvoiceData (int C_BPartner_ID, Timestamp DateDoc, int LVE_MajorPlan_ID, int LVE_MajorPlanType_ID)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		//	Display
		StringBuffer display = new StringBuffer("C_Invoice.DocumentNo, ")
			.append(DB.TO_CHAR("C_Invoice.DateInvoiced", DisplayType.Date, Env.getAD_Language(Env.getCtx())))
			.append(",C_Invoice.C_BPartner_ID,COALESCE(LCO_TaxIdType.Name,'')||C_BPartner.TaxID||' '||C_BPartner.Name AS BPartner, ")
			.append(DB.TO_CHAR("C_Invoice.GrandTotal", DisplayType.Amount, Env.getAD_Language(Env.getCtx())))
			.append(",invoiceopen(C_Invoice.C_Invoice_ID,0) AS OpenAmt ");
		StringBuffer sql = new StringBuffer("SELECT C_Invoice.C_Invoice_ID,").append(display)
			.append("FROM C_Invoice "
			+ "INNER JOIN C_BPartner ON (C_Invoice.C_BPartner_ID = C_BPartner.C_BPartner_ID) "
			+ "LEFT JOIN LCO_TaxIdType ON (LCO_TaxIdType.LCO_TaxIdType_ID = C_BPartner.LCO_TaxIdType_ID) "
			+ "WHERE EXISTS (SELECT 1 from LVE_MajorPlanTypeLine mptl "
			+ "INNER JOIN C_Invoice inv ON inv.C_BPartner_ID = mptl.C_BPartner_ID "
			+ "WHERE inv.IsPaid='N' AND inv.IsSoTrx='N' AND inv.DocStatus IN ('CO','CL') AND inv.C_Invoice_ID = C_Invoice.C_Invoice_ID "
			+ "AND ? BETWEEN mptl.ValidFrom AND COALESCE(mptl.ValidTo,?) "
			+ "AND mptl.LVE_MajorPlanType_ID = ? AND inv.C_Invoice_ID "
			+ "NOT IN (SELECT C_Invoice_ID FROM LVE_MajorPlan mp "
			+ "INNER JOIN LVE_MajorPlanLine mpl ON mp.LVE_MajorPlan_ID = mpl.LVE_MajorPlan_ID "
			+ "WHERE mp.DocStatus IN ('CO','CL') OR mp.LVE_MajorPlan_ID = ?)) ");
		if(C_BPartner_ID!= 0){
			sql = sql.append("AND C_Invoice.C_BPartner_ID = "+C_BPartner_ID);
		}
		sql = sql.append(" ORDER BY C_Invoice.DateInvoiced,C_Invoice.DocumentNo");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setTimestamp(1, DateDoc);
			pstmt.setTimestamp(2, DateDoc);
			pstmt.setInt(3, LVE_MajorPlanType_ID);
			pstmt.setInt(4, LVE_MajorPlan_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));           //  0-Selection
				KeyNamePair pp = new KeyNamePair(rs.getInt(1),rs.getString(2));
				line.add(pp);                   //  1-DocumentNo
				line.add(rs.getString(3));                   //  2-DateInvoiced
				pp = new KeyNamePair(rs.getInt(4), rs.getString(5).trim());
				line.add(pp);                           //  3-BPartner
				line.add(rs.getString(6));				// 4-GrandTotal
				line.add(rs.getBigDecimal(7));				// 5-OpenAmt
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return data;
	}   //  LoadInvoiceData
	
	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector<?> data)
	{
		window.getWListbox().clear();
		
		//  Remove previous listeners
		window.getWListbox().getModel().removeTableModelListener(window);
		//  Set Model
		ListModelTable model = new ListModelTable(data);
		model.addTableModelListener(window);
		window.getWListbox().setData(model, getOISColumnNames());
		//
		
		configureMiniTable(window.getWListbox());
	}   //  loadOrder

	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
	    Vector<String> columnNames = new Vector<String>(5);
	    columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
	    columnNames.add(Msg.translate(Env.getCtx(), "DocumentNo"));
	    columnNames.add(Msg.translate(Env.getCtx(), "DateInvoiced"));
	    columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
	    columnNames.add(Msg.translate(Env.getCtx(), "GrandTotal"));
	    columnNames.add(Msg.translate(Env.getCtx(), "OpenAmt"));

	    return columnNames;
	}
	


	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, String.class, true);	    //  1-DocumentNo
		miniTable.setColumnClass(2, String.class, true);        //  2-DateInvoiced
		miniTable.setColumnClass(3, String.class, true);        //  3-BPartner
		miniTable.setColumnClass(4, String.class, true);    	//  4-GrandTotal
		miniTable.setColumnClass(5, BigDecimal.class, false);    //  5-OpenAmt
		//  Table UI
		miniTable.autoSize();
	}
	
}