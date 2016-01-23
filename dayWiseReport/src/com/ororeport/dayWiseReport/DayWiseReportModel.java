package com.ororeport.dayWiseReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

/**
 * @author SOMYA
 * 
 */
public class DayWiseReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 4491925433507335878L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Date", "Tickets", "Subamount", "Discount", "Vat", "Service", "Total Amount" };
	private List<DayWiseReportItem> items;

	public DayWiseReportModel() {
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

	public List<DayWiseReportItem> getItems() {
		return items;
	}

	public void setItems(List<DayWiseReportItem> items) {
		this.items = items;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		DayWiseReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate();
		case 1:
			return String.valueOf(item.getNoOfTickets());
		case 2:
			return currencySymbol + " " + formatter.format(item.getBasePrice());
		case 3:
			return currencySymbol + " " + formatter.format(item.getDiscount());
		case 4:
			return currencySymbol + " " + formatter.format(item.getVatTax());
		case 5:
			return currencySymbol + " " + formatter.format(item.getServiceTax());
		case 6:
			return currencySymbol + " " + formatter.format(item.getTotalAmount());
		}
		return null;
	}
}
