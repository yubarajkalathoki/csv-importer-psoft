package com.yubaraj.csv.importer.psoft.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yuba Raj Kalathoki
 */
public class FileUtil {

    public static List<String> getFiles(String location) {
        File folder = new File(location);
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                //System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return files;
    }

    public static void main(String[] args) throws IOException {
//        System.out.println("files: " + getFiles("C:\\psoft-files\\csv-files"));
//        System.out.println("size: " + getFiles("C:\\psoft-files\\csv-files").size());

        String source = "D:\\psoft-files\\csv-files\\33JB1UBF1W.csv";
        String target = "D:\\psoft-files\\csv-files\\Archived\\33JB1UBF1W.csv";
        moveFile(source, target);

    }

    public static void moveFile(String source, String target) throws IOException {

        Files.move(FileSystems.getDefault().getPath(source),
                FileSystems.getDefault().getPath(target),
                StandardCopyOption.REPLACE_EXISTING);

    }

    public static void createDirectoryIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void moveFilesFromDir(String source, String target) {
        File sourceDirectory = new File(source);
        File[] files = sourceDirectory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Files.move(FileSystems.getDefault().getPath(source, file.getName()),
                                FileSystems.getDefault().getPath(target),
                                StandardCopyOption.ATOMIC_MOVE);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public static void deleteFilesFromDir(File file) {
        for (File dirFile : file.listFiles()) {
            dirFile.delete();
        }
    }
}
