package com.ororeport.taxWiseReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.taxWiseReport.TaxWiseReport;
import com.ororeport.taxWiseReport.ui.TaxWiseReportViewer;

public class TaxWiseReportAction extends AbstractAction {
	/**
	 * @author SOMYA
	 * 
	 */
	private static final long serialVersionUID = -8486823738235328827L;
	private static final String REPORT_TAB_NAME = "Daily Sales with TAX Breakup";

	public TaxWiseReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		TaxWiseReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new TaxWiseReportViewer(new TaxWiseReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);

		} else {
			browser = (TaxWiseReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
