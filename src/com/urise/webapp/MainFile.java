package com.urise.webapp;

import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        File file = new File(".//src//");
        printFileTree(file);
    }

    private static void printFileTree(File file) {
        printFileTree(file, 1);
    }

    private static void printFileTree(File file, int indent) {
        if (file.isDirectory()) {
            System.out.println(file.getName() + ":");
            File[] directoryContent = file.listFiles();
            if (directoryContent != null)
                for (File subFile : directoryContent) {
                    System.out.print(getIndent(indent));
                    printFileTree(subFile, indent + 1);
                }
        } else
            System.out.println(file.getName());
    }

    private static String getIndent(int indent) {
        return "\t".repeat(Math.max(0, indent));
    }
}
