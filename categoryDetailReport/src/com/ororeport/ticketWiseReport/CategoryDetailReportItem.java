package com.ororeport.ticketWiseReport;

public class CategoryDetailReportItem {
	private String date;
	private String categoryName;
	private double price;
	private double totalAmount;
	private Integer quantity;
	private double vatTax;
	private double svcTax;

	public double getSvcTax() {
		return svcTax;
	}

	public void setSvcTax(double svcTax) {
		this.svcTax = svcTax;
	}

	private double discount;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String catName) {
		this.categoryName = catName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public CategoryDetailReportItem() {
		super();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
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

	public double getVatTax() {
		return vatTax;
	}

	public void setVatTax(double vatTax) {
		this.vatTax = vatTax;
	}

}
