package com.ororeport.menuItemWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.menuItemWiseReport.action.MenuItemWiseReportAction;

@PluginImplementation
public class MenuItemWiseReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new MenuItemWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Consolidated Sales";
	}

}
