package com.sds.hakli.pojo;

public class FundTrfResp {

	private String responseCode;
	private String responseDescription;
	private String errorDescription;
	private String journalSeq;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getJournalSeq() {
		return journalSeq;
	}

	public void setJournalSeq(String journalSeq) {
		this.journalSeq = journalSeq;
	}

}
