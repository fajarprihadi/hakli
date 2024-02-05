package com.sds.hakli.pojo;

public class BIFastInqResp {
	
	private String responseCode;
	private String responseMessage;
	private String referenceNo;
	private String externalId;
	private String registrationId;
	private String receiverName;
	private String beneficiaryAccountNo;
	private String beneficiaryBankCode;
	private String beneficiaryAccountType;
	private String accountNumber;
	private String receiverType;
	private String receiverResidentStatus;
	private String receiverIdentityNumber;
	private String receiverTownName;
	private String currency;

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

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	public String getBeneficiaryAccountType() {
		return beneficiaryAccountType;
	}

	public void setBeneficiaryAccountType(String beneficiaryAccountType) {
		this.beneficiaryAccountType = beneficiaryAccountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getReceiverResidentStatus() {
		return receiverResidentStatus;
	}

	public void setReceiverResidentStatus(String receiverResidentStatus) {
		this.receiverResidentStatus = receiverResidentStatus;
	}

	public String getReceiverIdentityNumber() {
		return receiverIdentityNumber;
	}

	public void setReceiverIdentityNumber(String receiverIdentityNumber) {
		this.receiverIdentityNumber = receiverIdentityNumber;
	}

	public String getReceiverTownName() {
		return receiverTownName;
	}

	public void setReceiverTownName(String receiverTownName) {
		this.receiverTownName = receiverTownName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "BIFastInqResp [responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", referenceNo="
				+ referenceNo + ", externalId=" + externalId + ", registrationId=" + registrationId + ", receiverName="
				+ receiverName + ", beneficiaryAccountNo=" + beneficiaryAccountNo + ", beneficiaryBankCode="
				+ beneficiaryBankCode + ", beneficiaryAccountType=" + beneficiaryAccountType + ", accountNumber="
				+ accountNumber + ", receiverType=" + receiverType + ", receiverResidentStatus="
				+ receiverResidentStatus + ", receiverIdentityNumber=" + receiverIdentityNumber + ", receiverTownName="
				+ receiverTownName + ", currency=" + currency + "]";
	}
	
	

}
