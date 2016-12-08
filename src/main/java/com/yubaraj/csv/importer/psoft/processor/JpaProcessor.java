package com.yubaraj.csv.importer.psoft.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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

    public boolean saveDealsNativeQuery(List<ValidDeal> dealsList) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager em = emf.createEntityManager();
	try {
	    em.getTransaction().begin();
	    for (int i = 0; i < dealsList.size(); i++) {
		ValidDeal deals = dealsList.get(i);
		em.createNativeQuery(
			"insert into validdeal (dealUniqueId, fromCurrencyIsoCode, toCurrencyIsoCode,dealTimestamp, dealAmount, sourceFile) "
				+ "values ('" + deals.getDealUniqueId() + "','" + deals.getFromCurrencyIsoCode() + "','"
				+ deals.getToCurrencyIsoCode() + "','" + deals.getDealTimestamp() + "','"
				+ deals.getDealAmount() + "','" + deals.getSourceFile() + "')")
			.executeUpdate();
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

    static <T> List<List<T>> chopped(List<T> list, final int L) {
	List<List<T>> parts = new ArrayList<List<T>>();
	final int N = list.size();
	for (int i = 0; i < N; i += L) {
	    parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
	}
	return parts;
    }

    public boolean importFileDirectly(String file) {
	EntityManager em = emf.createEntityManager();
	EntityTransaction entityTransaction = em.getTransaction();
	try {
	    String sql = "LOAD DATA INFILE '" + file + "' " + "INTO TABLE validdeal " + "FIELDS TERMINATED BY ',' "
		    + "ENCLOSED BY '\"' " + "LINES TERMINATED BY '\\n'"
		    + " (dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount)  "
		    + " set sourceFile='" + file + "', id=NULL";
	    entityTransaction.begin();
	    em.createNativeQuery(sql).executeUpdate();
	    entityTransaction.commit();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public BigInteger countByFromCurrencyCode(String cCode, Long firstId, Long lastId) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    String sql = "SELECT COUNT(0) FROM validdeal WHERE fromCurrencyIsoCode='" + cCode + "' and id > " + firstId
		    + " and id <= " + lastId;
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
    public List<String> getFromCurrencyCode(Long firstId, Long lastId) {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	List<String> currencyCodes = new ArrayList<String>();
	EntityManager eManager = null;
	try {
	    String sqlString = "SELECT fromCurrencyIsoCode FROM validdeal where id > " + firstId + " and id <= "
		    + lastId + " GROUP BY fromCurrencyIsoCode";
	    eManager = emf.createEntityManager();
	    Query query = eManager.createNativeQuery(sqlString);
	    currencyCodes = query.getResultList();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return currencyCodes;
    }

    public LastRow getLastRow() {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    TypedQuery<LastRow> query = eManager.createQuery("SELECT lr FROM LastRow lr", LastRow.class);
	    return query.getSingleResult();
	} catch (NoResultException e) {
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return null;
    }

    public synchronized LastRow updateLastRow(String lastRowUniqueId) {
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
	    LastRow lastRow = getLastRow();
	    if (lastRow != null) {
		lastRow.setLastId(lastRowId.longValue());
		eManager.merge(lastRow);
	    } else {
		lastRow = new LastRow();
		lastRow.setLastId(lastRowId.longValue());
		eManager.persist(lastRow);
	    }
	    eManager.getTransaction().commit();
	    return lastRow;
	} catch (NonUniqueResultException nure) {
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return null;
    }

    public synchronized LastRow updateLastRow(LastRow lastRow) {
	LOGGER.debug("Updating last row.");
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    eManager.getTransaction().begin();
	    eManager.merge(lastRow);
	    eManager.getTransaction().commit();
	    return lastRow;
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	} finally {
	    eManager.close();
	}
	return null;
    }

    public long countTotalDeals() {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    String sql = "SELECT COUNT(0) FROM validdeal ";
	    Query query = eManager.createNativeQuery(sql);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count.longValue();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	    return 0;
	} finally {
	    eManager.close();
	}

    }

    /**
     * Returns sum of deal count from dealdetails.
     * 
     * @author Yuba Raj Kalathoki
     */
    public long getSumOfDealsPerOrderingCurrency() {
	if (emf == null) {
	    emf = Initializer.getConnection();
	}
	EntityManager eManager = null;
	try {
	    eManager = emf.createEntityManager();
	    String sql = "SELECT SUM(countOfDeals) FROM dealdetails";
	    Query query = eManager.createNativeQuery(sql);
	    BigDecimal sum = (BigDecimal) query.getSingleResult();
	    return sum.longValue();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Exception: " + e.getMessage());
	    return 0;
	} finally {
	    eManager.close();
	}
    }
}
