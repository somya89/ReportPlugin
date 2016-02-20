package com.ororeport.groupDetailReport;

public class GroupDetailReportItem {
	private String date;
	private String groupName;
	private String categoryName;
	private Double price;
	private Double totalAmount;
	private Integer quantity;
	private Double vatTax;
	private Double svcTax;

	public Double getSvcTax() {
		return svcTax;
	}

	public void setSvcTax(Double svcTax) {
		this.svcTax = svcTax;
	}

	private Double discount;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String grpName) {
		this.groupName = grpName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public GroupDetailReportItem() {
		super();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getVatTax() {
		return vatTax;
	}

	public void setVatTax(Double vatTax) {
		this.vatTax = vatTax;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
