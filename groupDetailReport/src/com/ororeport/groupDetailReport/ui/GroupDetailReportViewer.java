/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.ororeport.groupDetailReport.ui;

import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;

/**
 * @author SOMYA
 * 
 */
public class GroupDetailReportViewer extends ReportViewer {

	private static final long serialVersionUID = -4088107561589097998L;

	public GroupDetailReportViewer() {
		super();
		initComponents();
	}

	public GroupDetailReportViewer(Report report) {
		super(report);
	}

}
