package com.ororeport.dayWiseReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
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
	private final static String USER_REPORT_DIR = "/com/ororeport/dayWiseReport/template/";

	public DayWiseReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		DayWiseReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("daywise_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Day Wise Consolidated Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("itemReport", itemReport);

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
		grandTotalReportItem.setCashTrans(0.0);
		grandTotalReportItem.setCardTrans(0.0);
		grandTotalReportItem.setTotalAmount(0.0);
		grandTotalReportItem.setHomeDelivery(0);
		grandTotalReportItem.setTakeOut(0);

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = c.getTime();
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);

			date1 = d3;
			if (tickets.size() > 0) {
				DayWiseReportItem reportItem = new DayWiseReportItem();
				reportItem.setDate(tickets.get(0).getCreateDateDayFormatted());
				reportItem.setBasePrice(0.0);
				reportItem.setDiscount(0.0);
				reportItem.setNoOfTickets(tickets.size());
				reportItem.setServiceTax(0.0);
				reportItem.setVatTax(0.0);
				reportItem.setTotalAmount(0.0);
				reportItem.setCashTrans(0.0);
				reportItem.setCardTrans(0.0);
				reportItem.setTakeOut(0);
				reportItem.setHomeDelivery(0);
				grandTotalReportItem.setNoOfTickets(grandTotalReportItem.getNoOfTickets() + tickets.size());

				for (Ticket ticket : tickets) {
					Set<PosTransaction> tansactions = ticket.getTransactions();

					double cashAmount = 0.0;
					double cardAmount = 0.0;
					for (PosTransaction trans : tansactions) {
						if (trans.getPaymentType().equalsIgnoreCase(PaymentType.CASH.name())) {
							cashAmount += trans.getAmount();
						} else if (trans.getPaymentType().equalsIgnoreCase(PaymentType.CARD.name())) {
							cardAmount += trans.getAmount();
						} else {
							System.out.println("some other trans: " + trans.getAmount() + " and date" + ticket.getCreateDateFormatted());
						}
					}
					reportItem.setCashTrans(reportItem.getCashTrans() + cashAmount);
					reportItem.setCardTrans(reportItem.getCardTrans() + cardAmount);
					grandTotalReportItem.setCashTrans(grandTotalReportItem.getCashTrans() + cashAmount);
					grandTotalReportItem.setCardTrans(grandTotalReportItem.getCardTrans() + cardAmount);

					if (ticket.getTicketType().contains(OrderType.TAKE_OUT.name())) {
						reportItem.setTakeOut(reportItem.getTakeOut() + 1);
						grandTotalReportItem.setTakeOut(grandTotalReportItem.getTakeOut() + 1);
					} else if (ticket.getTicketType().contains(OrderType.HOME_DELIVERY.name())) {
						reportItem.setHomeDelivery(reportItem.getHomeDelivery() + 1);
						grandTotalReportItem.setHomeDelivery(grandTotalReportItem.getHomeDelivery() + 1);
					}
					List<TicketItem> ticketItems = ticket.getTicketItems();

					if (ticketItems == null)
						continue;
					reportItem.setBasePrice(reportItem.getBasePrice() + ticket.getSubtotalAmount());
					reportItem.setDiscount(reportItem.getDiscount() + ticket.getDiscountAmount());
					reportItem.setTotalAmount(reportItem.getTotalAmount() + ticket.getTotalAmount());
					grandTotalReportItem.setBasePrice(grandTotalReportItem.getBasePrice() + ticket.getSubtotalAmount());
					grandTotalReportItem.setDiscount(grandTotalReportItem.getDiscount() + ticket.getDiscountAmount());
					grandTotalReportItem.setTotalAmount(grandTotalReportItem.getTotalAmount() + ticket.getTotalAmount());

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
