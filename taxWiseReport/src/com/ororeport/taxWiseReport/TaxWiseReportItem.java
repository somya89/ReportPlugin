package com.ororeport.taxWiseReport;

/**
 * @author SOMYA
 * 
 */
public class TaxWiseReportItem {
	private String date;
	private int noOfTickets;
	private double vat1;
	private double vat2;
	private double subamount1;
	private double subamount2;
	private double subamountSvcTax;
	private double subAmountTotal;
	private double serviceTax;
	private double vatTax;
	private double total;

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

	public double getVat1() {
		return vat1;
	}

	public void setVat1(double vat1) {
		this.vat1 = vat1;
	}

	public double getVat2() {
		return vat2;
	}

	public void setVat2(double vat2) {
		this.vat2 = vat2;
	}

	public double getSubamount1() {
		return subamount1;
	}

	public void setSubamount1(double subamount1) {
		this.subamount1 = subamount1;
	}

	public double getSubamount2() {
		return subamount2;
	}

	public void setSubamount2(double subamount2) {
		this.subamount2 = subamount2;
	}

	public double getSubamountSvcTax() {
		return subamountSvcTax;
	}

	public void setSubamountSvcTax(double subamountSvcTax) {
		this.subamountSvcTax = subamountSvcTax;
	}

	public double getSubAmountTotal() {
		return subAmountTotal;
	}

	public void setSubAmountTotal(double subAmountTotal) {
		this.subAmountTotal = subAmountTotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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

}
