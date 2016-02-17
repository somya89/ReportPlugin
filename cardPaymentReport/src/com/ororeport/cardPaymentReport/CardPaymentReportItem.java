package com.ororeport.cardPaymentReport;

/**
 * @author SOMYA
 * 
 */
public class CardPaymentReportItem {
	private String date;
	private String time;
	private Boolean partial;
	private String ticketId;
	private String orderType;
	private Double cashAmount;
	private Double cardAmount;
	private Double subTotalAmount;
	private Double totalAmount;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public CardPaymentReportItem() {
		super();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public Double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public Double getSubTotalAmount() {
		return subTotalAmount;
	}

	public void setSubTotalAmount(Double subTotalAmount) {
		this.subTotalAmount = subTotalAmount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Boolean isPartialPayment() {
		return partial;
	}

	public void setPartial(Boolean partial) {
		this.partial = partial;
	}

}
