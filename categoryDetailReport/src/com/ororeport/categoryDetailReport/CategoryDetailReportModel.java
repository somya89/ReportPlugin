package com.ororeport.categoryDetailReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

public class CategoryDetailReportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Date", "Category Name", "Qty", "Amount", "Discount", "VAT", "SVC_Tax", "Total Amount" };
	private List<CategoryDetailReportItem> items;
	private double grandTotal;

	public CategoryDetailReportModel() {
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
		CategoryDetailReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate() != null ? item.getDate() : "";
		case 1:
			return item.getCategoryName();
		case 2:
			return item.getQuantity() != null ? String.valueOf(item.getQuantity()) : "";
		case 3:
			return formatter.format(item.getPrice());
		case 4:
			return formatter.format(item.getDiscount());
		case 5:
			return formatter.format(item.getVatTax());
		case 6:
			return formatter.format(item.getSvcTax());
		case 7:
			return formatter.format(item.getTotalAmount());
		}

		return null;
	}

	public List<CategoryDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<CategoryDetailReportItem> items) {
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

		for (CategoryDetailReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
