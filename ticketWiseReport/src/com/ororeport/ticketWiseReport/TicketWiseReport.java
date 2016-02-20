package com.ororeport.ticketWiseReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.TicketUtils;

public class TicketWiseReport extends Report {
	private TicketWiseReportModel itemReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/ticketWiseReport/template/";

	public TicketWiseReport() {
		super("TicketWiseReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		TicketWiseReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("ticket_wise_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Ticket Wise Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatFullDate(getStartDate()) + " to " + ReportService.formatFullDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
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

	@Override
	public void createModels(Date date1, Date date2) {
		List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2);
		HashMap<String, TicketWiseReportItem> itemMap = new HashMap<String, TicketWiseReportItem>();
		List<TicketWiseReportItem> itemList = new ArrayList<TicketWiseReportItem>();

		String startDate = null;
		String ticketDate = null;
		String ticketTime = null;
		String customer = null;
		for (Ticket t : tickets) {

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
			ticketDate = ticket.getCreateDateFormatted();
			ticketTime = ticket.getCreateTimeFormatted();
			customer = ticket.getCustomer() != null ? ticket.getCustomer().getName() : "-";
			if (!ticketDate.equals(startDate)) {
				TicketWiseReportItem reportItem = new TicketWiseReportItem();
				reportItem.setDate(ticketDate);
				reportItem.setTime(null);
				reportItem.setOrderType(null);
				reportItem.setCustomer(null);
				reportItem.setTicketId(null);
				reportItem.setDiscount(null);
				reportItem.setVatTax(null);
				reportItem.setSvcTax(null);
				reportItem.setCashAmount(null);
				reportItem.setCardAmount(null);
				reportItem.setSubTotalAmount(null);
				reportItem.setTotalAmount(null);
				startDate = ticketDate;
				itemList.add(reportItem);
			}

			List<TicketItem> ticketItems = ticket.getTicketItems();
			Set<PosTransaction> tansactions = ticket.getTransactions();
			double cashAmount = 0.0;
			double cardAmount = 0.0;
			for (PosTransaction trans : tansactions) {
				if (trans.getPaymentType().equalsIgnoreCase(PaymentType.CASH.name())) {
					cashAmount += trans.getAmount();
				} else if (trans.getPaymentType().equalsIgnoreCase(PaymentType.CARD.name())) {
					cardAmount += trans.getAmount();
				}
			}
			if (ticketItems == null)
				continue;
			String key = null;

			double ticketSubAmount = ticket.getSubtotalAmount();
			String orderType = null;
			if (ticket.getTicketType().contains(OrderType.TAKE_OUT.name())) {
				orderType = "TakeOut";
			} else if (ticket.getTicketType().contains(OrderType.HOME_DELIVERY.name())) {
				orderType = "Delivery";
			} else if (ticket.getTicketType().contains(OrderType.DINE_IN.name())) {
				orderType = "Dine in";
			}
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				} else {
					key = ticketItem.getItemId().toString();
				}
				TicketWiseReportItem reportItem = itemMap.get(key);
				if (reportItem == null) {
					reportItem = new TicketWiseReportItem();
					reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
					reportItem.setTime(ticketTime);
					reportItem.setOrderType(orderType);
					reportItem.setCustomer(customer);
					reportItem.setCardAmount(cardAmount);
					reportItem.setCashAmount(cashAmount);
					reportItem.setSubTotalAmount(ticketSubAmount);
					reportItem.setDate(null);
					reportItem.setVatTax(ticketItem.getVatTaxAmount());
					reportItem.setSvcTax(ticketItem.getServiceTaxAmount());
					reportItem.setDiscount(ticketItem.getDiscountAmount());
					reportItem.setTotalAmount(ticketItem.getTotalAmount());
					itemList.add(reportItem);
				}
				ticket = null;
			}
			itemReportModel = new TicketWiseReportModel();
			itemReportModel.setItems(itemList);
		}

	}
}
