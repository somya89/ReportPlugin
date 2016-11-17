package com.ororeport.taxWiseReport;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author SOMYA
 * 
 */
public class TaxWiseReportModel extends AbstractTableModel {
	private static final long serialVersionUID = -330312432844261644L;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");

	private String[] columnNames = { "Date", "Tickets", "Vat@12.5", "Vat@20", "Submount@12.5", "Subamount@20", "Subamount@Svc", "Subamount", "VAT", "SvcTax", "Total" };
	private List<TaxWiseReportItem> items;

	public TaxWiseReportModel() {
		super();
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

	public List<TaxWiseReportItem> getItems() {
		return items;
	}

	public void setItems(List<TaxWiseReportItem> items) {
		this.items = items;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		TaxWiseReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate();
		case 1:
			return String.valueOf(item.getNoOfTickets());
		case 2:
			return formatter.format(item.getVat1());
		case 3:
			return formatter.format(item.getVat2());
		case 4:
			return formatter.format(item.getSubamount1());
		case 5:
			return formatter.format(item.getSubamount2());
		case 6:
			return formatter.format(item.getSubamountSvcTax());
		case 7:
			return formatter.format(item.getSubAmountTotal());
		case 8:
			return formatter.format(item.getVatTax());
		case 9:
			return formatter.format(item.getServiceTax());
		case 10:
			return formatter.format(item.getTotal());
		}
		return null;
	}
}
