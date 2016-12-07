package com.yubaraj.csv.importer.psoft.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yubaraj.csv.importer.psoft.model.ValidDeal;

/**
 * Reads data from csv.
 * 
 * @author Yuba Raj Kalathoki
 */
public class CsvReader {
    public static List<ValidDeal> readCsv(String csvFile) {
        List<ValidDeal> list = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] deals = line.split(cvsSplitBy);
                ValidDeal deal = new ValidDeal(deals[0], deals[1], deals[2], deals[3], deals[4], csvFile);
                list.add(deal);
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
