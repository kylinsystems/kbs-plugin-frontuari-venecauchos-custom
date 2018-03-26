package org.frontuari.webui.apps.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.MTable;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.frontuari.model.MLVEMajorPlan;
import org.frontuari.model.MLVEMajorPlanLine;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

public class WCreatePaymentMajorPlanUI extends CreatePaymentMajorPlan 
implements IFormController,EventListener<Event>, WTableModelListener, ValueChangeListener {
	
	private CustomForm form = new CustomForm();
    /** Logger.          */
    private static CLogger log = CLogger.getCLogger(WCreatePaymentMajorPlanUI.class);
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/** Format                  */
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount);
    
    /**
     * Default constructor.
     */
    public WCreatePaymentMajorPlanUI()
    {
    	Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", "N");   //  defaults to no
		try
		{
			super.dynInit();
			dynInit();
			zkInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
    }
    
    private Borderlayout mainLayout = new Borderlayout();
	private Panel parameterPanel = new Panel();
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Label lMajorPlan = new Label();
	private WTableDirEditor fMajorPlan;
	private Label   lDocAction = new Label();
	private WTableDirEditor docAction;
	//	data panel
	private Label dataStatus = new Label();
	private WListbox miniTable = ListboxFactory.newDataTable();

	// command panel
	private Panel commandPanel = new Panel();
	ConfirmPanel cp = new ConfirmPanel();
	private Button bOk = cp.createButton(ConfirmPanel.A_OK);
	private Button bCancel = cp.createButton(ConfirmPanel.A_CANCEL);
	private Button bRefresh = cp.createButton(ConfirmPanel.A_REFRESH);
	private Grid commandLayout = GridFactory.newGridLayout();
	
	private boolean loading = false;
	private Label totalLabel = new Label();
	private Textbox totalField = new Textbox();
    /**
	 *	Static Init.
	 *  <pre>
	 *  selPanel (tabbed)
	 *      fOrg 
	 *      scrollPane & miniTable
	 *  genPanel
	 *      info
	 *  </pre>
	 *  @throws Exception
	 */
	void zkInit() throws Exception
	{
		//
		form.appendChild(mainLayout);
		parameterPanel.appendChild(parameterLayout);
		
		lMajorPlan.setText(Msg.translate(Env.getCtx(), "LVE_MajorPlan_ID"));
		dataStatus.setText(" ");
		

		totalLabel.setText(Msg.translate(Env.getCtx(), "TotalLines"));
		totalField.setText(m_format.format(BigDecimal.ZERO));
		totalField.setReadonly(true);
		
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
		bRefresh.addActionListener(this);
		
		// Parameter Panel
		North north = new North();
		north.setStyle("border: none");
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
		Rows rows = null;
		Row row = null;
		ZKUpdateUtil.setWidth(parameterLayout, "90%");
		rows = parameterLayout.newRows();
		row = rows.newRow();
		row.appendCellChild(lMajorPlan.rightAlign());
		ZKUpdateUtil.setHflex(fMajorPlan.getComponent(), "true");
		row.appendCellChild(fMajorPlan.getComponent(),2);
		row.appendCellChild(new Space());
		row.appendCellChild(lDocAction.rightAlign());
		ZKUpdateUtil.setHflex(docAction.getComponent(), "true");
		row.appendCellChild(docAction.getComponent(),2);
		row.appendCellChild(new Space());
		
		// Data Panel
		Center center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(miniTable);
		ZKUpdateUtil.setWidth(miniTable, "99%");
		center.setStyle("border: none");
		
		// Command Panel
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(commandPanel);
		commandPanel.appendChild(commandLayout);
		ZKUpdateUtil.setWidth(commandLayout, "90%");
		rows = commandLayout.newRows();
		row = rows.newRow();
		row.appendChild(bRefresh);
		row.appendCellChild(totalLabel.rightAlign());
		ZKUpdateUtil.setHflex(totalLabel, "true");
		row.appendCellChild(totalField, 2);
		ZKUpdateUtil.setHflex(totalField, "true");
		row.appendCellChild(bOk);
		ZKUpdateUtil.setHflex(bOk, "true");
		row.appendCellChild(bCancel);
		ZKUpdateUtil.setHflex(bCancel, "true");
	}	//	zkInit
	
	/**
	 *	Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		String sqlExists = " DocStatus = 'CO' AND (SELECT SUM(CASE WHEN IsPaid = 'N' THEN 1 ELSE 0 END) "
				+ "FROM LVE_MajorPlanLine mpl WHERE mpl.LVE_MajorPlan_ID = LVE_MajorPlan.LVE_MajorPlan_ID) > 0 ";
		MLookup MajorPlanL = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, DisplayType.TableDir, Env.getLanguage(Env.getCtx()),
				"LVE_MajorPlan_ID", 0, false, sqlExists);
		fMajorPlan = new WTableDirEditor ("LVE_MajorPlan_ID", false, false, true, MajorPlanL);
		fMajorPlan.addValueChangeListener(this);

		//	Search AD_Table from MajorPlan TableID
		MTable tpay = new MTable(Env.getCtx(), MPayment.Table_ID , null);
		//	Get Column from MajorPlan ColumnID
		MColumn colpay = tpay.getColumn(MPayment.COLUMNNAME_DocAction);
		//      Document Action Prepared/ Completed
		lDocAction.setText(Msg.translate(Env.getCtx(), "DocAction"));
		MLookup docActionL = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), colpay.get_ID(),
				DisplayType.List, Env.getLanguage(Env.getCtx()), "DocAction", 135 /* _Document Action */,
				false, "AD_Ref_List.Value IN ('CO','PR')");
		docAction = new WTableDirEditor("DocAction", true, false, true,docActionL);
		docAction.setValue(DocAction.ACTION_Complete);
	}	//	fillPicks

	/**
	 *  Load Data
	 */
	public void loadData()
	{
		//  Set Model
		Vector<Vector<Object>> data = getData();
		Vector<String> columnNames = getColumnNames();
		// Remove Previous Listeners
		miniTable.getModel().removeTableModelListener(this);
		
		ListModelTable modelI = new ListModelTable(data);
		modelI.addTableModelListener(this);
		miniTable.setData(modelI, columnNames);
		setColumnClass(miniTable);
		//	Color for TotalLines Columns
		miniTable.setColorColumn(5);
		miniTable.setColorColumn(6);
	}   //  executeQuery
	
	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection()
	{
		m_noSelected = 0;
		BigDecimal selectedAmt = Env.ZERO;

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			boolean isSelected = (Boolean)miniTable.getModel().getValueAt(i, 0);
			if (isSelected)
			{
				BigDecimal amt = ((BigDecimal)miniTable.getModel().getValueAt(i, 5))
						.add((BigDecimal)miniTable.getModel().getValueAt(i, 6));
				if (amt != null)
					selectedAmt = selectedAmt.add(amt);
				m_noSelected++;
			}
		}

		//  Information
		StringBuilder info = new StringBuilder();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" / ").append(miniTable.getRowCount());
		
		totalField.setText(m_format.format(selectedAmt));
		dataStatus.setText(info.toString());
	}   //  calculateSelection

	/**
	 *	Value Change Listener - requery
	 *  @param e event
	 */
	public void valueChange(ValueChangeEvent e)
	{
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		
		if (value == null)
			return;
		
		if (name.equals("LVE_MajorPlan_ID"))
			m_LVE_MajorPlan_ID = (Integer)e.getNewValue();
		//	Load Data with change
		loadData();
	}	//	vetoableChange
	
	public void tableChanged(WTableModelEvent e) {
		if (! loading )
			calculateSelection();
	}
	
	public void onEvent(Event event) throws Exception {
		log.config("");
		if(event.getTarget().equals(bOk))
			generate();
		else if (event.getTarget().equals(bCancel))
			SessionManager.getAppDesktop().closeActiveWindow();
		else if (event.getTarget().equals(bRefresh))
			loadData();		
	}

	/**************************************************************************
	 *	Generate Shipments
	 */
	public void generate()
	{
		log.info("");
		
		if (miniTable.getRowCount() == 0)
			return;
		
		calculateSelection();
		if (m_noSelected == 0)
			return;
		
		BigDecimal payAmt = BigDecimal.ZERO;
		String clauseIn = " AND C_Invoice_ID IN(";
		for ( int r = 0; r < miniTable.getModel().getRowCount(); r++ )
		{
			boolean isSelected = (Boolean)miniTable.getModel().getValueAt(r, 0);
			
			if (isSelected)
			{
				KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(r, 1);				//  2-Invoice
				int C_Invoice_ID = pp.getKey();
				clauseIn+=C_Invoice_ID+",";
				payAmt = payAmt
						.add(((BigDecimal)miniTable.getModel().getValueAt(r, 5))
						.add((BigDecimal)miniTable.getModel().getValueAt(r, 6)));
			}
		}
		//	get Major Plan 
		MLVEMajorPlan mP = new MLVEMajorPlan(Env.getCtx(), m_LVE_MajorPlan_ID, null);
		//	GeneratePay
		MPayment pay = new MPayment(Env.getCtx(), 0, null);
		pay.setIsReceipt(false);
		pay.setC_BankAccount_ID(mP.getLVE_MajorPlanType().getC_BankAccount_ID());
		pay.setC_BPartner_ID(mP.getLVE_MajorPlanType().getC_BPartner_ID());
		pay.setDateTrx(new Timestamp (System.currentTimeMillis()));
		pay.setC_Charge_ID(mP.getLVE_MajorPlanType().getC_Charge_ID());
		pay.setTenderType(MPayment.TENDERTYPE_Account);
		pay.set_ValueOfColumn("LVE_MajorPlan_ID", m_LVE_MajorPlan_ID);
		pay.setC_Currency_ID(Env.getContextAsInt(Env.getCtx(),"$C_Currency_ID"));
		pay.setPayAmt(payAmt);
		pay.saveEx();
		//	Process Payment 
		pay.processIt((String)docAction.getValue());
		pay.saveEx();
		//	Update rows payed
		clauseIn = clauseIn.substring(0, clauseIn.length()-1)+") ";
		MLVEMajorPlanLine[] lines = mP.getLines(clauseIn);
		for(MLVEMajorPlanLine line : lines){
			line.setC_Payment_ID(pay.getC_Payment_ID());
			line.setPayDate(pay.getDateTrx());
			line.setIsPaid(true);
			line.saveEx();
		}
	}	//	generatePayments

	public ADForm getForm()
	{
		return form;
	}
}