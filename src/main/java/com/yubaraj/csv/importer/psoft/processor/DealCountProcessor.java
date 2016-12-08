package com.yubaraj.csv.importer.psoft.processor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.model.DealDetails;
import com.yubaraj.csv.importer.psoft.model.LastRow;

/**
 * Processes to count deal.
 * 
 * @author Yuba Raj Kalathoki
 */
public class DealCountProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DealCountProcessor.class);
    private boolean counted;

    @Override
    public void run() {
	LOGGER.info("Counter thread started.");
	while (true) {
	    LastRow lastRow = null;
	    if (!counted) {
		lastRow = JpaProcessor.getInstance().getLastRow();
		List<String> fromCurrencyCode = new ArrayList<String>();
		if (lastRow != null) {
		    Long firstId = lastRow.getFirstId() != null ? lastRow.getFirstId() : 0;
		    Long lastId = lastRow.getLastId();
		    fromCurrencyCode = JpaProcessor.getInstance().getFromCurrencyCode(firstId, lastId);
		    lastRow.setFirstId(lastId);
		    JpaProcessor.getInstance().updateLastRow(lastRow);
		    lastRow = null;
		    LOGGER.debug("currencycode: " + fromCurrencyCode);
		    if (fromCurrencyCode.size() > 0) {
			LOGGER.info("Counting deals for from  currency code");
			for (String cCode : fromCurrencyCode) {
			    count(cCode, firstId, lastId);
			}
			counted = true;
			fromCurrencyCode.clear();
		    } else {
			counted = false;
			LOGGER.info("Waiting for new deal to count.");
			try {
			    Thread.sleep(10000L);
			} catch (InterruptedException e) {
			}
		    }
		}
	    } else
		counted = false;
	    try {
		Thread.sleep(1000L);
	    } catch (InterruptedException e) {
	    }
	    LOGGER.info("Counter thread excited.");
	}
    }

    private void count(String cCode, Long firstId, Long lastId) {
	BigInteger count = JpaProcessor.getInstance().countByFromCurrencyCode(cCode, firstId, lastId);
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

    public boolean isCounted() {
	return counted;
    }
}
