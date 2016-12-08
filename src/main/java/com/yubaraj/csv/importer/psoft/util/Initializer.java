package com.yubaraj.csv.importer.psoft.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database connection class.
 *
 * @author Yuba Raj Kalathoki
 */
public class Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);
    private static Properties dbConnectionProperties;
    private static EntityManagerFactory emf;
    public static Map<String, String> configMap;

    static {
	try {
	    configMap = new HashMap<>();
	    dbConnectionProperties = new Properties();
	    dbConnectionProperties.load(new FileInputStream("configuration.properties"));
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    public boolean init() {
	configMap.put(ConfigConst.CSV_FILE_LOCATION, dbConnectionProperties.getProperty("csv.file.location").trim());
	configMap.put(ConfigConst.CSV_FILE_NAME, dbConnectionProperties.getProperty("csv.file.name").trim());
	
	String serverAddress = dbConnectionProperties.getProperty("serverAddress").trim();
	String portNumber = dbConnectionProperties.getProperty("portNumber").trim();
	String databaseName = dbConnectionProperties.getProperty("databaseName").trim();
	String databaseUser = dbConnectionProperties.getProperty("databaseUser").trim();
	String databasePass = dbConnectionProperties.getProperty("databasePass").trim();

	/**
	 * set the properties hashmap to create entitymanagerfactory
	 */
	Map<String,String> properties = new HashMap<String,String>();
	properties.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
	properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	properties.put("hibernate.connection.url",
		"jdbc:mysql://" + serverAddress + ":" + portNumber + "/" + databaseName);
	properties.put("hibernate.connection.username", databaseUser);
	properties.put("hibernate.connection.password", databasePass);
	properties.put("hibernate.jdbc.batch_size", String.valueOf(20));
	/**
	 * create entitymanagerfactory using properties hashmap
	 */
	try {
	    emf = Persistence.createEntityManagerFactory("psoftPU", properties);
	} catch (Exception e) {
	    emf = null;
	    e.printStackTrace();
	    LOGGER.error(e.getMessage());
	}
	return true;
    }

    public void closeConnection() {
	emf.close();
    }

    /**
     *
     * @return An instance of {@link EntityManagerFactory}.
     */
    public static EntityManagerFactory getConnection() {
	return emf;
    }
}
