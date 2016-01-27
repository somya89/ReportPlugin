package com.ororeport.groupWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.groupWiseReport.action.GroupWiseReportAction;

@PluginImplementation
public class GroupWiseReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new GroupWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Consolidated Sales";
	}

}
