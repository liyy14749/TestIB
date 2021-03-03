package com.stock.core.util.receipt;

public class PendingRenewalInfo {

	private String expiration_intent;
	private Long grace_period_expires_date_ms;
	private String original_transaction_id;
	
	public String getExpiration_intent() {
		return expiration_intent;
	}
	public void setExpiration_intent(String expiration_intent) {
		this.expiration_intent = expiration_intent;
	}
	public Long getGrace_period_expires_date_ms() {
		return grace_period_expires_date_ms;
	}
	public void setGrace_period_expires_date_ms(Long grace_period_expires_date_ms) {
		this.grace_period_expires_date_ms = grace_period_expires_date_ms;
	}
	public String getOriginal_transaction_id() {
		return original_transaction_id;
	}
	public void setOriginal_transaction_id(String original_transaction_id) {
		this.original_transaction_id = original_transaction_id;
	}
}
