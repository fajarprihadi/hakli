package com.sds.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;

public class StringUtils {

	/*private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	*/

	private static final String EMAIL_PATTERN = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)";

	public static boolean emailValidator(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email.trim());
		return matcher.matches();
	}
	
	public static boolean digitKtpValidator(String noktp) {
		boolean digitValid = true;
		if(noktp.trim().length() != 16 ) {
			digitValid = false;
		}
		return digitValid;
	}
	
	public static boolean ktpValidator(String noktp, String wilayah, String tgllahir) {
		boolean isValid = false;
		String checkWilcode = noktp.substring(0, 6);
		String checkTgllahir = noktp.substring(6, 12);
		
		if(wilayah.equals(checkWilcode)) {
			if(tgllahir.equals(checkTgllahir)) {
				isValid = true;
			} else {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		
		return isValid;
	}

	public static String randomKey() {
		Random rn = new Random();
		long min = 100000000;
		long max = 999999999;
		long n = max - min + 1;
		long rnd = rn.nextLong() % n;
		long crnt = (Long.parseLong(new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date())) / rnd);
		return String.valueOf(crnt);
	}

	public static boolean isValidDate(String format, String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static String dateFormatted(String strDate) {
		try {
			return (strDate.substring(5, 7) + "/" + strDate.substring(8, 10) + "/" + strDate.substring(0, 4));
		} catch (Exception e) {
			return "";
		}
	}

	public static String dateUnformatted(String strDate) {
		try {
			return (strDate.substring(6) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2));
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateUnformatted2(String strDate) {
		try {
			return (strDate.substring(4) + "-" + strDate.substring(2, 4) + "-" + strDate.substring(0, 2));
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateDashFormatted(String strDate) throws Exception {
		try {
			return (strDate.substring(0, 4) + "-" + strDate.substring(4, 6) + "-" + strDate.substring(6, 8));
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateDashFormatted2(String strDate) throws Exception {
		String date = null;
		try {
			date = (strDate.substring(6, 10) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2));
		} catch (Exception e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	public static String dateDashFormatted3(String strDate) throws Exception {
		String date = "";
		try {
			if (strDate != null && strDate.length() >= 10)
				date = (strDate.substring(8, 10) + "-" + strDate.substring(5, 7) + "-" + strDate.substring(0, 4));
		} catch (Exception e) {
			date = "";
			e.printStackTrace();
		}
		return date;
	}

	public static String dateSlashFormatted(String strDate) throws Exception {
		String date = "";
		try {
			if (strDate != null && strDate.length() >= 10)
				date = (strDate.substring(8, 10) + "/" + strDate.substring(5, 7) + "/" + strDate.substring(0, 4));
		} catch (Exception e) {
			date = "";
			e.printStackTrace();
		}
		return date;
	}

	public static String dateFddMMyyyy(String strDate) throws Exception {
		try {
			return (strDate.substring(4, 8) + "-" + strDate.substring(2, 4) + "-" + strDate.substring(0, 2));
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateDashddMMyyyy(String strDate) throws Exception {
		try {
			return (strDate.substring(6, 10) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2));
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateFyyyyMMdd(String strDate) throws Exception {
		try {
			return (strDate.substring(0, 4) + "-" + strDate.substring(4, 6) + "-" + strDate.substring(6, 8));
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}

	public static String currencyFormatted(String curr) throws Exception {
		String value = "0";
		try {
			if (curr == null | curr.trim().equals(""))
				value = "0";
			else
				value = NumberFormat.getNumberInstance().format(new BigDecimal(curr)).replaceAll(",", ".");
		} catch (Exception e) {
			value = "0";
			e.printStackTrace();
		}
		return value;
	}

	public static String stringPad(int anInt, int padLength) {
		String strInt = String.valueOf(anInt);
		int strLength = strInt.length();
		for (int i = 0; i < (padLength - strLength); i++)
			strInt = "0" + strInt;
		return strInt;
	}

	public static String getPeriod(Date period) {
		String sPeriod = "";
		if (period != null) {
			String month = getMonthLabel(Integer.parseInt(new SimpleDateFormat("MM").format(period)));
			String year = new SimpleDateFormat("yyyy").format(period);
			sPeriod = month + " " + year;
		}
		return sPeriod;
	}

	public static String getMonthLabel(int month) {
		String label = "";

		switch (month) {
		case 1:
			label = "Januari";
			break;
		case 2:
			label = "Februari";
			break;
		case 3:
			label = "Maret";
			break;
		case 4:
			label = "April";
			break;
		case 5:
			label = "Mei";
			break;
		case 6:
			label = "Juni";
			break;
		case 7:
			label = "Juli";
			break;
		case 8:
			label = "Agustus";
			break;
		case 9:
			label = "September";
			break;
		case 10:
			label = "Oktober";
			break;
		case 11:
			label = "November";
			break;
		case 12:
			label = "Desember";
			break;
		}

		return label;
	}

	public static String getMonthshortLabel(int month) {
		String label = "";

		switch (month) {
		case 1:
			label = "Jan";
			break;
		case 2:
			label = "Feb";
			break;
		case 3:
			label = "Mar";
			break;
		case 4:
			label = "Apr";
			break;
		case 5:
			label = "May";
			break;
		case 6:
			label = "Jun";
			break;
		case 7:
			label = "Jul";
			break;
		case 8:
			label = "Aug";
			break;
		case 9:
			label = "Sep";
			break;
		case 10:
			label = "Oct";
			break;
		case 11:
			label = "Nov";
			break;
		case 12:
			label = "Dec";
			break;
		}

		return label;
	}

	public static int getMonthValue(String month) {
		int value = 0;
		if (month.equals("Jan"))
			value = 1;
		else if (month.equals("Feb"))
			value = 2;
		else if (month.equals("Mar"))
			value = 3;
		else if (month.equals("Apr"))
			value = 4;
		else if (month.equals("May"))
			value = 5;
		else if (month.equals("Jun"))
			value = 6;
		else if (month.equals("Jul"))
			value = 7;
		else if (month.equals("Aug"))
			value = 8;
		else if (month.equals("Sep"))
			value = 9;
		else if (month.equals("Oct"))
			value = 10;
		else if (month.equals("Nov"))
			value = 11;
		else if (month.equals("Dec"))
			value = 12;
		return value;
	}

	public static String angkaToTerbilang(Long angka) throws Exception {
		String bilangan = "";
		String[] huruf = { "", "Satu", "Dua", "Tiga", "Empat", "Lima", "Enam", "Tujuh", "Delapan", "Sembilan",
				"Sepuluh", "Sebelas" };
		try {
			if (angka < 12)
				return huruf[angka.intValue()];
			if (angka >= 12 && angka <= 19)
				return huruf[angka.intValue() % 10] + " Belas";
			if (angka >= 20 && angka <= 99)
				return angkaToTerbilang(angka / 10) + " Puluh " + huruf[angka.intValue() % 10];
			if (angka >= 100 && angka <= 199)
				return "Seratus " + angkaToTerbilang(angka % 100);
			if (angka >= 200 && angka <= 999)
				return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
			if (angka >= 1000 && angka <= 1999)
				return "Seribu " + angkaToTerbilang(angka % 1000);
			if (angka >= 2000 && angka <= 999999)
				return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
			if (angka >= 1000000 && angka <= 999999999)
				return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
			if (angka >= 1000000000 && angka <= 999999999999L)
				return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
			if (angka >= 1000000000000L && angka <= 999999999999999L)
				return angkaToTerbilang(angka / 1000000000000L) + " Triliun "
						+ angkaToTerbilang(angka % 1000000000000L);
			if (angka >= 1000000000000000L && angka <= 999999999999999999L)
				return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun "
						+ angkaToTerbilang(angka % 1000000000000000L);
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bilangan;
	}

	public static String FormatUang(String Uang) {
		if (Uang == null || Uang.equals("")) {
			Uang = "0";
			return Uang;
		}
		String fU = new String("");
		BigDecimal BD = new BigDecimal(Uang);
		try {
			fU = new DecimalFormat("#,##0;-#,##0").getInstance(java.util.Locale.ITALY).format(BD);
			// if (fU.lastIndexOf(",")<0) fU=fU+",00";
		} catch (Exception e) {
			fU = Uang;
		}
		return fU;
	}

	public static String addPadding(int count, String quote) {
		String padding = "";
		for (int i = 0; i < count; i++) {
			padding = quote + padding;
		}
		return padding;
	}

	public static String checkLeng(int len, String sAmount, char quote) {
		if (sAmount.length() > len) {
			int lebih = sAmount.length() - len;
			sAmount = sAmount.substring(lebih, sAmount.length());
		} else if (sAmount.length() < len) {
			int kurang = len - sAmount.length();
			for (int i = 0; i < kurang; i++) {
				if (quote == '0') {
					sAmount = "0" + sAmount;
				} else {
					sAmount = " " + sAmount;
				}
			}
		}
		return sAmount;
	}

	public static List<Integer> listYears(Integer start) {
		List<Integer> objList = new ArrayList<>();
		Integer current = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = start; i <= current; i++) {
			objList.add(i);
		}
		return objList;
	}

	public static Date getDateCustomFormat(Cell cell) throws Exception {
		Date date = null;
		DataFormatter poiFormatter = new DataFormatter();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
		DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
		style.setDataFormat(format.getFormat("[$-809]yyyy-MM-dd;@"));
		cell.setCellStyle(style);
		String sdate = poiFormatter.formatCellValue(cell);
		System.out.println("getDateCustomFormat sdate " + sdate);
		date = dateFormat.parse(sdate);
		return date;
	}

	public static String extractEmails(String emails) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emails);
		String res = emails;
		while (matcher.find()) {
			res = matcher.group();
			break;
		}
		return res;
	}
	
	
}
