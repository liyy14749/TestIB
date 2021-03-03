package com.stock.core.util.receipt;

public class IOSResult {

	private IOSReceipt receipt;
	private Integer status;
	private String environment;
	private String notification_type;
	private Long auto_renew_status_change_date_ms;
	private String auto_renew_status_change_date;
	private String auto_renew_product_id;
	private String auto_renew_status;
	private LatestReceiptInfo latest_receipt_info;
	private LatestReceiptInfo latest_expired_receipt_info;
	private UnifiedReceipt unified_receipt;
	private String latest_receipt;
	
	public String getLatest_receipt() {
		return latest_receipt;
	}
	public void setLatest_receipt(String latest_receipt) {
		this.latest_receipt = latest_receipt;
	}
	public LatestReceiptInfo getLatest_expired_receipt_info() {
		return latest_expired_receipt_info;
	}
	public void setLatest_expired_receipt_info(LatestReceiptInfo latest_expired_receipt_info) {
		this.latest_expired_receipt_info = latest_expired_receipt_info;
	}
	public UnifiedReceipt getUnified_receipt() {
		return unified_receipt;
	}
	public void setUnified_receipt(UnifiedReceipt unified_receipt) {
		this.unified_receipt = unified_receipt;
	}
	public Long getAuto_renew_status_change_date_ms() {
		return auto_renew_status_change_date_ms;
	}
	public void setAuto_renew_status_change_date_ms(Long auto_renew_status_change_date_ms) {
		this.auto_renew_status_change_date_ms = auto_renew_status_change_date_ms;
	}
	public String getAuto_renew_status_change_date() {
		return auto_renew_status_change_date;
	}
	public void setAuto_renew_status_change_date(String auto_renew_status_change_date) {
		this.auto_renew_status_change_date = auto_renew_status_change_date;
	}
	public String getAuto_renew_product_id() {
		return auto_renew_product_id;
	}
	public void setAuto_renew_product_id(String auto_renew_product_id) {
		this.auto_renew_product_id = auto_renew_product_id;
	}
	public String getAuto_renew_status() {
		return auto_renew_status;
	}
	public void setAuto_renew_status(String auto_renew_status) {
		this.auto_renew_status = auto_renew_status;
	}
	public String getNotification_type() {
		return notification_type;
	}
	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	public LatestReceiptInfo getLatest_receipt_info() {
		return latest_receipt_info;
	}
	public void setLatest_receipt_info(LatestReceiptInfo latest_receipt_info) {
		this.latest_receipt_info = latest_receipt_info;
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
	@Override
	public String toString() {
		return "IOSResult [receipt=" + receipt + ", status=" + status
				+ ", environment=" + environment + "]";
	}
}
