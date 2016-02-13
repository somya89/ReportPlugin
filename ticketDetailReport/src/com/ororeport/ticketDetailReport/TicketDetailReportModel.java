package com.ororeport.ticketDetailReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

/**
 * @author SOMYA
 * 
 */
public class TicketDetailReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Ticket", "Date", "Time", "Name", "Qty", "Base Price", "Discount", "Vat", "Svc Tax", "Cash", "Card", "Total_noTax", "Total Amount", "OrderType" };
	private List<TicketDetailReportItem> items;
	private double grandTotal;

	public TicketDetailReportModel() {
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
		TicketDetailReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getTicketId() != null ? item.getTicketId() : "";
		case 1:
			return item.getDate() != null ? item.getDate() : "";
		case 2:
			return item.getTime() != null ? item.getTime() : "";
		case 3:
			return item.getName() != null ? item.getName() : "";
		case 4:
			return item.getQuantity() != null ? String.valueOf(item.getQuantity()) : "";
		case 5:
			return item.getPrice() != null ? formatter.format(item.getPrice()) : "";
		case 6:
			return item.getDiscount() != null ? formatter.format(item.getDiscount()) : "";
		case 7:
			return item.getVatTax() != null ? formatter.format(item.getVatTax()) : "";
		case 8:
			return item.getSvcTax() != null ? formatter.format(item.getSvcTax()) : "";
		case 9:
			return item.getCashAmount() != null ? formatter.format(item.getCashAmount()) : "";
		case 10:
			return item.getCardAmount() != null ? formatter.format(item.getCardAmount()) : "";
		case 11:
			return item.getSubTotalAmount() != null ? formatter.format(item.getSubTotalAmount()) : "";
		case 12:
			return item.getTotalAmount() != null ? formatter.format(item.getTotalAmount()) : "";
		case 13:
			return item.getOrderType() != null ? String.valueOf(item.getOrderType()) : "";
		}

		return null;
	}

	public List<TicketDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<TicketDetailReportItem> items) {
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

		for (TicketDetailReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
