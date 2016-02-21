package com.ororeport.menuItemDetailReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;

public class MenuItemDetailedReport extends Report {
	private MenuItemDetailReportModel itemReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/menuItemDetailReport/template/";

	public MenuItemDetailedReport() {
		super("MenuItemDetailReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		MenuItemDetailReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("menu_item_sub_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Menu Item Wise Sales Daily");
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

	public void createModels(Date date1, Date date2) {
		List<MenuItemDetailReportItem> itemList = new ArrayList<MenuItemDetailReportItem>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

		MenuItemDetailReportItem grandTotalReportItem = new MenuItemDetailReportItem();
		grandTotalReportItem.setDate("");
		grandTotalReportItem.setCategoryName("** GRAND TOTAL**");
		grandTotalReportItem.setGroupName(null);
		grandTotalReportItem.setMenuName(null);
		grandTotalReportItem.setBasePrice(null);
		grandTotalReportItem.setDiscount(0.0);
		grandTotalReportItem.setPrice(0.0);
		grandTotalReportItem.setQuantity(0);
		grandTotalReportItem.setVatTax(0.0);
		grandTotalReportItem.setSvcTax(0.0);
		grandTotalReportItem.setTotalAmount(0.0);

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date2);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = date2;
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);
			d3 = c.getTime();
			HashMap<String, MenuItemDetail> menuItemMap = new HashMap<String, MenuItemDetail>();
			HashMap<String, Set<String>> groupMap = new HashMap<String, Set<String>>();
			HashMap<String, Set<String>> categoryMap = new HashMap<String, Set<String>>();

			for (int i = 0; i < tickets.size(); i++) {
				Ticket t = tickets.get(i);
				List<TicketItem> items = t.getTicketItems();
				for (TicketItem a : items) {
					String menuItemName = a.getName().trim();
					if (menuItemName.startsWith("**"))
						continue;
					String groupName = a.getGroupName().trim();
					String catName = a.getCategoryName().trim();

					if (!menuItemMap.containsKey(menuItemName)) {
						MenuItemDetail m1 = new MenuItemDetail(a.getItemCount(), a.getDiscountAmount(), a.getVatTaxAmount(), a.getServiceTaxAmount(), a.getSubtotalAmount(), a.getUnitPriceDisplay(),
								a.getTotalAmount());
						menuItemMap.put(menuItemName, m1);
					} else {
						MenuItemDetail m2 = menuItemMap.get(menuItemName);
						m2.add(a);
						menuItemMap.put(menuItemName, m2);
					}

					if (!categoryMap.containsKey(catName)) {
						Set<String> groupSet1 = new HashSet<String>();
						groupSet1.add(groupName);
						categoryMap.put(catName, groupSet1);
					} else {
						Set<String> groupSet2 = categoryMap.get(catName);
						groupSet2.add(groupName);
						categoryMap.put(catName, groupSet2);
					}

					if (!groupMap.containsKey(groupName)) {
						Set<String> menuItemSet = new HashSet<String>();
						menuItemSet.add(menuItemName);
						groupMap.put(groupName, menuItemSet);
					} else {
						Set<String> menuItemSet1 = groupMap.get(groupName);
						menuItemSet1.add(menuItemName);
						groupMap.put(groupName, menuItemSet1);
					}
				}
			}

			for (Map.Entry<String, Set<String>> entryCat : categoryMap.entrySet()) {
				Set<String> grpSet = entryCat.getValue();
				String catName = entryCat.getKey();
				MenuItemDetailReportItem crdi = new MenuItemDetailReportItem();
				crdi.setDate(dateFormat.format(date1));
				crdi.setCategoryName(catName);
				crdi.setGroupName(null);
				crdi.setMenuName(null);
				crdi.setDiscount(0.0);
				crdi.setBasePrice(null);
				crdi.setPrice(0.0);
				crdi.setQuantity(0);
				crdi.setVatTax(0.0);
				crdi.setSvcTax(0.0);
				crdi.setTotalAmount(0.0);
				itemList.add(crdi);

				for (String groupName : grpSet) {
					if (groupMap.containsKey(groupName)) {
						Set<String> menuSet = groupMap.get(groupName);
						MenuItemDetailReportItem gdri = new MenuItemDetailReportItem();
						gdri.setDate(null);
						gdri.setCategoryName(null);
						gdri.setGroupName(groupName);
						gdri.setMenuName(null);
						gdri.setDiscount(0.0);
						gdri.setPrice(0.0);
						gdri.setBasePrice(null);
						gdri.setQuantity(0);
						gdri.setVatTax(0.0);
						gdri.setSvcTax(0.0);
						gdri.setTotalAmount(0.0);
						itemList.add(gdri);
						for (String menuName : menuSet) {
							if (menuItemMap.containsKey(menuName)) {
								MenuItemDetail value = menuItemMap.get(menuName);
								MenuItemDetailReportItem mdri = new MenuItemDetailReportItem();
								mdri.setCategoryName(null);
								mdri.setGroupName(null);
								mdri.setMenuName(menuName);
								mdri.setDate(null);
								mdri.setDiscount(value.getDiscount());
								mdri.setPrice(value.getPrice());
								mdri.setBasePrice(value.getBasePrice());
								mdri.setQuantity(value.getQuantity());
								mdri.setVatTax(value.getVatTaxAmount());
								mdri.setSvcTax(value.getSvcTaxAmount());
								mdri.setTotalAmount(value.getTotalAmount());

								grandTotalReportItem.setQuantity(grandTotalReportItem.getQuantity() + value.getQuantity());
								grandTotalReportItem.setDiscount(grandTotalReportItem.getDiscount() + value.getDiscount());
								grandTotalReportItem.setPrice(grandTotalReportItem.getPrice() + value.getPrice());
								grandTotalReportItem.setVatTax(grandTotalReportItem.getVatTax() + value.getVatTaxAmount());
								grandTotalReportItem.setSvcTax(grandTotalReportItem.getSvcTax() + value.getSvcTaxAmount());
								grandTotalReportItem.setTotalAmount(grandTotalReportItem.getTotalAmount() + value.getTotalAmount());

								gdri.setQuantity(gdri.getQuantity() + value.getQuantity());
								gdri.setDiscount(gdri.getDiscount() + value.getDiscount());
								gdri.setPrice(gdri.getPrice() + value.getPrice());
								gdri.setVatTax(gdri.getVatTax() + value.getVatTaxAmount());
								gdri.setSvcTax(gdri.getSvcTax() + value.getSvcTaxAmount());
								gdri.setTotalAmount(gdri.getTotalAmount() + value.getTotalAmount());

								crdi.setQuantity(crdi.getQuantity() + value.getQuantity());
								crdi.setDiscount(crdi.getDiscount() + value.getDiscount());
								crdi.setPrice(crdi.getPrice() + value.getPrice());
								crdi.setVatTax(crdi.getVatTax() + value.getVatTaxAmount());
								crdi.setSvcTax(crdi.getSvcTax() + value.getSvcTaxAmount());
								crdi.setTotalAmount(crdi.getTotalAmount() + value.getTotalAmount());

								itemList.add(mdri);
							}
						}
					}
				}
			}
			date1 = d3;
		}
		itemList.add(grandTotalReportItem);
		itemReportModel = new MenuItemDetailReportModel();
		itemReportModel.setItems(itemList);
		itemReportModel.calculateGrandTotal();
	}

	class MenuItemDetail {

		private int quantity;
		private double discount;
		private double vatTaxAmount;
		private double svcTaxAmount;
		private double price;
		private double totalAmount;
		private double basePrice;

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

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getBasePrice() {
			return basePrice;
		}

		public void setBasePrice(double basePrice) {
			this.basePrice = basePrice;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public double getVatTaxAmount() {
			return vatTaxAmount;
		}

		public void setVatTaxAmount(double vatTaxAmount) {
			this.vatTaxAmount = vatTaxAmount;
		}

		public double getSvcTaxAmount() {
			return svcTaxAmount;
		}

		public void setSvcTaxAmount(double svcTaxAmount) {
			this.svcTaxAmount = svcTaxAmount;
		}

		public void add(MenuItemDetail m) {
			this.quantity += m.quantity;
			this.discount += m.discount;
			this.vatTaxAmount += m.vatTaxAmount;
			this.svcTaxAmount += m.svcTaxAmount;
			this.price += m.price;
			this.totalAmount += m.totalAmount;
		}

		public void add(TicketItem t) {
			this.quantity += t.getItemCount();
			this.discount += t.getDiscountAmount();
			this.vatTaxAmount += t.getVatTaxAmount();
			this.svcTaxAmount += t.getServiceTaxAmount();
			this.price += t.getSubtotalAmount();
			this.totalAmount += t.getTotalAmount();
		}

		public MenuItemDetail(int qty, double disc, double vatTax, double svcTax, double price, double basePrice, double totalAmt) {
			this.quantity = qty;
			this.discount = disc;
			this.vatTaxAmount += vatTax;
			this.svcTaxAmount += svcTax;
			this.price = price;
			this.basePrice = basePrice;
			this.totalAmount = totalAmt;
		}

		public MenuItemDetail() {
		}

	}
}
