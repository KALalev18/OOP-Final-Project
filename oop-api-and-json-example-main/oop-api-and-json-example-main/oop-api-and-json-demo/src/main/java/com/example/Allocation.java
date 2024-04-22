package com.example;

import java.util.HashMap;
import java.util.Scanner;

public class Allocation {
    static void allocateFunds(Scanner sc, String municipality) {
        HashMap<String, Integer> projects = new HashMap<>();
        projects.put("Education", 0);
        projects.put("Healthcare", 0);
        projects.put("Infrastructure", 0);
        projects.put("Environment", 0);
        projects.put("Social welfare", 0); // Venues for allocating

        int startBudget = 5000000; // 5 000 000 euros
        int remainingBudget = startBudget;

        System.out.println("\nWelcome to the fund allocation game for " + municipality + "!\n");
        System.out.println("You have " + startBudget + " euros to allocate to different projects.");

        for (String project : projects.keySet()) { // particular anount allocation
            while (true) {
                System.out.print("Enter the amount to allocate to " + project + ": ");
                int allocation = sc.nextInt();
                sc.nextLine();

                if (allocation < 0 || allocation > remainingBudget) {
                    System.out.println("Invalid allocation amount. Please enter a value between 0 and " + remainingBudget);
                } else {
                    projects.put(project, allocation);
                    remainingBudget -= allocation;
                    break;
                }
            }
        }

        System.out.println("\nAllocation Results for " + municipality + ":\n"); // Allocation results
        for (String project : projects.keySet()) {
            int allocation = projects.get(project);
            System.out.println(project + ": " + allocation + " euros");
        }
        System.out.println("\nRemaining Budget: " + remainingBudget + " euros\n");
    }
}
