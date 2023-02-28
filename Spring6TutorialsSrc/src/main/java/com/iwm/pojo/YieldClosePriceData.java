package com.iwm.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class YieldClosePriceData implements Serializable {

	private Date priceDate;
	private BigDecimal closePrice;
	private BigDecimal quoterlyDividendAmount;
	private BigDecimal yield;
	
	public Date getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}
	public BigDecimal getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(BigDecimal closePrice) {
		this.closePrice = closePrice;
	}
	public BigDecimal getQuoterlyDividendAmount() {
		return quoterlyDividendAmount;
	}
	public void setQuoterlyDividendAmount(BigDecimal quoterlyDividendAmount) {
		this.quoterlyDividendAmount = quoterlyDividendAmount;
	}
	public BigDecimal getYield() {
		return yield;
	}
	public void setYield(BigDecimal yield) {
		this.yield = yield;
	}
	@Override
	public String toString() {
		return "YieldClosePriceData [priceDate=" + priceDate + ", closePrice=" + closePrice
				+ ", quoterlyDividendAmount=" + quoterlyDividendAmount + ", yield=" + yield + "]";
	}

}
