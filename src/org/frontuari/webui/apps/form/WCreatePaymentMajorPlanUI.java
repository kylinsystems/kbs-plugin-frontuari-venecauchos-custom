package org.frontuari.webui.apps.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.DocumentLink;
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
import org.adempiere.webui.editor.WDateEditor;
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
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.frontuari.model.MLVEMajorPlan;
import org.frontuari.model.MLVEMajorPlanLine;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
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
	/** Total Pay from selected lines */
	BigDecimal totalPay = BigDecimal.ZERO;
    
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
			commandPanel.appendChild(new Separator());
			commandPanel.appendChild(statusBar);
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
	private Label dateLabel = new Label();
	private WDateEditor dateField = new WDateEditor();
	//	data panel
	private Hlayout statusBar = new Hlayout();
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
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		dataStatus.setText(" ");
		statusBar.appendChild(dataStatus);
		

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
		//	Major Plan
		row = rows.newRow();
		row.appendCellChild(lMajorPlan.rightAlign());
		ZKUpdateUtil.setHflex(fMajorPlan.getComponent(), "true");
		row.appendCellChild(fMajorPlan.getComponent(),2);
		//	PayDate
		Hbox box = new Hbox();
		box.appendChild(dateLabel.rightAlign());
		box.appendChild(dateField.getComponent());
		row.appendCellChild(box);
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
		//  Date set to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(Env.getCtx(), "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateField.setValue(new Timestamp(cal.getTimeInMillis()));
		dateField.addValueChangeListener(this);
		//  Translation
		statusBar.appendChild(new Label(Msg.getMsg(Env.getCtx(), "AllocateStatus")));
		ZKUpdateUtil.setVflex(statusBar, "min");
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
		totalPay = selectedAmt;
		dataStatus.setText(info.toString());
		statusBar.getChildren().clear();
		statusBar.appendChild(dataStatus);
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
		else if(name.equals("Date"))
			m_PayDate = (Timestamp)e.getNewValue();
		//	Load Data with change
		loadData();
	}	//	vetoableChange
	
	public void tableChanged(WTableModelEvent e) {
		if (! loading )
			calculateSelection();
	}
	
	public void onEvent(Event event) throws Exception {
		log.config("");
		if(event.getTarget().equals(bOk)){
			bOk.setEnabled(false);
			statusBar.getChildren().clear();
			MPayment payment = generate();
			loadData();
			bOk.setEnabled(true);
			if(payment != null){
				DocumentLink link = new DocumentLink(payment.getDocumentNo(), payment.get_Table_ID(), payment.get_ID());				
				statusBar.appendChild(link);
			}
		}
		else if (event.getTarget().equals(bCancel))
			SessionManager.getAppDesktop().closeActiveWindow();
		else if (event.getTarget().equals(bRefresh))
			loadData();
	}

	/**************************************************************************
	 *	Generate Shipments
	 */
	public MPayment generate()
	{
		log.info("");
		
		if (miniTable.getRowCount() == 0)
			return null;
		
		calculateSelection();
		if (m_noSelected == 0)
			return null;
		
		try{
			final MPayment[] payment = new MPayment[1];
			Trx.run(new TrxRunnable() 
			{
				public void run(String trxName)
				{
					statusBar.getChildren().clear();
//					get Major Plan 
					MLVEMajorPlan mP = new MLVEMajorPlan(Env.getCtx(), m_LVE_MajorPlan_ID, trxName);
					//	GeneratePay
					MPayment pay = new MPayment(Env.getCtx(), 0, trxName);
					pay.setIsReceipt(false);
					pay.setC_BankAccount_ID(mP.getLVE_MajorPlanType().getC_BankAccount_ID());
					pay.setC_BPartner_ID(mP.getLVE_MajorPlanType().getC_BPartner_ID());
					pay.setDateTrx(m_PayDate);
					pay.setDateAcct(m_PayDate);
					pay.setTenderType(MPayment.TENDERTYPE_Account);
					pay.set_ValueOfColumn("LVE_MajorPlan_ID", m_LVE_MajorPlan_ID);
					pay.setC_Currency_ID(Env.getContextAsInt(Env.getCtx(),"$C_Currency_ID"));
					pay.setPayAmt(totalPay);
					pay.saveEx(trxName);
					//	Process Payment 
					if(pay.processIt(MPayment.ACTION_Complete))
						pay.saveEx(trxName);
					else
						throw new AdempiereException(pay.getProcessMsg()); 
					//	Create Allocation Header
					MAllocationHdr allHdr = new MAllocationHdr(Env.getCtx(), 0, trxName);
					MUser user = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), trxName);
					allHdr.setDateTrx(m_PayDate);
					allHdr.setDateAcct(m_PayDate);
					allHdr.setDescription(Msg.translate(Env.getCtx(),"CreatedBy")+": "+user.getName());
					allHdr.setC_Currency_ID(Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID"));
					allHdr.saveEx(trxName);
					//	Create Line from Payment
					MAllocationLine allLinePay = new MAllocationLine(allHdr);
					allLinePay.setC_Payment_ID(pay.getC_Payment_ID());
					allLinePay.setC_BPartner_ID(pay.getC_BPartner_ID());
					allLinePay.setAmount(totalPay.negate());
					allLinePay.saveEx(trxName);
					for ( int r = 0; r < miniTable.getModel().getRowCount(); r++ )
					{
						boolean isSelected = (Boolean)miniTable.getModel().getValueAt(r, 0);
						
						if (isSelected)
						{
							BigDecimal payAmtMP = (BigDecimal)miniTable.getModel().getValueAt(r, 5); 
							BigDecimal payAmtInt = (BigDecimal)miniTable.getModel().getValueAt(r, 6); 
							KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(r, 1); //  2-Invoice
							int C_Invoice_ID = pp.getKey();
							int MPLine_ID = DB.getSQLValue(null, "SELECT LVE_MajorPlanLine_ID FROM LVE_MajorPlanLine WHERE LVE_MajorPlan_ID = ? AND C_Invoice_ID = ?", m_LVE_MajorPlan_ID,C_Invoice_ID);
							MLVEMajorPlanLine line = new MLVEMajorPlanLine(Env.getCtx(), MPLine_ID, trxName);
							//	Create AllocationLine for pay Major Plan
							MAllocationLine allLineMP = new MAllocationLine(allHdr);
							allLineMP.setC_BPartner_ID(pay.getC_BPartner_ID());
							allLineMP.setC_Charge_ID(mP.getLVE_MajorPlanType().getC_Charge_ID());
							allLineMP.set_ValueOfColumn("LVE_MajorPlanLine_ID", MPLine_ID);
							allLineMP.setAmount(payAmtMP);
							allLineMP.saveEx(trxName);
							//	Create AllocationLine for pay Interest
							MAllocationLine allLineInt = new MAllocationLine(allHdr);
							allLineInt.setC_BPartner_ID(pay.getC_BPartner_ID());
							allLineInt.setC_Charge_ID(mP.getLVE_MajorPlanType().getLVE_Charge_ID());
							allLineInt.set_ValueOfColumn("LVE_MajorPlanLine_ID", MPLine_ID);
							allLineInt.setAmount(payAmtInt);
							allLineInt.saveEx(trxName);
							
							BigDecimal openAmt = line.openAmt();
							openAmt = openAmt.subtract(payAmtMP);
							//	Check if pay all major plan documents for set with paid
							if(openAmt.compareTo(BigDecimal.ZERO) < 0){
								line.setIsPaid(true);
								line.saveEx(trxName);
							}
						}
					}
					if(allHdr.processIt(MAllocationHdr.DOCACTION_Complete))
						allHdr.saveEx(trxName);
					else
						throw new AdempiereException(allHdr.getProcessMsg());
					
					payment[0] = pay;
				}
			});
			return payment[0];
		}catch(Exception e){
			FDialog.error(form.getWindowNo(), form, "Error", e.getLocalizedMessage());
			return null;
		}
	}	//	generatePayments

	public ADForm getForm()
	{
		return form;
	}
}