package com.yubaraj.csv.importer.psoft.psoft;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;

import com.yubaraj.csv.importer.psoft.util.ConfigConst;
import com.yubaraj.csv.importer.psoft.util.Initializer;

public abstract class AbstractTest {
    protected EntityManager em;
    protected String fileName;
    protected long startTime;
    protected long endTime;
    protected long executionTime;

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

}
