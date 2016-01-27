package com.ororeport.dayWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.dayWiseReport.action.DayWiseReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class DayWiseReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new DayWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Consolidated Sales";
	}

}
