package com.yubaraj.csv.importer.psoft;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubaraj.csv.importer.psoft.processor.ApplicationProcessor;
import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 * Starter class.
 *
 * @author Yuba Raj Kalathoki
 */
public class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws IOException {
	Initializer initializer = new Initializer();
	if (initializer.init()) {
	    Thread thread = new Thread(new ApplicationProcessor());
	    thread.start();
	} else {
	    LOGGER.info("Failed to initialize configuration file.");
	}
    }
}
