package com.ororeport.voidDetailReport;

import java.util.List;

import com.floreantpos.model.TaxTreatment;

/**
 * @author SOMYA
 *
 */
public class VoidDetailReportItem {
	private String date;
	private String id;
	private String name;
	private Double sellPrice;
	private Double buyPrice;
	private Double profit;
	private Integer quantity;
	private List<TaxTreatment> taxList;
	private Double discount;
	
	private String ticketId;
	private Double taxAmount;
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

	public VoidDetailReportItem() {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
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

	public List<TaxTreatment> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxTreatment> taxList) {
		this.taxList = taxList;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

}
