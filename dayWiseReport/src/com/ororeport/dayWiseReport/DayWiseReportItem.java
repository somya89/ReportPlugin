package com.ororeport.dayWiseReport;

/**
 * @author SOMYA
 * 
 */
public class DayWiseReportItem {
	private String date;
	private int noOfTickets;
	private int takeOut;
	private int homeDelivery;
	private int dineIn;
	private double discount;
	private double basePrice;
	private double totalAmount;
	private double serviceTax;
	private double vatTax;
	private double cashTrans;
	private double cardTrans;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getNoOfTickets() {
		return noOfTickets;
	}

	public void setNoOfTickets(int noOfTickets) {
		this.noOfTickets = noOfTickets;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public double getVatTax() {
		return vatTax;
	}

	public void setVatTax(double vatTax) {
		this.vatTax = vatTax;
	}

	public double getCashTrans() {
		return cashTrans;
	}

	public void setCashTrans(double cashTrans) {
		this.cashTrans = cashTrans;
	}

	public double getCardTrans() {
		return cardTrans;
	}

	public void setCardTrans(double cardTrans) {
		this.cardTrans = cardTrans;
	}

	public int getTakeOut() {
		return takeOut;
	}

	public void setTakeOut(int takeOut) {
		this.takeOut = takeOut;
	}

	public int getHomeDelivery() {
		return homeDelivery;
	}

	public void setHomeDelivery(int homeDelivery) {
		this.homeDelivery = homeDelivery;
	}

	public int getDineIn() {
		return dineIn;
	}

	public void setDineIn(int dineIn) {
		this.dineIn = dineIn;
	}

}
