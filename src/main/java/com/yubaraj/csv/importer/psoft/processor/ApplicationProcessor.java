package com.yubaraj.csv.importer.psoft.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    ImportProcessor importProcessor;
    DealCountProcessor dealCountProcessor;

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

    public void process(String fileName) {
	LOGGER.info("Importing file: " + fileName);
	String source = Initializer.configMap.get(ConfigConst.CSV_FILE_LOCATION) + "\\" + fileName;
	List<ValidDeal> dealsList = CsvReader.readCsv(source);
	String firstRowUniqueId = dealsList.get(0).getDealUniqueId();
	if (JpaProcessor.getInstance().saveDeals(dealsList)) {
	    JpaProcessor.getInstance().updateLastRow(firstRowUniqueId);
	    LOGGER.info("Imported file: " + fileName);
	    startCounter();
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

    private void startCounter() {
	dealCountProcessor = new DealCountProcessor();
	new Thread(dealCountProcessor).start();
    }

    static <T> List<List<T>> chopped(List<T> list, final int L) {
	List<List<T>> parts = new ArrayList<List<T>>();
	final int N = list.size();
	for (int i = 0; i < N; i += L) {
	    parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
	}
	return parts;
    }
}
