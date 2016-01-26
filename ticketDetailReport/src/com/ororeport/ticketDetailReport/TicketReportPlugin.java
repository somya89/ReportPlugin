package com.ororeport.ticketDetailReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.ticketDetailReport.action.TicketDetailReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class TicketReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new TicketDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales";
	}

}
