package com.yubaraj.csv.importer.psoft.psoft;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yubaraj.csv.importer.psoft.processor.ApplicationProcessor;
import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 * Test class to import csv file.
 * 
 * @author Yuba Raj Kalathoki
 */
public class ImporterTest {
    Initializer initializer;
    EntityManager em;
    EntityManagerFactory emf;

    @Before
    public void init() {
	initializer = new Initializer();
	initializer.init();
	emf = Initializer.getConnection();
	em = emf.createEntityManager();
	em.getTransaction().begin();
    }

    @After
    public void close() {
	em.getTransaction().commit();
    }

    @Test(timeout = 40)
    public void importFile() {
	String fileName = "QKKDQCM99Z.csv";

	ApplicationProcessor processor = new ApplicationProcessor();
	processor.process(fileName);

    }
}
