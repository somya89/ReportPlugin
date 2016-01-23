package com.ororeport.menuItemDetailReport;

import java.text.SimpleDateFormat;
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
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;

public class MenuItemDetailedReport extends Report {
	private MenuItemDetailReportModel itemReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/menuItemDetailReport/template/";

	public MenuItemDetailedReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		MenuItemDetailReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("menu_item_sub_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Menu Item Detail Report");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
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

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());
		List<MenuItemDetailReportItem> itemList = new ArrayList<MenuItemDetailReportItem>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = c.getTime();
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);
			HashMap<String, MenuItemDetail> menuItemMap = new HashMap<String, MenuItemDetail>();
			
			for (int i = 0; i < tickets.size(); i++) {
				Ticket t = TicketDAO.getInstance().loadFullTicket(tickets.get(i).getId());

				List<TicketItem> items = t.getTicketItems();
				for (TicketItem a : items) {
					String menuItemName = a.getName().trim();
					if (!menuItemMap.containsKey(menuItemName)) {
						MenuItemDetail m1 = new MenuItemDetail(1, a.getDiscountAmount(), a.getTaxAmount(), a.getSubtotalAmount(), a.getTotalAmount());
						menuItemMap.put(menuItemName, m1);
					} else {
						MenuItemDetail m2 = menuItemMap.get(menuItemName);
						m2.add(a);
						menuItemMap.put(menuItemName, m2);
					}
				}
			}
			for (Map.Entry<String, MenuItemDetail> entry : menuItemMap.entrySet()) {
				MenuItemDetail value = entry.getValue();
				MenuItemDetailReportItem mdri = new MenuItemDetailReportItem();
				mdri.setDate(dateFormat.format(date1));
				mdri.setMenuName(entry.getKey());
				mdri.setDiscount(value.getDiscount());
				mdri.setPrice(value.getPrice());
				mdri.setQuantity(value.getQuantity());
				mdri.setTaxAmount(value.getTaxAmount());
				mdri.setTotalAmount(value.getTotalAmount());
				itemList.add(mdri);
			}
			date1 = d3;
		}

		itemReportModel = new MenuItemDetailReportModel();
		itemReportModel.setItems(itemList);
		itemReportModel.calculateGrandTotal();
	}

	class MenuItemDetail {

		private int quantity;
		private double discount;
		private double taxAmount;
		private double price;
		private double totalAmount;

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public double getTaxAmount() {
			return taxAmount;
		}

		public void setTaxAmount(double taxAmount) {
			this.taxAmount = taxAmount;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public void add(MenuItemDetail m) {
			this.quantity += m.quantity;
			this.discount += m.discount;
			this.taxAmount += m.taxAmount;
			this.price += m.price;
			this.totalAmount += m.totalAmount;
		}

		public void add(TicketItem t) {
			this.quantity += t.getItemCount();
			this.discount += t.getDiscountAmount();
			this.taxAmount += t.getTaxAmount();
			this.price += t.getSubtotalAmount();
			this.totalAmount += t.getTotalAmount();
		}

		public MenuItemDetail(int qty, double disc, double taxAmt, double price, double totalAmt) {
			this.quantity = qty;
			this.discount = disc;
			this.taxAmount = taxAmt;
			this.price = price;
			this.totalAmount = totalAmt;
		}

		public MenuItemDetail() {
		}

	}
}
