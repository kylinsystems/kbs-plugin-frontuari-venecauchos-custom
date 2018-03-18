package org.frontuari.webui.apps.form;

import java.util.EventListener;

import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.zkoss.zhtml.Iframe;

public class CreateFromMajorPlan implements IFormController, EventListener {

	private CustomForm m_form = new CustomForm();
	private Iframe iframe = new Iframe();
	
	public CreateFromMajorPlan()
	{
		init();
	}
	
	private void init()
	{
		
	}
	
	public ADForm getForm() {
		return m_form;
	}

}
