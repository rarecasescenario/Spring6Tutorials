package com.iwm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import com.iwm.pojo.YahooHistoryData;
import com.iwm.pojo.YahooHistoryDividendData;

public class YahooPrices {

	public static void main(String[] args) throws IOException, ParseException {

		final String SYM = "BMO.TO";
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 30);

		java.util.Date period1 = datechange(cal);
		cal1.set(Calendar.YEAR, 2023);
		cal1.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal1.set(Calendar.DAY_OF_MONTH, 23);
		java.util.Date period2 = datechange(cal1);

		long strDate = (period1.getTime());
		strDate = strDate / 1000;
		long strDate1 = (period2.getTime());
		strDate1 = strDate1 / 1000;
		
		
		List<YahooHistoryData> lprices = getHistoryPrices(SYM, strDate, strDate1);
		List<YahooHistoryDividendData> lDiv = getHistoryDividends(SYM, strDate, strDate1);

		String curDiv = "";
		
		
		System.out.println("Date,ClosePrice,Dividend,Yield");
		
		for(int i = 0; i < lprices.size(); i++) {
			
			String cdate = lprices.get(i).getDate();
			
			Optional<YahooHistoryDividendData> div = lDiv.stream().filter(d -> d.getDate().equals(cdate)).findFirst();
			
			if(div.isPresent()) {
				curDiv = div.get().getDividend();
			}
			lprices.get(i).setDividend(curDiv);
			
			if( !curDiv.isEmpty()) {
				double dprice = Double.parseDouble(lprices.get(i).getClose());
				double ddiv = Double.parseDouble(lprices.get(i).getDividend());
				Double yield = ddiv * 4 /dprice * 100;
				System.out.println(lprices.get(i).getDate() + "," + lprices.get(i).getClose() + "," + lprices.get(i).getDividend() + "," + String.format("%.2f", new BigDecimal(yield)));	
			}
		}
	}

	
	public static List<YahooHistoryData> getHistoryPrices(String symbol, long strStartDate,
			long strEndDate) throws IOException, ParseException {
		
		List<YahooHistoryData> result = new ArrayList();

		String interval = "1d";

		String link = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol + "?period1=" + strStartDate
				+ "&period2=" + strEndDate + "&interval=" + interval + "&events=history";
		//System.out.println("Div Link: " + link);

		URL url = new URL(link);
		URLConnection urlConn = url.openConnection();
		InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
		BufferedReader buf = new BufferedReader(inStream);
		
		String line;
		
		while((line = buf.readLine()) != null){
		
			//System.out.println(line); //Date,Open,High,Low,Close,Adj Close,Volume
			
			if(line != null && !line.contains("Date") && line.length() > 50) {
				String[] mem = line.split(",");
			
				YahooHistoryData cd = new YahooHistoryData();
				
				cd.setDate(mem[0]);
				cd.setOpen(mem[1]);
				cd.setHigh(mem[2]);
				cd.setLow(mem[3]);
				cd.setClose(mem[4]);
				
				result.add(cd);
			}
		}
		return result;
	}	
	
	
	public static List<YahooHistoryDividendData> getHistoryDividends(String symbol, long strStartDate,
			long strEndDate) throws IOException, ParseException {
		
		List<YahooHistoryDividendData> result = new ArrayList();

		String interval = "1d";

		String link = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol + "?period1=" + strStartDate
				+ "&period2=" + strEndDate + "&interval=" + interval + "&events=div";
		//System.out.println("Div Link: " + link);

		URL url = new URL(link);
		URLConnection urlConn = url.openConnection();
		InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
		BufferedReader buf = new BufferedReader(inStream);
		
		String line;
		
		while((line = buf.readLine()) != null){
		
			if(line != null && !line.contains("Dividends") && line.length() > 15) {

				String[] mem = line.split(",");
			
				YahooHistoryDividendData currentDivData = new YahooHistoryDividendData();
				
				currentDivData.setDate(mem[0]);
				currentDivData.setDividend(mem[1]);
				
				result.add(currentDivData);
			}
		}
		return result;
	}
	

	public static java.util.Date datechange(Calendar cal) throws ParseException {
		java.util.Date dateOne = cal.getTime();
		// boolean date1904 = true;
		// double ans =DateUtil.getExcelDate(cal,date1904);

		String a = dateOne.toString();
		String b[] = a.split(" ");
		String c = b[1] + " " + b[2] + " " + b[5];
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(sdf.parse(c));
		dateOne = cal.getTime();
		sdf.format(dateOne);
		return dateOne;
	}
}



//// System.out.println(strDate+" "+strDate1+" ans");
//String link = "https://query1.finance.yahoo.com/v7/finance/download/" + SYM + "?period1=" + strDate
//		+ "&period2=" + strDate1 + "&interval=" + interval + "&events=history";
//
//System.out.println("Link: " + link);
//
//URL url = new URL(link);
//URLConnection urlConn = url.openConnection();
//InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
//BufferedReader buf = new BufferedReader(inStream);
//
//String line = buf.readLine();
//while (line != null) {
//	// System.out.println(line);
//	line = buf.readLine();
//}