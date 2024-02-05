package com.sds.hakli.pojo;

public class BIFastTrfResp {
	
	private String responseCode;
	private String responseMessage;
	private String customerReference;
	private String sourceAccountNo;
	private String beneficiaryAccountNo;
	private String beneficiaryBankCode;
	private String referenceNo;
	private String externalId;
	private String journalSequence;
	private String originalReferenceNo;
	private BIFastAmount amount;
	
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
	public String getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
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
	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}
	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
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
	public String getJournalSequence() {
		return journalSequence;
	}
	public void setJournalSequence(String journalSequence) {
		this.journalSequence = journalSequence;
	}
	public String getOriginalReferenceNo() {
		return originalReferenceNo;
	}
	public void setOriginalReferenceNo(String originalReferenceNo) {
		this.originalReferenceNo = originalReferenceNo;
	}
	public BIFastAmount getAmount() {
		return amount;
	}
	public void setAmount(BIFastAmount amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "BIFastTrfResp [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", customerReference=" + customerReference + ", sourceAccountNo=" + sourceAccountNo
				+ ", beneficiaryAccountNo=" + beneficiaryAccountNo + ", beneficiaryBankCode=" + beneficiaryBankCode
				+ ", referenceNo=" + referenceNo + ", externalId=" + externalId + ", journalSequence=" + journalSequence
				+ ", originalReferenceNo=" + originalReferenceNo + ", amount=" + amount + "]";
	}
	
	
}
