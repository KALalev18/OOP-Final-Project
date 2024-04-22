package com.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Quiz {
    public void buildQuiz(String municipality, Scanner sc) {

        // Generating quiz info and order of question set to random

        Random r = new Random();
        int score = 0;
        Set<Integer> asked = new HashSet<>();
    
        for (int i = 1; i <= 10; i++) {
            String question, a, b, c, d;
            String correct = "";
    
            int qT;
            do {
                qT = r.nextInt(10); 
            } while (!asked.add(qT));
    
            // the questions (should've been added fetching implementation and different answers for different municipalities)

            switch (qT) {
                case 0:
                    question = "Question " + i + ": What is the population of " + municipality + "?";
                    a = "a) 100,000";
                    b = "b) 150,000";
                    c = "c) 200,000";
                    d = "d) 250,000";
                    correct = "c";
                    break;
                case 1:
                    question = "Question " + i + ": Who is the current mayor of " + municipality + "?";
                    a = "a) John Smith";
                    b = "b) Mary Johnson";
                    c = "c) Michael Brown";
                    d = "d) Sarah Wilson";
                    correct = "b";
                    break;
                case 2:
                    question = "Question " + i + ": What famous landmark is located in " + municipality + "?";
                    a = "a) Statue of Liberty";
                    b = "b) Eiffel Tower";
                    c = "c) Sydney Opera House";
                    d = "d) Golden Gate Bridge";
                    correct = "b";
                    break;
                case 3:
                    question = "Question " + i + ": What does the Finnish word "+ municipality +" mean in English?";
                    a = "a) Lake";
                    b = "b) Harbour";
                    c = "c) River";
                    d = "d) Bay";
                    correct = "d";
                    break;
                case 4:
                    question = "Question " + i + ": In which year was " + municipality + " founded?";
                    a = "a) 1905";
                    b = "b) 1910";
                    c = "c) 1915";
                    d = "d) 1920";
                    correct = "a";
                    break;
                case 5:
                    question = "Question " + i + ": How far is " + municipality + " from Helsinki?";
                    a = "a) 50";
                    b = "b) 100";
                    c = "c) 150";
                    d = "d) 200";
                    correct = "b";
                    break;
                case 6:
                    question = "Question " + i + ": What is the nickname of " + municipality + "?";
                    a = "a) Chicago of Finland";
                    b = "b) Cold of Moscow";
                    c = "c) Big oil land";
                    d = "d) Pipe creating";
                    correct = "a";
                    break;
                case 7:
                    question = "Question " + i + ": Which lake is located in " + municipality + "?";
                    a = "a) No lake";
                    b = "b) Lake Vesijärvi";
                    c = "c) Lake Saimaa";
                    d = "d) Gulf Of Finland";
                    correct = "b";
                    break;
                case 8:
                    question = "Question " + i + ": What is the name of the local hockey team in " + municipality + "?";
                    a = "a) No team";
                    b = "b) Pelicans";
                    c = "c) Jokerit";
                    d = "d) HIFK";
                    correct = "b";
                    break;
                case 9:
                    question = "Question " + i + ": What is the largest park in " + municipality + "?";
                    a = "a) Sports park";
                    b = "b) Hatanpää Arboretum";
                    c = "c) Kumpula Botanic Garden";
                    d = "d) Kaisaniemi Park";
                    correct = "a";
                    break;
                default:
                    question = "Question " + i + ": What is the capital of Finland?";
                    a = "a) Helsinki";
                    b = "b) Stockholm";
                    c = "c) Oslo";
                    d = "d) Copenhagen";
                    correct = "a";
                    break;
            }

            // the logic response and score allocation
    
            System.out.println(question);
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
    
            System.out.print("Enter your answer (a, b, c, d): ");
            String res = sc.nextLine();
    
            if (res.equalsIgnoreCase(correct)) {
                System.out.println("\nCorrect!\n");
                score++;
            } else {
                System.out.println("\nIncorrect! The correct answer is: " + correct+"\n");
            }
        }
    
        System.out.println("\nYour score: " + score + "/10\n");
    }
}
