/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.ororeport.dayWiseReport.ui;

import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;

/**
 * @author SOMYA
 * 
 */
public class DayWiseReportViewer extends ReportViewer {

	private static final long serialVersionUID = -4088107561589097998L;

	public DayWiseReportViewer() {
		super();
		initComponents();
	}

	public DayWiseReportViewer(Report report) {
		super(report);
	}

}
