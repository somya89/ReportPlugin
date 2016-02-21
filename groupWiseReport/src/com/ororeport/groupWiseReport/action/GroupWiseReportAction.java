package com.ororeport.groupWiseReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.groupWiseReport.ConsolidatedGroupReport;
import com.ororeport.groupWiseReport.ui.GroupWiseReportViewer;

public class GroupWiseReportAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110637654763546195L;
	private static final String REPORT_TAB_NAME = "Menu Group Wise Sales Consolidated";

	public GroupWiseReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		GroupWiseReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new GroupWiseReportViewer(new ConsolidatedGroupReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);
			
		} else {
			browser = (GroupWiseReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
