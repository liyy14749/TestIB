package com.stock.core.util.receiptReq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatestReceiptInfo implements Comparable<LatestReceiptInfo>{
	
	private static final Logger log = LoggerFactory.getLogger(LatestReceiptInfo.class);
	
	private String transaction_id;
	private String original_transaction_id;
	private Long purchase_date_ms;
	private Long expires_date_ms;
	private String expires_date;
	private String product_id;
	private String bid;
	private String is_trial_period;
	private String is_in_intro_offer_period;
	
	public String getExpires_date() {
		return expires_date;
	}
	public void setExpires_date(String expires_date) {
		this.expires_date = expires_date;
	}
	public Long getExpires_date_ms() {
		if(expires_date_ms==null && expires_date!=null){
			try {
				expires_date_ms = Long.parseLong(expires_date);
			} catch (Exception e) {
				log.error(" getExpires_date_ms error " + expires_date);
			}
		}
		return expires_date_ms;
	}
	public void setExpires_date_ms(Long expires_date_ms) {
		this.expires_date_ms = expires_date_ms;
	}
	public Long getPurchase_date_ms() {
		return purchase_date_ms;
	}
	public void setPurchase_date_ms(Long purchase_date_ms) {
		this.purchase_date_ms = purchase_date_ms;
	}
	public String getIs_trial_period() {
		return is_trial_period;
	}
	public void setIs_trial_period(String is_trial_period) {
		this.is_trial_period = is_trial_period;
	}
	public String getIs_in_intro_offer_period() {
		return is_in_intro_offer_period;
	}
	public void setIs_in_intro_offer_period(String is_in_intro_offer_period) {
		this.is_in_intro_offer_period = is_in_intro_offer_period;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOriginal_transaction_id() {
		return original_transaction_id;
	}
	public void setOriginal_transaction_id(String original_transaction_id) {
		this.original_transaction_id = original_transaction_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	@Override
	public int compareTo(LatestReceiptInfo o) {
		return o.getExpires_date_ms().compareTo(this.getExpires_date_ms());
	}
	@Override
	public String toString() {
		return "LatestReceiptInfo [transaction_id=" + transaction_id + ", original_transaction_id="
				+ original_transaction_id + ", purchase_date_ms=" + purchase_date_ms + ", expires_date_ms="
				+ expires_date_ms + ", product_id=" + product_id + ", bid=" + bid + ", is_trial_period="
				+ is_trial_period + ", is_in_intro_offer_period=" + is_in_intro_offer_period + "]";
	}
	
}
