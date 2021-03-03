package com.stock.core.util.receipt;

import java.util.List;

public class UnifiedReceipt {

	private Integer status;
	private String environment;
	private List<LatestReceiptInfoItem> latest_receipt_info;
	private List<PendingRenewalInfo> pending_renewal_info;
	
	public List<PendingRenewalInfo> getPending_renewal_info() {
		return pending_renewal_info;
	}
	public void setPending_renewal_info(List<PendingRenewalInfo> pending_renewal_info) {
		this.pending_renewal_info = pending_renewal_info;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public List<LatestReceiptInfoItem> getLatest_receipt_info() {
		return latest_receipt_info;
	}
	public void setLatest_receipt_info(List<LatestReceiptInfoItem> latest_receipt_info) {
		this.latest_receipt_info = latest_receipt_info;
	}
}
