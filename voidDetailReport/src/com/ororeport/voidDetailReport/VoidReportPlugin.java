package com.ororeport.voidDetailReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.voidDetailReport.action.VoidDetailReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class VoidReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new VoidDetailReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales Detail";
	}

}
