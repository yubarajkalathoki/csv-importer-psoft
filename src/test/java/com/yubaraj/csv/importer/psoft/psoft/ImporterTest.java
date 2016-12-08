package com.yubaraj.csv.importer.psoft.psoft;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.processor.ApplicationProcessor;
import com.yubaraj.csv.importer.psoft.util.ConfigConst;
import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 * Test class to import csv file.
 * 
 * @author Yuba Raj Kalathoki
 */
public class ImporterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImporterTest.class);
    EntityManager em;
    String fileName;
    long startTime;
    long endTime;
    long executionTime;

    @Before
    public void init() {
	new Initializer().init();
	EntityManagerFactory emf = Initializer.getConnection();
	em = emf.createEntityManager();
	em.getTransaction().begin();
	fileName = Initializer.configMap.get(ConfigConst.CSV_FILE_NAME);
    }

    @After
    public void close() {
	em.getTransaction().commit();
    }

    @Test
    public void importFile() {
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
