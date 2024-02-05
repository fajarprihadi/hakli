package com.sds.hakli.pojo;

public class BIFastStatusResp {
	
	private String responseCode;
	private String responseMessage;
	private String originalPartnerReferenceNo;
	private String originalReferenceNo;
	private String serviceCode;
	private String transactionDate;
	private BIFastAmount amount;
	private String referenceNumber;
	private String sourceAccountNo;
	private String beneficiaryAccountNo;
	private String remark;
	private String latestTransactionStatus;
	private String transactionStatusDesc;
	private BIFastAdditionalInfo additionalInfo;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getOriginalPartnerReferenceNo() {
		return originalPartnerReferenceNo;
	}
	public void setOriginalPartnerReferenceNo(String originalPartnerReferenceNo) {
		this.originalPartnerReferenceNo = originalPartnerReferenceNo;
	}
	public String getOriginalReferenceNo() {
		return originalReferenceNo;
	}
	public void setOriginalReferenceNo(String originalReferenceNo) {
		this.originalReferenceNo = originalReferenceNo;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public BIFastAmount getAmount() {
		return amount;
	}
	public void setAmount(BIFastAmount amount) {
		this.amount = amount;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getSourceAccountNo() {
		return sourceAccountNo;
	}
	public void setSourceAccountNo(String sourceAccountNo) {
		this.sourceAccountNo = sourceAccountNo;
	}
	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}
	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLatestTransactionStatus() {
		return latestTransactionStatus;
	}
	public void setLatestTransactionStatus(String latestTransactionStatus) {
		this.latestTransactionStatus = latestTransactionStatus;
	}
	public String getTransactionStatusDesc() {
		return transactionStatusDesc;
	}
	public void setTransactionStatusDesc(String transactionStatusDesc) {
		this.transactionStatusDesc = transactionStatusDesc;
	}
	public BIFastAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(BIFastAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	@Override
	public String toString() {
		return "BIFastStatusResp [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", originalPartnerReferenceNo=" + originalPartnerReferenceNo + ", originalReferenceNo="
				+ originalReferenceNo + ", serviceCode=" + serviceCode + ", transactionDate=" + transactionDate
				+ ", amount=" + amount + ", referenceNumber=" + referenceNumber + ", sourceAccountNo=" + sourceAccountNo
				+ ", beneficiaryAccountNo=" + beneficiaryAccountNo + ", remark=" + remark + ", latestTransactionStatus="
				+ latestTransactionStatus + ", transactionStatusDesc=" + transactionStatusDesc + ", additionalInfo="
				+ additionalInfo + "]";
	}
	
}
