package com.ororeport.salesreport.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.ororeport.salesreport.ui.SalesDetailReportView;

public class SalesDetailReportAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110637654763546195L;

	public SalesDetailReportAction() {
		super("Sales Report");
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		SalesDetailReportView browser = null;

		int index = tabbedPane.indexOfTab("Sales Report");
		if (index == -1) {
			browser = new SalesDetailReportView();
			tabbedPane.addTab("Sales Report", browser);
			
		} else {
			browser = (SalesDetailReportView) tabbedPane.getComponentAt(index);
		}

		tabbedPane.setSelectedComponent(browser);
	}
}
