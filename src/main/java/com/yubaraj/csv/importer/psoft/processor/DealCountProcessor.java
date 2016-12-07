package com.yubaraj.csv.importer.psoft.processor;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.model.DealDetails;

/**
 * Processes to count deal.
 * 
 * @author Yuba Raj Kalathoki
 */
public class DealCountProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DealCountProcessor.class);

    private boolean started = false;

    @Override
    public void run() {
	started = true;
	List<String> fromCurrencyCode = JpaProcessor.getInstance().getFromCurrencyCode();
	BigInteger lastRow = new BigInteger("0");
	if (fromCurrencyCode.size() > 0) {
	    lastRow = JpaProcessor.getInstance().getLastRow();
	    LOGGER.info("Counting deals per from  currency code");
	}
	for (String cCode : fromCurrencyCode) {
	    count(cCode, lastRow);
	}
    }

    private void count(String cCode, BigInteger lastRow) {
	BigInteger count = JpaProcessor.getInstance().countByFromCurrencyCode(cCode, lastRow);
	if (count.compareTo(BigInteger.ZERO) > 0) {
	    DealDetails dealDetails = JpaProcessor.getInstance().getDealDetailsFromCurrencyCode(cCode);
	    if (dealDetails != null) {
		Long total = dealDetails.getCountOfDeals() + count.longValue();
		dealDetails.setCountOfDeals(total);
		JpaProcessor.getInstance().updateCount(dealDetails);
	    } else {
		DealDetails neCount = new DealDetails();
		neCount.setCountOfDeals(count.longValue());
		neCount.setCurrencyIsoCode(cCode);
		JpaProcessor.getInstance().updateCount(neCount);
	    }
	}
    }

    public boolean isStarted() {
	return started;
    }
}
