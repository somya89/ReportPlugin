package com.ororeport.menuItemDetailReport;

import java.util.List;

import com.floreantpos.model.TaxTreatment;

public class MenuItemDetailReportItem {
	private String date;
	private String menuName;
	private double taxAmount;
	private double price;
	private double totalAmount;
	private int quantity;
	private List<TaxTreatment> taxList;
	private double discount;

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public MenuItemDetailReportItem() {
		super();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public List<TaxTreatment> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxTreatment> taxList) {
		this.taxList = taxList;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

}
