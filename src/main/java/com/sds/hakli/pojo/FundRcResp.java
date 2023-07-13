package com.sds.hakli.pojo;

public class FundRcResp {

	private String responseCode;
	private String responseDescription;
	private String errorDescription;
	private FundRcData data;

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

	public FundRcData getData() {
		return data;
	}

	public void setData(FundRcData data) {
		this.data = data;
	}

}
