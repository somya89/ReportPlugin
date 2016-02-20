package com.ororeport.ticketWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.ticketWiseReport.action.TicketWiseReportAction;

@PluginImplementation
public class TicketWisePlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new TicketWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales Detail";
	}

}
