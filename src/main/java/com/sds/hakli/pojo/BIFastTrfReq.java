package com.sds.hakli.pojo;

public class BIFastTrfReq {

	private String customerReference;
	private String senderIdentityNumber;
	private String sourceAccountNo;
	private BIFastAmount amount;
	private String beneficiaryBankCode;
	private String beneficiaryAccountNo;
	private String referenceNo;
	private String externalId;
	private String transactionDate;
	private String paymentInfo;
	private String senderType;
	private String senderResidentStatus;
	private String senderTownName;
	private BIFastAdditionalInfo additionalInfo;
	public String getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}
	public String getSenderIdentityNumber() {
		return senderIdentityNumber;
	}
	public void setSenderIdentityNumber(String senderIdentityNumber) {
		this.senderIdentityNumber = senderIdentityNumber;
	}
	public String getSourceAccountNo() {
		return sourceAccountNo;
	}
	public void setSourceAccountNo(String sourceAccountNo) {
		this.sourceAccountNo = sourceAccountNo;
	}
	public BIFastAmount getAmount() {
		return amount;
	}
	public void setAmount(BIFastAmount amount) {
		this.amount = amount;
	}
	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}
	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
	}
	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}
	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public String getSenderType() {
		return senderType;
	}
	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}
	public String getSenderResidentStatus() {
		return senderResidentStatus;
	}
	public void setSenderResidentStatus(String senderResidentStatus) {
		this.senderResidentStatus = senderResidentStatus;
	}
	public String getSenderTownName() {
		return senderTownName;
	}
	public void setSenderTownName(String senderTownName) {
		this.senderTownName = senderTownName;
	}
	public BIFastAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(BIFastAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
}
