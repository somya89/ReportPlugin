/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.ororeport.ticketDetailReport.ui;

import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;

/**
 * @author SOMYA
 * 
 */
public class TicketDetailReportViewer extends ReportViewer {

	private static final long serialVersionUID = -4088107561589097998L;

	public TicketDetailReportViewer() {
		super();
		initComponents();
	}

	public TicketDetailReportViewer(Report report) {
		super(report);
	}

}
