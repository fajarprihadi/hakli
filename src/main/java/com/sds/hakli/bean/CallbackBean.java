package com.sds.hakli.bean;

public class CallbackBean {

	private String brivaNo;
	private String billAmount; 
	private String transactionDateTime;
	private String journalSeq;
	private String terminalId;
	
	public String getBrivaNo() {
		return brivaNo;
	}
	public void setBrivaNo(String brivaNo) {
		this.brivaNo = brivaNo;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public String getJournalSeq() {
		return journalSeq;
	}
	public void setJournalSeq(String journalSeq) {
		this.journalSeq = journalSeq;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
}
