package com.ororeport.groupDetailReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.groupDetailReport.GroupDetailedReport;
import com.ororeport.groupDetailReport.ui.GroupDetailReportViewer;

public class GroupDetailReportAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110637654763546195L;
	private static final String REPORT_TAB_NAME = "MenuGroup Wise Sales Daily";

	public GroupDetailReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		GroupDetailReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new GroupDetailReportViewer(new GroupDetailedReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);
			
		} else {
			browser = (GroupDetailReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
