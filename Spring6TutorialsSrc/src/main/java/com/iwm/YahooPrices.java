package com.iwm;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.iwm.bean.YahooHistoryYield;
import com.iwm.config.AppConfig;
import com.iwm.pojo.YieldClosePriceData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class YahooPrices {

	private static final Logger log = LogManager.getLogger(YahooPrices.class);
	
	public static void main(String[] args) {

		log.info("Yahoo Yield Started...");
		String symbol = "IGM.TO";
		String startDate = "2021-01-03";
		String endDate = "2023-02-28";
		
		log.info("Symbol: " + symbol + " from " + startDate + " to " + endDate);

		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		YahooHistoryYield yhy = ctx.getBean("yahooHistoryYield", YahooHistoryYield.class);
		List<YieldClosePriceData> ll = null;
		try {
			ll = yhy.getHistoryYields(symbol, startDate,endDate);
		} catch (IOException | ParseException e) {
			log.error("ERROR: " + e);
		}
		
//		ll.forEach(t -> {
//			System.out.println(t.toString());
//		});
		
		//List<YieldClosePriceData> lfiltered = 
		List<YieldClosePriceData> lm = ll.stream()
				.filter(t -> t.getYield().compareTo(new BigDecimal("6.5")) != 1)
				.collect(Collectors.toList())
				.stream()
				.map(t -> ((List<YieldClosePriceData>) t.getPriceDate()).get(Calendar.MONTH))
				.collect(Collectors.toList());
		
		lm.forEach(t -> {
			System.out.println(t.toString());
		});
		
	}
}