package com.ororeport.categoryWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.categoryWiseReport.action.CategoryWiseReportAction;

@PluginImplementation
public class CategoryWiseReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new CategoryWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Consolidated Sales";
	}

}
