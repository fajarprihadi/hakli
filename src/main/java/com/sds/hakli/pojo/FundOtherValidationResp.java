package com.sds.hakli.pojo;

public class FundOtherValidationResp {

	private String responseCode;
	private String responseDescription;
	private String errorDescription;
	private String Name;

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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
