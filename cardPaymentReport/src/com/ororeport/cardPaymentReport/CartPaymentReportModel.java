package com.ororeport.cardPaymentReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

/**
 * @author SOMYA
 * 
 */
public class CartPaymentReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Ticket", "Date", "Time", "Cash", "Card", "Total_noTax", "Total Amount", "OrderType", "Partial" };
	private List<CardPaymentReportItem> items;
	private double grandTotal;

	public CartPaymentReportModel() {
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
		CardPaymentReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getTicketId() != null ? item.getTicketId() : "";
		case 1:
			return item.getDate() != null ? item.getDate() : "";
		case 2:
			return item.getTime() != null ? item.getTime() : "";
		case 3:
			return item.getCashAmount() != null ? formatter.format(item.getCashAmount()) : "";
		case 4:
			return item.getCardAmount() != null ? formatter.format(item.getCardAmount()) : "";
		case 5:
			return item.getSubTotalAmount() != null ? formatter.format(item.getSubTotalAmount()) : "";
		case 6:
			return item.getTotalAmount() != null ? formatter.format(item.getTotalAmount()) : "";
		case 7:
			return item.getOrderType() != null ? String.valueOf(item.getOrderType()) : "";
		case 8:
			return item.isPartialPayment() != null ? (item.isPartialPayment() ? "T" : "F") : "";
		}

		return null;
	}

	public List<CardPaymentReportItem> getItems() {
		return items;
	}

	public void setItems(List<CardPaymentReportItem> items) {
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
}
