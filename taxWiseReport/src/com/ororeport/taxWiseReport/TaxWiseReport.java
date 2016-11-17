package com.ororeport.taxWiseReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;

/**
 * @author SOMYA
 * 
 */
public class TaxWiseReport extends Report {
	private TaxWiseReportModel itemReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/taxWiseReport/template/";

	public TaxWiseReport() {
		super("TaxWiseReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		TaxWiseReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("taxwise_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Daily Sales with TAX Breakup");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatFullDate(getStartDate()) + " to " + ReportService.formatFullDate(getEndDate()));
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("itemReport", itemReport);

		JasperReport masterReport = ReportUtil.getReport("report_template", USER_REPORT_DIR, this.getClass());
		print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	/**
	 * 
	 */
	public void createModels(Date date1, Date date2) {
		List<TaxWiseReportItem> itemList = new ArrayList<TaxWiseReportItem>();

		TaxWiseReportItem grandTotalReportItem = new TaxWiseReportItem();
		grandTotalReportItem.setDate("** GRAND TOTAL**");
		grandTotalReportItem.setNoOfTickets(0);
		grandTotalReportItem.setVat1(0.0);
		grandTotalReportItem.setVat2(0.0);
		grandTotalReportItem.setSubamount1(0.0);
		grandTotalReportItem.setSubamount2(0.0);
		grandTotalReportItem.setSubamountSvcTax(0.0);
		grandTotalReportItem.setSubAmountTotal(0.0);
		grandTotalReportItem.setServiceTax(0.0);
		grandTotalReportItem.setSubamountSvcTax(0.0);
		grandTotalReportItem.setVatTax(0.0);
		grandTotalReportItem.setTotal(0.0);

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = c.getTime();
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);

			date1 = d3;
			if (tickets.size() > 0) {
				TaxWiseReportItem reportItem = new TaxWiseReportItem();
				reportItem.setDate(tickets.get(0).getCreateDateDayFormatted());
				reportItem.setNoOfTickets(tickets.size());
				reportItem.setVat1(0.0);
				reportItem.setVat2(0.0);
				reportItem.setSubamount1(0.0);
				reportItem.setSubamount2(0.0);
				reportItem.setSubamountSvcTax(0.0);
				reportItem.setSubAmountTotal(0.0);
				reportItem.setServiceTax(0.0);
				reportItem.setSubamountSvcTax(0.0);
				reportItem.setVatTax(0.0);
				reportItem.setTotal(0.0);
				grandTotalReportItem.setNoOfTickets(grandTotalReportItem.getNoOfTickets() + tickets.size());

				for (Ticket ticket : tickets) {
					List<TicketItem> ticketItems = ticket.getTicketItems();

					if (ticketItems == null)
						continue;
					reportItem.setSubAmountTotal(reportItem.getSubAmountTotal() + ticket.getSubtotalAmount());
					reportItem.setTotal(reportItem.getTotal() + ticket.getTotalAmount());
					grandTotalReportItem.setSubAmountTotal(grandTotalReportItem.getSubAmountTotal() + ticket.getSubtotalAmount());
					grandTotalReportItem.setTotal(grandTotalReportItem.getTotal() + ticket.getTotalAmount());

					double serviceTax = 0.0;
					double vatTax = 0.0;

					double vatTax1 = 0.0;
					double vatTax2 = 0.0;
					double subamount1 = 0.0;
					double subamount2 = 0.0;
					double submountSvc = 0.0;

					Map<String, Double> taxMap = ticket.getTicketTaxDetails();
					for (Map.Entry<String, Double> entry : taxMap.entrySet()) {
						if (entry.getKey().toLowerCase().contains("service-6")) {
							serviceTax += entry.getValue();
							submountSvc += entry.getValue() * 100 / 6;
						} else if (entry.getKey().toLowerCase().contains("vat-12.5")) {
							vatTax1 += entry.getValue();
							vatTax += entry.getValue();
							subamount1 += entry.getValue() * 100 / 12.5;
						} else if (entry.getKey().toLowerCase().contains("vat-20")) {
							vatTax2 += entry.getValue();
							vatTax += entry.getValue();
							subamount2 += entry.getValue() * 100 / 20;
						}
					}
					reportItem.setVat1(reportItem.getVat1() + vatTax1);
					reportItem.setVat2(reportItem.getVat2() + vatTax2);
					grandTotalReportItem.setVat1(grandTotalReportItem.getVat1() + vatTax1);
					grandTotalReportItem.setVat2(grandTotalReportItem.getVat2() + vatTax2);
					
					reportItem.setSubamountSvcTax(reportItem.getSubamountSvcTax() + submountSvc);
					grandTotalReportItem.setSubamountSvcTax(grandTotalReportItem.getSubamountSvcTax() + submountSvc);
					
					reportItem.setSubamount1(reportItem.getSubamount1() + subamount1);
					reportItem.setSubamount2(reportItem.getSubamount2() + subamount2);
					grandTotalReportItem.setSubamount1(grandTotalReportItem.getSubamount1() + subamount1);
					grandTotalReportItem.setSubamount2(grandTotalReportItem.getSubamount2() + subamount2);
					
					reportItem.setServiceTax(reportItem.getServiceTax() + serviceTax);
					reportItem.setVatTax(reportItem.getVatTax() + vatTax);
					
					grandTotalReportItem.setServiceTax(grandTotalReportItem.getServiceTax() + serviceTax);
					grandTotalReportItem.setVatTax(grandTotalReportItem.getVatTax() + vatTax);
					ticket = null;
				}
				itemList.add(reportItem);
			}
		}
		itemList.add(grandTotalReportItem);
		itemReportModel = new TaxWiseReportModel();
		itemReportModel.setItems(itemList);
	}
}
