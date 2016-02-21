package com.ororeport.menuItemWiseReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.menuItemWiseReport.ConsolidatedMenuItemReport;
import com.ororeport.menuItemWiseReport.ui.MenuItemWiseReportViewer;

public class MenuItemWiseReportAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110637654763546195L;
	private static final String REPORT_TAB_NAME = "MenuItem Wise Sales Consolidated";

	public MenuItemWiseReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		MenuItemWiseReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new MenuItemWiseReportViewer(new ConsolidatedMenuItemReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);
			
		} else {
			browser = (MenuItemWiseReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
