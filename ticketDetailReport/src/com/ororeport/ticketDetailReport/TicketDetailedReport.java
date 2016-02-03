package com.ororeport.ticketDetailReport;

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
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.TicketUtils;

/**
 * @author SOMYA
 * 
 */
public class TicketDetailedReport extends Report {
	private TicketDetailReportModel itemReportModel;
	private TicketDetailReportModel modifierReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/ticketDetailReport/template/";

	public TicketDetailedReport() {
		super("TicketDetailReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		TicketDetailReportModel itemReportModel = this.itemReportModel;
		TicketDetailReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("ticket_report", USER_REPORT_DIR, this.getClass());
		JasperReport modifierReport = ReportUtil.getReport("ticket_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Ticket Detail Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatFullDate(getStartDate()) + " to " + ReportService.formatFullDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);

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
		HashMap<String, TicketDetailReportItem> itemMap = new HashMap<String, TicketDetailReportItem>();
		HashMap<String, TicketDetailReportItem> modifierMap = new HashMap<String, TicketDetailReportItem>();
		List<TicketDetailReportItem> itemList = new ArrayList<TicketDetailReportItem>();
		List<TicketDetailReportItem> modifierList = new ArrayList<TicketDetailReportItem>();

		String startDate = null;
		String ticketDate = null;
		for (Ticket t : tickets) {

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
			ticketDate = ticket.getCreateDateFormatted();
			if (!ticketDate.equals(startDate)) {
				TicketDetailReportItem reportItem = new TicketDetailReportItem();
				reportItem.setDate(ticketDate);
				reportItem.setOrderType(null);
				reportItem.setTicketId(null);
				reportItem.setDiscount(null);
				reportItem.setBasePrice(null);
				reportItem.setName(null);
				reportItem.setQuantity(null);
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
			boolean first = true;
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
				TicketDetailReportItem reportItem = itemMap.get(key);
				if (reportItem == null) {
					reportItem = new TicketDetailReportItem();
					if (first) {
						reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
						reportItem.setOrderType(orderType);
						reportItem.setCardAmount(cardAmount);
						reportItem.setCashAmount(cashAmount);
						reportItem.setSubTotalAmount(ticketSubAmount);
						first = false;
					} else {
						reportItem.setTicketId(null);
					}
					reportItem.setDate(null);
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setVatTax(ticketItem.getVatTaxAmount());
					reportItem.setSvcTax(ticketItem.getServiceTaxAmount());
					reportItem.setDiscount(ticketItem.getDiscountAmount());
					reportItem.setQuantity(ticketItem.getItemCount());
					reportItem.setTotalAmount(ticketItem.getTotalAmount());
					itemList.add(reportItem);
					// itemMap.put(key, reportItem);
				}

				if (ticketItem.isHasModifiers() && ticketItem.getTicketItemModifierGroups() != null) {
					List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();

					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						List<TicketItemModifier> modifiers = ticketItemModifierGroup.getTicketItemModifiers();
						for (TicketItemModifier modifier : modifiers) {
							if (modifier.getItemId() == null) {
								key = modifier.getName();
							} else {
								key = modifier.getItemId().toString();
							}
							TicketDetailReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new TicketDetailReportItem();
								modifierReportItem.setPrice(modifier.getUnitPrice());
								modifierReportItem.setName(modifier.getName());
								// modifierReportItem.setTaxRate(modifier.getTaxRate());
								modifierList.add(modifierReportItem);

								// modifierMap.put(key, modifierReportItem);
							}
							modifierReportItem.setQuantity(modifierReportItem.getQuantity() + 1);
							// modifierReportItem.setTotal(modifierReportItem.getTotal()
							// + modifier.getTotal());
						}
					}
				}
			}
			ticket = null;
		}
		itemReportModel = new TicketDetailReportModel();
		itemReportModel.setItems(itemList);
		// itemReportModel.calculateGrandTotal();

		modifierReportModel = new TicketDetailReportModel();
		modifierReportModel.setItems(modifierList);
		// modifierReportModel.calculateGrandTotal();
	}
}
