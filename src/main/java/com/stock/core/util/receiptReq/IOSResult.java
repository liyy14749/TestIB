package com.stock.core.util.receiptReq;

import java.util.List;

public class IOSResult{

	private IOSReceipt receipt;
	private Integer status;
	private String environment;
	private List<LatestReceiptInfo> latest_receipt_info;
	private String latest_receipt;
	private LatestReceiptInfoItem latest_expired_receipt_info;
	
	public LatestReceiptInfoItem getLatest_expired_receipt_info() {
		return latest_expired_receipt_info;
	}
	public void setLatest_expired_receipt_info(LatestReceiptInfoItem latest_expired_receipt_info) {
		this.latest_expired_receipt_info = latest_expired_receipt_info;
	}
	public IOSReceipt getReceipt() {
		return receipt;
	}
	public void setReceipt(IOSReceipt receipt) {
		this.receipt = receipt;
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
	public List<LatestReceiptInfo> getLatest_receipt_info() {
		return latest_receipt_info;
	}
	public void setLatest_receipt_info(List<LatestReceiptInfo> latest_receipt_info) {
		this.latest_receipt_info = latest_receipt_info;
	}
	public String getLatest_receipt() {
		return latest_receipt;
	}
	public void setLatest_receipt(String latest_receipt) {
		this.latest_receipt = latest_receipt;
	}
}
