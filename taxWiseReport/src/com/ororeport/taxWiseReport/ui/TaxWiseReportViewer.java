/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.ororeport.taxWiseReport.ui;

import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;

/**
 * @author SOMYA
 * 
 */
public class TaxWiseReportViewer extends ReportViewer {

	private static final long serialVersionUID = 7364556705257299449L;

	public TaxWiseReportViewer() {
		super();
		initComponents();
	}

	public TaxWiseReportViewer(Report report) {
		super(report);
	}

}
