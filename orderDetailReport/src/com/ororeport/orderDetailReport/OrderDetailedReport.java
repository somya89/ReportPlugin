package com.ororeport.orderDetailReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.TicketUtils;

/**
 * @author SOMYA
 *
 */
public class OrderDetailedReport extends Report {
	private OrderDetailReportModel itemReportModel;
	private OrderDetailReportModel modifierReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/menuItemDetailReport/template/";

	public OrderDetailedReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		OrderDetailReportModel itemReportModel = this.itemReportModel;
		OrderDetailReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("menu_item_sub_report", USER_REPORT_DIR, this.getClass());
		JasperReport modifierReport = ReportUtil.getReport("menu_item_sub_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", "============================ Order Detail Report ===============================");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
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

	private void refreshBuyPrice() {
		MenuItemDAO menuItemDAO = new MenuItemDAO();
		List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
		for (MenuItem m : itemList) {
			m.setBuyPrice(getBuyPriceFromInventory(m));
			menuItemDAO.saveOrUpdate(m);
		}
	}

	private static Double getBuyPriceFromInventory(MenuItem menuItem) {
		double buyPrice = 0.0d;
		if (menuItem != null && menuItem.getRecepie() != null) {
			List<RecepieItem> riList = menuItem.getRecepie().getRecepieItems();
			if (riList != null && !riList.isEmpty()) {
				for (RecepieItem ri : riList) {
					if (ri != null && ri.getInventoryItem() != null) {
						Double itemQty = ri.getPercentage();
						buyPrice += ri.getInventoryItem().getAverageRunitPrice() * itemQty;
					}
				}
			}
		}
		return buyPrice;
	}

	/**
	 * 
	 */
	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2);
		refreshBuyPrice();
		HashMap<String, OrderDetailReportItem> itemMap = new HashMap<String, OrderDetailReportItem>();
		HashMap<String, OrderDetailReportItem> modifierMap = new HashMap<String, OrderDetailReportItem>();
		List<OrderDetailReportItem> itemList = new ArrayList<OrderDetailReportItem>();
		List<OrderDetailReportItem> modifierList = new ArrayList<OrderDetailReportItem>();
		for (Ticket t : tickets) {

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());

			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;
			boolean first = true;
			String key = null;
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				} else {
					key = ticketItem.getItemId().toString();
				}
				OrderDetailReportItem reportItem = itemMap.get(key);
				MenuItem mi = MenuItemDAO.getInstance().findByItemId(ticketItem.getItemId());
				if (reportItem == null) {
					reportItem = new OrderDetailReportItem();
					reportItem.setId(key);
					if (first) {
						reportItem.setTicketId(TicketUtils.getTicketNumber(ticket));
						reportItem.setDate(ticket.getCreateDateFormatted());
						first = false;
					} else{
						reportItem.setTicketId(null);
						reportItem.setDate(null);
					}
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setBuyPrice(mi.getBuyPrice());
					reportItem.setProfit(ticketItem.getUnitPrice() - mi.getBuyPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxList(ticketItem.getTaxList());
					reportItem.setTaxAmount(ticketItem.getTaxAmount());
					reportItem.setDiscount(ticketItem.getDiscountAmount());
					reportItem.setQuantity(ticketItem.getItemCount());
					reportItem.setTotalAmount(ticketItem.getTotalAmount());
					itemList.add(reportItem);
//					itemMap.put(key, reportItem);
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
							OrderDetailReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new OrderDetailReportItem();
								modifierReportItem.setId(key);
								modifierReportItem.setPrice(modifier.getUnitPrice());
								modifierReportItem.setName(modifier.getName());
								// modifierReportItem.setTaxRate(modifier.getTaxRate());
								modifierReportItem.setTaxList(modifier.getTaxList());
								modifierList.add(modifierReportItem);

//								modifierMap.put(key, modifierReportItem);
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
		itemReportModel = new OrderDetailReportModel();
		itemReportModel.setItems(itemList);
		itemReportModel.calculateGrandTotal();

		modifierReportModel = new OrderDetailReportModel();
		modifierReportModel.setItems(modifierList);
		modifierReportModel.calculateGrandTotal();
	}
}
