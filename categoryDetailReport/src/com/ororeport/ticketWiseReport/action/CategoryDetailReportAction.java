package com.ororeport.categoryDetailReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.categoryDetailReport.CategoryDetailedReport;
import com.ororeport.categoryDetailReport.ui.CategoryDetailReportViewer;

public class CategoryDetailReportAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110637654763546195L;
	private static final String REPORT_TAB_NAME = "Category Report";

	public CategoryDetailReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		CategoryDetailReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new CategoryDetailReportViewer(new CategoryDetailedReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);
			
		} else {
			browser = (CategoryDetailReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
