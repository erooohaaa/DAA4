package org.example.util;

import java.io.File;

public class RunAllDatasets {
    public static void main(String[] args) {
        File dataDir = new File("data");
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("RUNNING: " + file.getName());
                System.out.println("=".repeat(50));

                try {

                    String[] mainArgs = { "data/" + file.getName() };
                    org.example.Main.main(mainArgs);
                } catch (Exception e) {
                    System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }
}