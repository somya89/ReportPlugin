/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.ororeport.voidDetailReport.ui;

import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;

/**
 * @author SOMYA
 * 
 */
public class VoidDetailReportViewer extends ReportViewer {

	private static final long serialVersionUID = -4088107561589097998L;

	public VoidDetailReportViewer() {
		super();
		initComponents();
	}

	public VoidDetailReportViewer(Report report) {
		super(report);
	}

}
