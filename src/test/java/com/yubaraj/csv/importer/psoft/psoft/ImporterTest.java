package com.yubaraj.csv.importer.psoft.psoft;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.processor.ApplicationProcessor;

/**
 * Test class to import csv file.
 * 
 * @author Yuba Raj Kalathoki
 */
public class ImporterTest extends AbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImporterTest.class);

    @Test
    public void importFileTest() {
	ApplicationProcessor processor = new ApplicationProcessor();
	startTime = System.currentTimeMillis();
	processor.process(fileName);
	endTime = System.currentTimeMillis();
	LOGGER.info("TestCase Execution time : " + executionTimeInSecond());
	Assert.assertEquals(5, executionTime);
    }

    private long executionTimeInSecond() {
	executionTime = secondValue(endTime - startTime);
	return executionTime;
    }

    private long secondValue(long mills) {
	return mills / 1000;
    }
}
