package com.ororeport.menuItemDetailReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.menuItemDetailReport.action.MenuItemDetailReportAction;

@PluginImplementation
public class MenuItemReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new MenuItemDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales Detail";
	}

}
