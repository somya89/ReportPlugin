package com.ororeport.groupDetailReport;

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

public class GroupDetailedReport extends Report {
	private GroupDetailReportModel itemReportModel;
	private final static String USER_REPORT_DIR = "/com/ororeport/groupDetailReport/template/";

	public GroupDetailedReport() {
		super("MenuGroupReport");
		setDailyReport(true);
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		createModels(startDate, endDate);

		GroupDetailReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport = ReportUtil.getReport("group_report", USER_REPORT_DIR, this.getClass());

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportType", "Menu Group Detail Report");
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
		List<GroupDetailReportItem> itemList = new ArrayList<GroupDetailReportItem>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

		GroupDetailReportItem grandTotalReportItem = new GroupDetailReportItem();
		grandTotalReportItem.setDate(null);
		grandTotalReportItem.setCategoryName("** GRAND TOTAL**");
		grandTotalReportItem.setGroupName("-------------------->>>>>");
		grandTotalReportItem.setDiscount(0.0);
		grandTotalReportItem.setPrice(0.0);
		grandTotalReportItem.setQuantity(0);
		grandTotalReportItem.setVatTax(0.0);
		grandTotalReportItem.setSvcTax(0.0);
		grandTotalReportItem.setTotalAmount(0.0);

		for (; date1.after(date2) == false;) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.DATE, 1); // number of days to add
			Date d3 = c.getTime();
			List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, d3);
			HashMap<String, GroupDetail> groupMap = new HashMap<String, GroupDetail>();
			HashMap<String, Set<String>> categoryMap = new HashMap<String, Set<String>>();

			for (int i = 0; i < tickets.size(); i++) {
				Ticket t = tickets.get(i);
				List<TicketItem> items = t.getTicketItems();
				for (TicketItem a : items) {
					String groupName = a.getGroupName().trim();
					String catName = a.getCategoryName().trim();
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
						GroupDetail m1 = new GroupDetail(a.getItemCount(), a.getDiscountAmount(), a.getVatTaxAmount(), a.getServiceTaxAmount(), a.getSubtotalAmount(), a.getTotalAmount());
						groupMap.put(groupName, m1);
					} else {
						GroupDetail m2 = groupMap.get(groupName);
						m2.add(a);
						groupMap.put(groupName, m2);
					}
				}
			}

			for (Map.Entry<String, Set<String>> entryCat : categoryMap.entrySet()) {
				Set<String> grpSet = entryCat.getValue();
				GroupDetailReportItem gdri = new GroupDetailReportItem();
				gdri.setCategoryName(entryCat.getKey());
				gdri.setDate(dateFormat.format(date1));
				gdri.setGroupName("----------------------------");
				gdri.setDiscount(0.0);
				gdri.setPrice(0.0);
				gdri.setQuantity(0);
				gdri.setVatTax(0.0);
				gdri.setSvcTax(0.0);
				gdri.setTotalAmount(0.0);
				itemList.add(gdri);

				for (String grpName : grpSet) {
					if (groupMap.containsKey(grpName)) {
						GroupDetail value = groupMap.get(grpName);
						GroupDetailReportItem cdri = new GroupDetailReportItem();
						cdri.setCategoryName(null);
						cdri.setDate(null);
						cdri.setGroupName(ReportUtil.toCamelCase(grpName));
						cdri.setDiscount(value.getDiscount());
						cdri.setPrice(value.getPrice());
						cdri.setQuantity(value.getQuantity());
						cdri.setVatTax(value.getVatTaxAmount());
						cdri.setSvcTax(value.getSvcTaxAmount());
						cdri.setTotalAmount(value.getTotalAmount());

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

						itemList.add(cdri);
					}
				}
			}
			date1 = d3;

		}
		itemList.add(grandTotalReportItem);
		itemReportModel = new GroupDetailReportModel();
		itemReportModel.setItems(itemList);
	}

	class GroupDetail {

		private int quantity;
		private double discount;
		private double vatTaxAmount;
		private double svcTaxAmount;
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

		public void add(GroupDetail m) {
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

		public GroupDetail(int qty, double disc, double vatTax, double svcTax, double price, double totalAmt) {
			this.quantity = qty;
			this.discount = disc;
			this.vatTaxAmount += vatTax;
			this.svcTaxAmount += svcTax;
			this.price = price;
			this.totalAmount = totalAmt;
		}

		public GroupDetail() {
		}

	}
}
