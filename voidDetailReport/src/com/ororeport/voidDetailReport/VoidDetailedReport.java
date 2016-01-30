package com.ororeport.voidDetailReport;

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
public class VoidDetailedReport extends Report {
	private VoidReportModel itemReportModel;
	private VoidReportModel modifierReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/voidDetailReport/template/";

	public VoidDetailedReport() {
		super("VoidTicketDetailReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		VoidReportModel itemReportModel = this.itemReportModel;
		VoidReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("void_report", USER_REPORT_DIR, this.getClass());
		JasperReport modifierReport = ReportUtil.getReport("void_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Void Ticket Detail Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
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

	/**
	 * 
	 */
	public void createModels(Date date1, Date date2) {
		List<Ticket> tickets = TicketDAO.getInstance().findVoidTickets(date1, date2);
		HashMap<String, VoidDetailReportItem> itemMap = new HashMap<String, VoidDetailReportItem>();
		HashMap<String, VoidDetailReportItem> modifierMap = new HashMap<String, VoidDetailReportItem>();
		List<VoidDetailReportItem> itemList = new ArrayList<VoidDetailReportItem>();
		List<VoidDetailReportItem> modifierList = new ArrayList<VoidDetailReportItem>();

		String startDate = null;
		String ticketDate = null;
		for (Ticket t : tickets) {

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
			ticketDate = ticket.getCreateDateFormatted();
			if (!ticketDate.equals(startDate)) {
				VoidDetailReportItem reportItem = new VoidDetailReportItem();
				reportItem.setDate(ticketDate);
				reportItem.setTicketId(null);
				reportItem.setDiscount(null);
				reportItem.setBasePrice(null);
				reportItem.setName(null);
				reportItem.setQuantity(null);
				reportItem.setVatTax(null);
				reportItem.setSvcTax(null);
				reportItem.setCashAmount(null);
				reportItem.setCardAmount(null);
				reportItem.setTotalAmount(null);
				reportItem.setVoidReason(null);
				startDate = ticketDate;
				itemList.add(reportItem);
			}

			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;
			boolean first = true;
			String key = null;
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
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				} else {
					key = ticketItem.getItemId().toString();
				}
				VoidDetailReportItem reportItem = itemMap.get(key);
				if (reportItem == null) {
					reportItem = new VoidDetailReportItem();
					if (first) {
						reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
						reportItem.setCardAmount(cardAmount);
						reportItem.setCashAmount(cashAmount);
						reportItem.setVoidReason(ticket.getVoidReason());
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
							VoidDetailReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new VoidDetailReportItem();
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
		itemReportModel = new VoidReportModel();
		itemReportModel.setItems(itemList);
		// itemReportModel.calculateGrandTotal();

		modifierReportModel = new VoidReportModel();
		modifierReportModel.setItems(modifierList);
		// modifierReportModel.calculateGrandTotal();
	}
}
