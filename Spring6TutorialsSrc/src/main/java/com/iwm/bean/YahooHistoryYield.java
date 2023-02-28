package com.iwm.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import com.iwm.pojo.YahooHistoryData;
import com.iwm.pojo.YahooHistoryDividendData;
import com.iwm.pojo.YieldClosePriceData;

import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class YahooHistoryYield {

	private static final Logger log = LogManager.getLogger(YahooHistoryYield.class);
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 
	 * @param symbol    i.e. BNS.TO
	 * @param startDate [yyyy-MM-dd]
	 * @param endDate   [yyyy-MM-dd]
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<YieldClosePriceData> getHistoryYields(String symbol, String startDate, String endDate) throws IOException, ParseException {
		if( symbol == null || startDate == null || endDate == null)
			return null;
	
		java.util.Date period1 = stringToDate(startDate);
		log.info("period1 = " + period1);
		long strDate = (period1.getTime());
		strDate = strDate / 1000;
		
		java.util.Date period2 = stringToDate(endDate);
		log.info("period2 = " + period2);
		long strDate1 = (period2.getTime());
		strDate1 = strDate1 / 1000;		
		
		List<YahooHistoryData> lprices = getHistoryPrices(symbol, strDate, strDate1);
		List<YahooHistoryDividendData> lDiv = getHistoryDividends(symbol, strDate, strDate1);
		List<YieldClosePriceData> lycpd = new ArrayList();

		String curDiv = "";
		//System.out.println("Date,ClosePrice,Dividend,Yield");
		
		for(int i = 0; i < lprices.size(); i++) {
			
			String cdate = lprices.get(i).getDate();
			
			Optional<YahooHistoryDividendData> div = lDiv.stream().filter(d -> d.getDate().equals(cdate)).findFirst();
			
			if(div.isPresent()) {
				curDiv = div.get().getDividend();
			}
			lprices.get(i).setDividend(curDiv);
			
			YieldClosePriceData ycpd = new YieldClosePriceData();
			
			
			if( !curDiv.isEmpty()) {

				java.util.Date pDate = stringToDate(lprices.get(i).getDate());
				BigDecimal bPrice = new BigDecimal(lprices.get(i).getClose());
				BigDecimal bDiv = new BigDecimal(lprices.get(i).getDividend());
				BigDecimal bYield = (bDiv.multiply(new BigDecimal("4"),MathContext.DECIMAL32).divide(bPrice, MathContext.DECIMAL32).multiply(new BigDecimal("100"),MathContext.DECIMAL32));
				
				ycpd.setPriceDate(pDate);
				ycpd.setClosePrice(bPrice);
				ycpd.setQuoterlyDividendAmount(bDiv);
				ycpd.setYield(bYield);
				
				lycpd.add(ycpd);
			}
		}
		return lycpd;
	}
	
	
	public List<YahooHistoryData> getHistoryPrices(String symbol, long strStartDate,
			long strEndDate) throws IOException, ParseException {
		
		List<YahooHistoryData> result = new ArrayList();

		String interval = "1d";

		String link = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol + "?period1=" + strStartDate
				+ "&period2=" + strEndDate + "&interval=" + interval + "&events=history";
		log.info("Price History Link: " + link);

		URL url = new URL(link);
		URLConnection urlConn = url.openConnection();
		InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
		BufferedReader buf = new BufferedReader(inStream);
		
		String line;
		
		while((line = buf.readLine()) != null){
			
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
	

	public List<YahooHistoryDividendData> getHistoryDividends(String symbol, long strStartDate,
			long strEndDate) throws IOException, ParseException {
		
		List<YahooHistoryDividendData> result = new ArrayList();

		String interval = "1d";

		String link = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol + "?period1=" + strStartDate
				+ "&period2=" + strEndDate + "&interval=" + interval + "&events=div";
		
		log.info("Div Link: " + link);

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
	

	public java.util.Date datechange(Calendar cal) throws ParseException {
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
	
	
	/**
	 * Convert String date into java Date
	 * 
	 * @param sDate
	 * @return
	 */
	public java.util.Date stringToDate(String sDate) {
		
		Date pDate = null;
		try {
			pDate = sdf.parse(sDate);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return pDate;
	}
}
 
