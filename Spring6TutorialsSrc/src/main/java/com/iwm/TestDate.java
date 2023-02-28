package com.iwm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		
		String sDate1="2023-02-21";
		
		Date date1 = null;
		try {
			date1 = sdf.parse(sDate1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sDate1+" -> "+date1);
	}

	
	public static Date StringToDate(String sDate) {
		
		Date pDate = null;
		try {
			pDate = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pDate;
	}
	
	
}
