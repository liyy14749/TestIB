package com.stock.core.util.receipt;

public class LatestReceiptInfoItem implements Comparable<LatestReceiptInfoItem>{

	private String transaction_id;
	private String original_transaction_id;
	private Long expires_date_ms;
	private Long purchase_date_ms;
	private String product_id;
	private String bid;
	private String is_trial_period;
	private String is_in_intro_offer_period;
	
	public Long getExpires_date_ms() {
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
	public int compareTo(LatestReceiptInfoItem o) {
		if(o.getExpires_date_ms() == null){
			return -1;
		}
		return o.getExpires_date_ms().compareTo(this.getExpires_date_ms());
	}
}
