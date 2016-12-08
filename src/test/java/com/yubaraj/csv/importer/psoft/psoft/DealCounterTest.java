package com.yubaraj.csv.importer.psoft.psoft;

import org.junit.Assert;
import org.junit.Test;

import com.yubaraj.csv.importer.psoft.processor.JpaProcessor;

/**
 * Tests accumulative count of deals.
 * 
 * @author Yuba Raj Kalathoki
 */
public class DealCounterTest extends AbstractTest {
    /**
     * After importing csv file into database table. The number of record in
     * validdeal table and the sum of countOfDeals from dealdetails table must be
     * equal.
     * 
     * @author Yuba Raj Kalathoki
     */
    @Test
    public void accumulativeCountTest() {
	long totalDeals = JpaProcessor.getInstance().countTotalDeals();
	long sumOfDealsPerOrderingCurrency = JpaProcessor.getInstance().getSumOfDealsPerOrderingCurrency();
	Assert.assertEquals(totalDeals, sumOfDealsPerOrderingCurrency);
    }
}
