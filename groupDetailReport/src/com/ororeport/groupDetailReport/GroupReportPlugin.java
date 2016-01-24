package com.ororeport.groupDetailReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.groupDetailReport.action.GroupDetailReportAction;

@PluginImplementation
public class GroupReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new GroupDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales";
	}

}
