package com.ororeport.groupWiseReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

public class GroupWiseReportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Date", "Group Name", "Qty", "Amount", "Discount", "VAT", "SVC_Tax", "Total Amount", "Category Name" };
	private List<GroupWiseReportItem> items;
	private double grandTotal;

	public GroupWiseReportModel() {
		super();
		currencySymbol = Application.getCurrencySymbol();
	}

	public int getRowCount() {
		if (items == null) {
			return 0;
		}

		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		GroupWiseReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate() != null ? item.getDate() : "";
		case 1:
			return item.getGroupName() != null ? String.valueOf(item.getGroupName()) : "";
		case 2:
			return item.getQuantity() != null ? String.valueOf(item.getQuantity()) : "";
		case 3:
			return item.getPrice() != null ? formatter.format(item.getPrice()) : "";
		case 4:
			return item.getDiscount() != null ? formatter.format(item.getDiscount()) : "";
		case 5:
			return item.getVatTax() != null ? formatter.format(item.getVatTax()) : "";
		case 6:
			return item.getSvcTax() != null ? formatter.format(item.getSvcTax()) : "";
		case 7:
			return item.getTotalAmount() != null ? formatter.format(item.getTotalAmount()) : "";
		case 8:
			return item.getCategoryName() != null ? item.getCategoryName() : "";
		}

		return null;
	}

	public List<GroupWiseReportItem> getItems() {
		return items;
	}

	public void setItems(List<GroupWiseReportItem> items) {
		this.items = items;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public String getGrandTotalAsString() {
		return currencySymbol + " " + formatter.format(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		grandTotal = 0;
		if (items == null) {
			return;
		}

		for (GroupWiseReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
