package com.yubaraj.csv.importer.psoft.psoft;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.yubaraj.csv.importer.psoft.util.CsvReader;
import com.yubaraj.csv.importer.psoft.util.CsvWriter;
import com.yubaraj.csv.importer.psoft.util.CurrencyEnum;

/**
 *
 * @author Yuba Raj Kalathoki
 */
public class CsvWriterTest {

    public static void main(String[] args) {
        writeCsv();
        //readCsv();
    }

    private static void writeCsv() {
        int f = 0;
        while (f != 50) {
            String fileName = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            String sourceFile = "C:\\psoft-files\\csv-files\\" + fileName + ".csv";
            FileWriter fw = null;
            try {
                fw = new FileWriter(sourceFile);
                int i = 0;
                while (i != 100000) {
                    String uniqueId = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
                    String fromCurrency = CurrencyEnum.randomCurrency().toString();
                    String toCurrency = CurrencyEnum.randomCurrency().toString();
                    String timestamp = new Timestamp(System.currentTimeMillis()).toString();
                    int n = new Random().nextInt(100);
                    double d = Math.round((n * 100.0) / 100.0);
                    String dealAmount = String.valueOf(d);
                    CsvWriter.writeLine(fw, Arrays.asList(uniqueId, fromCurrency, toCurrency, timestamp, dealAmount));
                    i++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (fw != null) {
                        fw.flush();
                        fw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            f++;
        }
    }

    @SuppressWarnings("unused")
    private static void readCsv() {
        String sourceFile = "C:\\psoft-files\\csv-files\\J6HETUKQ0Z.csv";
        CsvReader.readCsv(sourceFile);
    }
}
