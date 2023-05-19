package com.sds.hakli.viewmodel;

import com.sds.utils.StringUtils;

public class TestValidatorKtp {

	public static void main(String[] args) {
		String noktp = "3171070707970003";
		boolean isValid = StringUtils.ktpValidator(noktp, "317107", "070797", "L");
		
		if(isValid) {
			System.out.println("NOMOR KTP BENAR");
		} else {
			System.out.println("NOMOR KTP SALAH");
		}
	}

}
