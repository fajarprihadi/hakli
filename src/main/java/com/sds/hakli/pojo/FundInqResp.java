package com.sds.hakli.pojo;

public class FundInqResp {

	private String responseCode;
	private String responseDescription;
	private String errorDescription;
	private FundData data;

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

	public FundData getData() {
		return data;
	}

	public void setData(FundData data) {
		this.data = data;
	}

}
