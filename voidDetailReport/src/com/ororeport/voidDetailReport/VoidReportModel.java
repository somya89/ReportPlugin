package com.ororeport.voidDetailReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

/**
 * @author SOMYA
 * 
 */
public class VoidReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Ticket", "Date", "Name", "Qty", "Base Price", "Discount", "Vat", "Svc Tax", "Total Amount" };
	private List<VoidDetailReportItem> items;
	private double grandTotal;

	public VoidReportModel() {
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
		VoidDetailReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getTicketId() != null ? item.getTicketId() : "";
		case 1:
			return item.getDate() != null ? item.getDate() : "";
		case 2:
			return item.getName() != null ? item.getName() : "";
		case 3:
			return item.getQuantity() != null ? String.valueOf(item.getQuantity()) : "";
		case 4:
			return item.getPrice() != null ? formatter.format(item.getPrice()) : "";
		case 5:
			return item.getDiscount() != null ? formatter.format(item.getDiscount()) : "";
		case 6:
			return item.getVatTax() != null ? formatter.format(item.getVatTax()) : "";
		case 7:
			return item.getSvcTax() != null ? formatter.format(item.getSvcTax()) : "";
		case 8:
			return item.getTotalAmount() != null ? formatter.format(item.getTotalAmount()) : "";
		}

		return null;
	}

	public List<VoidDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<VoidDetailReportItem> items) {
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

		for (VoidDetailReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
