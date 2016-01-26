package com.ororeport.ticketDetailReport;


/**
 * @author SOMYA
 * 
 */
public class TicketDetailReportItem {
	private String date;
	private String name;
	private Double sellPrice;
	private Double profit;
	private Integer quantity;
	private Double discount;
	private String ticketId;
	private Double vatTax;
	private Double svcTax;
	private Double basePrice;
	private Double totalAmount;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public TicketDetailReportItem() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return sellPrice;
	}

	public void setPrice(Double price) {
		this.sellPrice = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
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

	public Double getSvcTax() {
		return svcTax;
	}

	public void setSvcTax(Double svcTax) {
		this.svcTax = svcTax;
	}

}
