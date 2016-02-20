package com.ororeport.ticketWiseReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TicketWiseReportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");

	private String[] columnNames = { "Ticket", "Date", "Time", "Discount", "Vat", "Svc Tax", "Cash", "Card", "Total_noTax", "Total Amount", "OrderType", "Customer" };
	private List<TicketWiseReportItem> items;

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
		TicketWiseReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getTicketId() != null ? item.getTicketId() : "";
		case 1:
			return item.getDate() != null ? item.getDate() : "";
		case 2:
			return item.getTime() != null ? item.getTime() : "";
		case 3:
			return item.getDiscount() != null ? formatter.format(item.getDiscount()) : "";
		case 4:
			return item.getVatTax() != null ? formatter.format(item.getVatTax()) : "";
		case 5:
			return item.getSvcTax() != null ? formatter.format(item.getSvcTax()) : "";
		case 6:
			return item.getCashAmount() != null ? formatter.format(item.getCashAmount()) : "";
		case 7:
			return item.getCardAmount() != null ? formatter.format(item.getCardAmount()) : "";
		case 8:
			return item.getSubTotalAmount() != null ? formatter.format(item.getSubTotalAmount()) : "";
		case 9:
			return item.getTotalAmount() != null ? formatter.format(item.getTotalAmount()) : "";
		case 10:
			return item.getOrderType() != null ? String.valueOf(item.getOrderType()) : "";
		case 11:
			return item.getCustomer() != null ? String.valueOf(item.getCustomer()) : "";
		}

		return null;
	}

	public List<TicketWiseReportItem> getItems() {
		return items;
	}

	public void setItems(List<TicketWiseReportItem> items) {
		this.items = items;
	}
}
