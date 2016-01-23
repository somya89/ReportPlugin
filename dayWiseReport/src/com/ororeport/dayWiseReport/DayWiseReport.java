package com.ororeport.dayWiseReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.main.Application;
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
public class DayWiseReport extends Report {
	private DayWiseReportModel itemReportModel;
	private DayWiseReportModel modifierReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/dayWiseReport/template/";

	public DayWiseReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		DayWiseReportModel itemReportModel = this.itemReportModel;
		DayWiseReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("daywise_report", USER_REPORT_DIR, this.getClass());
		JasperReport modifierReport = ReportUtil.getReport("daywise_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Day Wise Consolidated Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		// map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);

		JasperReport masterReport = ReportUtil.getReport("report_template", USER_REPORT_DIR, this.getClass());

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new JRViewer(print);
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
	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		List<DayWiseReportItem> itemList = new ArrayList<DayWiseReportItem>();

		DayWiseReportItem grandTotalReportItem = new DayWiseReportItem();
		grandTotalReportItem.setDate("** GRAND TOTAL**");
		grandTotalReportItem.setBasePrice(0.0);
		grandTotalReportItem.setDiscount(0.0);
		grandTotalReportItem.setNoOfTickets(0);
		grandTotalReportItem.setServiceTax(0.0);
		grandTotalReportItem.setVatTax(0.0);
		grandTotalReportItem.setTotalAmount(0.0);

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = c.getTime();
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);
			date1 = d3;
			if (tickets.size() > 0) {
				Ticket ticket = TicketDAO.getInstance().loadFullTicket(tickets.get(0).getId());

				DayWiseReportItem reportItem = new DayWiseReportItem();
				reportItem.setDate(ticket.getCreateDateDayFormatted());
				reportItem.setBasePrice(0.0);
				reportItem.setDiscount(0.0);
				reportItem.setNoOfTickets(tickets.size());
				reportItem.setServiceTax(0.0);
				reportItem.setVatTax(0.0);
				reportItem.setTotalAmount(0.0);
				grandTotalReportItem.setNoOfTickets(grandTotalReportItem.getNoOfTickets() + tickets.size());

				for (Ticket t : tickets) {
					ticket = TicketDAO.getInstance().loadFullTicket(t.getId());

					List<TicketItem> ticketItems = ticket.getTicketItems();
					if (ticketItems == null)
						continue;

					for (TicketItem ticketItem : ticketItems) {
						reportItem.setBasePrice(reportItem.getBasePrice() + ticketItem.getSubtotalAmount());
						reportItem.setDiscount(reportItem.getDiscount() + ticketItem.getDiscountAmount());
						grandTotalReportItem.setBasePrice(grandTotalReportItem.getBasePrice() + ticketItem.getSubtotalAmount());
						grandTotalReportItem.setDiscount(grandTotalReportItem.getDiscount() + ticketItem.getDiscountAmount());
						reportItem.setTotalAmount(reportItem.getTotalAmount() + ticketItem.getTotalAmount());
						grandTotalReportItem.setTotalAmount(grandTotalReportItem.getTotalAmount() + ticketItem.getTotalAmount());

					}
					double serviceTax = 0.0;
					double vatTax = 0.0;
					Map<String, Double> taxMap = ticket.getTicketTaxDetails();
					for (Map.Entry<String, Double> entry : taxMap.entrySet()) {
						if (entry.getKey().toLowerCase().contains("service")) {
							serviceTax += entry.getValue();
						} else if (entry.getKey().toLowerCase().contains("vat")) {
							vatTax += entry.getValue();
						}
					}
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
		itemReportModel = new DayWiseReportModel();
		itemReportModel.setItems(itemList);
	}
}
