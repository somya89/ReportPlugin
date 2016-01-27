package com.ororeport.menuItemDetailReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

public class MenuItemDetailReportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Date", "Menu Item Name", "Base_Price", "Qty", "Amount", "Discount", "VAT", "SVC_Tax", "Total Amount" };
	private List<MenuItemDetailReportItem> items;
	private double grandTotal;

	public MenuItemDetailReportModel() {
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
		MenuItemDetailReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate() != null ? item.getDate() : "";
		case 1:
			return item.getMenuName();
		case 2:
			return item.getBasePrice() != null ? formatter.format(item.getBasePrice()) : "";
		case 3:
			return item.getQuantity() != null ? String.valueOf(item.getQuantity()) : "";
		case 4:
			return formatter.format(item.getPrice());
		case 5:
			return formatter.format(item.getDiscount());
		case 6:
			return formatter.format(item.getVatTax());
		case 7:
			return formatter.format(item.getSvcTax());
		case 8:
			return formatter.format(item.getTotalAmount());
		}

		return null;
	}

	public List<MenuItemDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItemDetailReportItem> items) {
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

		for (MenuItemDetailReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
