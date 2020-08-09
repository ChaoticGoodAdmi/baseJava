package com.urise.webapp;
import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        File file = new File(".//src//");
        printFile(file);
    }

    private static void printFile(File file) {
        if (file.isDirectory()) {
            File[] directoryContent = file.listFiles();
            if (directoryContent != null)
                for (File subFile : directoryContent) {
                    printFile(subFile);
                }
        } else
            System.out.println(file.getName());
    }
}
