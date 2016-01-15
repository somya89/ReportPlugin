package com.ororeport.orderDetailReport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.orderDetailReport.OrderDetailedReport;
import com.ororeport.orderDetailReport.ui.OrderDetailReportViewer;

public class OrderDetailReportAction extends AbstractAction {

	/**
	 * @author SOMYA
	 *
	 */
	private static final long serialVersionUID = -5110637654763546195L;
	private static final String REPORT_TAB_NAME = "Order Detail Report";

	public OrderDetailReportAction() {
		super(REPORT_TAB_NAME);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		OrderDetailReportViewer browser = null;

		int index = tabbedPane.indexOfTab(REPORT_TAB_NAME);
		if (index == -1) {
			browser = new OrderDetailReportViewer(new OrderDetailedReport());
			tabbedPane.addTab(REPORT_TAB_NAME, browser);
			
		} else {
			browser = (OrderDetailReportViewer) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
