package com.yubaraj.csv.importer.psoft.processor;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.model.ValidDeal;
import com.yubaraj.csv.importer.psoft.util.ConfigConst;
import com.yubaraj.csv.importer.psoft.util.CsvReader;
import com.yubaraj.csv.importer.psoft.util.FileUtil;
import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 * Manages deal process.
 * 
 * @author Yuba Raj Kalathoki
 */
public class ApplicationProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProcessor.class);

    DealCountProcessor dealCountProcessor;
    private static final String SEPARATOR = "/";

    @Override
    public void run() {
	LOGGER.info("==========Application started==========");
	String path = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION);
	while (true) {
	    List<String> files = FileUtil.getFiles(path);
	    if (files.size() > 0) {
		LOGGER.info("Csv file found.");
		for (String file : files) {
		    process(file);
		    sleep(1000l);
		}
	    } else {
		LOGGER.info("Waiting for file to import.");
		sleep(90000l);
	    }
	}
    }

    private void sleep(long mills) {
	try {
	    Thread.sleep(mills);
	} catch (InterruptedException e) {
	    LOGGER.error(e.getMessage());
	}
    }

    public synchronized void process(String fileName) {
	LOGGER.info("Importing file: " + fileName);
	String source = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION) + SEPARATOR + fileName;
	LOGGER.debug("Source: " + source);
	List<ValidDeal> dealsList = CsvReader.readCsv(source);
	String firstRowUniqueId = dealsList.get(dealsList.size() - 1).getDealUniqueId();
	boolean imported = JpaProcessor.getInstance().importFileDirectly(source);
	if (imported) {
	    dealsList.clear();
	    archiveFiles(source, fileName);
	    JpaProcessor.getInstance().updateLastRow(firstRowUniqueId);
	    LOGGER.info("Imported file: " + fileName);
	    startCounter();
	}
    }

    private void archiveFiles(String source, String fileName) {
	String newDirectory = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION) + SEPARATOR + "Archived";
	FileUtil.createDirectoryIfNotExist(newDirectory);
	String target = newDirectory + SEPARATOR + fileName;
	try {
	    FileUtil.moveFile(source, target);
	} catch (IOException e) {
	    LOGGER.info("Failed to move file. Reason:" + e.getMessage());
	}
    }

    private void startCounter() {
	if (dealCountProcessor == null) {
	    dealCountProcessor = new DealCountProcessor();
	    Thread thread = new Thread(dealCountProcessor);
	    thread.start();
	}
    }
}
