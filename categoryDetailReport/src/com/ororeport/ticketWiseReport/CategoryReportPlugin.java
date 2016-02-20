package com.ororeport.ticketWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.ticketWiseReport.action.CategoryDetailReportAction;

@PluginImplementation
public class CategoryReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new CategoryDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales Detail";
	}

}
