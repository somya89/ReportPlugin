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
		map.put("reportType", "Ticket Sales Daily");
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
		List<TicketWiseReportItem> itemList = new ArrayList<TicketWiseReportItem>();

		TicketWiseReportItem gdri = new TicketWiseReportItem();
		gdri.setDate("*TOTAL*");
		gdri.setCustomer(null);
		gdri.setTicketId(null);
		gdri.setTime(null);
		gdri.setOrderType(null);
		gdri.setCardAmount(0.0);
		gdri.setCashAmount(0.0);
		gdri.setSubTotalAmount(0.0);
		gdri.setVatTax(0.0);
		gdri.setSvcTax(0.0);
		gdri.setDiscount(0.0);
		gdri.setTotalAmount(0.0);

		for (Ticket t : tickets) {
			TicketWiseReportItem reportItem = new TicketWiseReportItem();

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
			reportItem.setDate(ticket.getCreateDateFormatted());
			reportItem.setTime(ticket.getCreateTimeFormatted());
			reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
			
			reportItem.setVatTax(0.0);
			reportItem.setSvcTax(0.0);
			reportItem.setDiscount(0.0);
			reportItem.setTotalAmount(0.0);
			reportItem.setCardAmount(0.0);
			reportItem.setCashAmount(0.0);
			
			reportItem.setSubTotalAmount(ticket.getSubtotalAmount());
			gdri.setSubTotalAmount(gdri.getSubTotalAmount() + ticket.getSubtotalAmount());

			String orderType = null;
			if (ticket.getTicketType().contains(OrderType.TAKE_OUT.name())) {
				orderType = "TakeOut";
			} else if (ticket.getTicketType().contains(OrderType.HOME_DELIVERY.name())) {
				orderType = "Delivery";
			} else if (ticket.getTicketType().contains(OrderType.DINE_IN.name())) {
				orderType = "Dine in";
			}
			reportItem.setOrderType(orderType);

			String customer = ticket.getCustomer() != null ? ticket.getCustomer().getName() : "-";
			reportItem.setCustomer(customer);

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
			reportItem.setCardAmount(reportItem.getCardAmount() + cardAmount);
			reportItem.setCashAmount(reportItem.getCashAmount() + cashAmount);
			gdri.setCashAmount(gdri.getCashAmount() + cashAmount);
			gdri.setCardAmount(gdri.getCardAmount() + cardAmount);

			for (TicketItem ticketItem : ticketItems) {
				reportItem.setVatTax(reportItem.getVatTax() + ticketItem.getVatTaxAmount());
				reportItem.setSvcTax(reportItem.getSvcTax() + ticketItem.getServiceTaxAmount());
				reportItem.setDiscount(reportItem.getDiscount() + ticketItem.getDiscountAmount());
				reportItem.setTotalAmount(reportItem.getTotalAmount() + ticketItem.getTotalAmount());

				gdri.setVatTax(gdri.getVatTax() + ticketItem.getVatTaxAmount());
				gdri.setSvcTax(gdri.getSvcTax() + ticketItem.getServiceTaxAmount());
				gdri.setDiscount(gdri.getDiscount() + ticketItem.getDiscountAmount());
				gdri.setTotalAmount(gdri.getTotalAmount() + ticketItem.getTotalAmount());
			}
			itemList.add(reportItem);
		}
		itemList.add(gdri);
		itemReportModel = new TicketWiseReportModel();
		itemReportModel.setItems(itemList);
	}
}
