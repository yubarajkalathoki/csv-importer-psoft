package com.yubaraj.csv.importer.psoft.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.model.ValidDeal;
import com.yubaraj.csv.importer.psoft.util.ConfigConst;
import com.yubaraj.csv.importer.psoft.util.CsvReader;
import com.yubaraj.csv.importer.psoft.util.FileUtil;
import com.yubaraj.csv.importer.psoft.util.Initializer;

public class ImportProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportProcessor.class);

    private boolean finishedImport = false;

    String path;

    public ImportProcessor(String path) {
	this.path = path;
    }

    @Override
    public void run() {
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

    private void sleep(long mills) {
	try {
	    Thread.sleep(mills);
	} catch (InterruptedException e) {
	    LOGGER.error(e.getMessage());
	}
    }

    private void process(String fileName) {
	LOGGER.info("Importing file: " + fileName);
	String source = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION) + "\\" + fileName;
	List<ValidDeal> dealsList = CsvReader.readCsv(source);

	String firstRowUniqueId = dealsList.get(0).getDealUniqueId();

	if (JpaProcessor.getInstance().saveDeals(dealsList)) {
	    JpaProcessor.getInstance().updateLastRow(firstRowUniqueId);
	    LOGGER.info("Imported file: " + fileName);
	    finishedImport = true;
	    // startCounter();
	    archiveFiles(source, fileName);
	}
    }

    private void archiveFiles(String source, String fileName) {
	String newDirectory = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION) + File.separator + "Archived";
	FileUtil.createDirectoryIfNotExist(newDirectory);
	String target = newDirectory + File.separator + fileName;
	try {
	    FileUtil.moveFile(source, target);
	} catch (IOException e) {
	    LOGGER.info("Failed to move file. Reason:" + e.getMessage());
	}
    }

    // private void startCounter() {
    // BigInteger lastRow = JpaProcessor.getInstance().getLastRow();
    // List<String> fromCurrencyCode =
    // JpaProcessor.getInstance().getFromCurrencyCode();
    // ExecutorService executor = Executors.newFixedThreadPool(10);
    // DealCountProcessor countProcessor;
    // for (String cCode : fromCurrencyCode) {
    // countProcessor = new DealCountProcessor(cCode, lastRow);
    // executor.execute(countProcessor);
    // }
    // }

    public boolean isFinishedImport() {
	return finishedImport;
    }
}
