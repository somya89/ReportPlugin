package com.ororeport.taxWiseReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.taxWiseReport.action.TaxWiseReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class TaxWiseReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new TaxWiseReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Consolidated Tax";
	}

}
