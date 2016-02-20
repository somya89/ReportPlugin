package com.ororeport.cardPaymentReport;

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
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.TicketUtils;

/**
 * @author SOMYA
 * 
 */
public class CardPaymentReport extends Report {
	private CartPaymentReportModel itemReportModel;
	private CartPaymentReportModel modifierReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/cardPaymentReport/template/";

	public CardPaymentReport() {
		super("CardPaymentReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		CartPaymentReportModel itemReportModel = this.itemReportModel;
		CartPaymentReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("card_report", USER_REPORT_DIR, this.getClass());
		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Card Payment Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatFullDate(getStartDate()) + " to " + ReportService.formatFullDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
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
		List<CardPaymentReportItem> itemList = new ArrayList<CardPaymentReportItem>();

		CardPaymentReportItem grandTotalReportItem = new CardPaymentReportItem();
		grandTotalReportItem.setDate(null);
		grandTotalReportItem.setTicketId("* GRAND TOTAL*");
		grandTotalReportItem.setTime(null);
		grandTotalReportItem.setOrderType(null);
		grandTotalReportItem.setCardAmount(0.0);
		grandTotalReportItem.setCashAmount(0.0);
		grandTotalReportItem.setSubTotalAmount(null);
		grandTotalReportItem.setPartial(null);
		grandTotalReportItem.setTotalAmount(0.0);

		String ticketDate = null;
		String ticketTime = null;
		int count = 0;
		for (Ticket ticket : tickets) {
			ticketDate = ticket.getCreateDateFormatted();
			ticketTime = ticket.getCreateTime24HrFormatted();

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

			boolean isPartial = false;
			if (ticket.getPaymentMode().contains("Both")) {
				isPartial = true;
			}

			String orderType = null;
			if (ticket.getTicketType().contains(OrderType.TAKE_OUT.name())) {
				orderType = "TakeOut";
			} else if (ticket.getTicketType().contains(OrderType.HOME_DELIVERY.name())) {
				orderType = "Delivery";
			} else if (ticket.getTicketType().contains(OrderType.DINE_IN.name())) {
				orderType = "Dine in";
			}

			if (ticket.getPaymentMode().contains(PaymentType.CARD.name()) || ticket.getPaymentMode().contains("Both")) {
				CardPaymentReportItem reportItem = new CardPaymentReportItem();
				reportItem.setDate(ticketDate);
				reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
				reportItem.setTime(ticketTime);
				reportItem.setOrderType(orderType);
				reportItem.setCardAmount(cardAmount);
				reportItem.setCashAmount(cashAmount);
				reportItem.setSubTotalAmount(ticket.getSubtotalAmount());
				reportItem.setPartial(isPartial);
				reportItem.setTotalAmount(ticket.getTotalAmount());
				itemList.add(reportItem);
				count++;
				grandTotalReportItem.setCashAmount(grandTotalReportItem.getCashAmount() + cashAmount);
				grandTotalReportItem.setCardAmount(grandTotalReportItem.getCardAmount() + cardAmount);
				grandTotalReportItem.setTotalAmount(grandTotalReportItem.getTotalAmount() + ticket.getTotalAmount());
			}
		}
		grandTotalReportItem.setOrderType(String.valueOf(count));

		itemList.add(grandTotalReportItem);
		itemReportModel = new CartPaymentReportModel();
		itemReportModel.setItems(itemList);
	}
}
