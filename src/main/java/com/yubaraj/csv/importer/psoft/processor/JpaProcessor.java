package com.yubaraj.csv.importer.psoft.processor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.model.DealDetails;
import com.yubaraj.csv.importer.psoft.model.LastRow;
import com.yubaraj.csv.importer.psoft.model.ValidDeal;
import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 *
 * @author Yuba Raj Kalathoki
 */
public class JpaProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaProcessor.class);
    private static JpaProcessor dbProcessor;
    private static EntityManagerFactory emf;

    public static JpaProcessor getInstance() {
	try {
	    if (dbProcessor == null) {
		synchronized (JpaProcessor.class) {
		    if (dbProcessor == null) {
			emf = Initializer.getConnection();
			dbProcessor = new JpaProcessor();
		    }
		}
	    }
	    return dbProcessor;
	} catch (Exception e) {
	    return dbProcessor;
	}
    }

    public boolean saveDeals(List<ValidDeal> dealsList) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager em = emf.createEntityManager();
	try {
	    em.getTransaction().begin();
	    for (int i = 0; i < dealsList.size(); i++) {
		ValidDeal deals = dealsList.get(i);
		em.persist(deals);
		if ((i % 20) == 0) {
		    em.flush();
		    em.clear();
		}
	    }
	    em.getTransaction().commit();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	    return false;
	} finally {
	    em.close();
	}
    }

    public boolean importFileDirectly(String file) {
	EntityManager em = emf.createEntityManager();
	EntityTransaction entityTransaction = em.getTransaction();
	System.out.println("file: " + file);
	try {
	    String sql = "LOAD DATA INFILE '" + file + "' " + "INTO TABLE validdeal " + "FIELDS TERMINATED BY ',' "
		    + "ENCLOSED BY '\"' " + "LINES TERMINATED BY '\\n' set sourceFile='" + file + "'";
	    System.out.println("sql: " + sql);
	    entityTransaction.begin();
	    em.createNativeQuery(sql).executeUpdate();
	    System.out.println("===============Finished!=============");
	    entityTransaction.commit();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public BigInteger countByFromCurrencyCode(String cCode, BigInteger lastId) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    String sql = "SELECT COUNT(0) FROM validdeal WHERE fromCurrencyIsoCode='" + cCode + "' and id >=" + lastId;
	    Query query = eManager.createNativeQuery(sql);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	    return new BigInteger("0");
	} finally {
	    eManager.close();
	}

    }

    public DealDetails getDealDetailsFromCurrencyCode(String cCode) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    TypedQuery<DealDetails> query = eManager
		    .createQuery("SELECT d FROM DealDetails d WHERE d.currencyIsoCode = :cCode", DealDetails.class);
	    return query.setParameter("cCode", cCode).getSingleResult();
	    // return (DealDetails) query.getSingleResult();
	} catch (NoResultException e) {
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return null;
    }

    public void updateCount(DealDetails dealDetails) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    eManager.getTransaction().begin();
	    if (dealDetails.getId() != null) {
		eManager.merge(dealDetails);
	    } else {
		eManager.persist(dealDetails);
	    }
	    eManager.getTransaction().commit();
	    LOGGER.info(
		    "Total deals for [" + dealDetails.getCurrencyIsoCode() + "] is: " + dealDetails.getCountOfDeals());
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
    }

    @SuppressWarnings("unchecked")
    public List<String> getFromCurrencyCode() {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	List<String> currencyCodes = new ArrayList<String>();
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    Query query = eManager
		    .createNativeQuery("SELECT fromCurrencyIsoCode FROM validdeal GROUP BY fromCurrencyIsoCode");
	    currencyCodes = query.getResultList();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return currencyCodes;
    }

    public BigInteger getLastRow() {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    Query query = eManager.createNativeQuery("SELECT rowId FROM lastrow");
	    return (BigInteger) query.getSingleResult();
	} catch (NoResultException e) {
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return new BigInteger("0");
    }

    public void updateLastRow(String lastRowUniqueId) {
	LOGGER.debug("Updating last row.");
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    eManager.getTransaction().begin();
	    Query query = eManager
		    .createNativeQuery("select id from validdeal where dealUniqueId='" + lastRowUniqueId + "'");
	    BigInteger lastRowId = (BigInteger) query.getSingleResult();
	    LastRow lRow = new LastRow(1l);
	    lRow.setRowId(lastRowId.longValue());
	    eManager.merge(lRow);
	    eManager.getTransaction().commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
    }
}
