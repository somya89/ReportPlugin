package com.ororeport.orderDetailReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.orderDetailReport.action.OrderDetailReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class OrderReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new OrderDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales";
	}

}
