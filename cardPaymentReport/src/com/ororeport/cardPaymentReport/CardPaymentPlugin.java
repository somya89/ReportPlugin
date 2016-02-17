package com.ororeport.cardPaymentReport;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.extension.ReportPlugin;
import com.ororeport.cardPaymentReport.action.CardPaymentReportAction;
/**
 * @author SOMYA
 *
 */
@PluginImplementation
public class CardPaymentPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new CardPaymentReportAction()};
	}

	@Override
	public String getBaseMenuName() {
		return "Sales Detail";
	}

}
