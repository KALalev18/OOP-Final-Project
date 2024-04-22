package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    // go through ll the .csv and show to the console

    static void readCSV(String filename) {    
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    System.out.print(part + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // search through with full output

    public static void searchCSV(String filename) { 
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            System.out.print("\nEnter city to search: ");
            String text = System.console().readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    if (part.contains(text)) {
                        System.out.println("\n"+line);
                        return;
                    }
                }
            }
            System.out.println("\nNo match found in CSV file.");
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // input more CSV info for different scenario

    public static void searchCSVMore(String fileName) { 
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            System.out.print("\nEnter city to search: ");
            String searchText = System.console().readLine();
            System.out.println("\n");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    if (part.contains(searchText)) {
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
