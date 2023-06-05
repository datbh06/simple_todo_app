package com.yugen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TodoApp {
    private static Map<Integer, String> todoList = new LinkedHashMap<>();
    private static Map<Integer, String> completedList = new LinkedHashMap<>();
    private static int itemCount = 0;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int menuItem = -1;

        while (menuItem != 0) {
            menuItem = displayMenu();
            switch (menuItem) {
                case 1 -> showTodoList();
                case 2 -> addTodoItem();
                case 3 -> removeTodoItem();
                case 4 -> markAsCompleted();
                case 0 -> System.out.println("Exiting the program. Goodbye!");
            }
        }
        sc.close();
    }

    /**
     * Displays the main menu and prompts the user to enter a menu choice.
     *
     * @return the user's menu choice
     */
    public static int displayMenu() {
        int choice;
        System.out.println("Main Menu");
        System.out.println("=========================");
        System.out.println("1. Display to-do list");
        System.out.println("2. Add item to list");
        System.out.println("3. Remove item from list");
        System.out.println("4. Mark item as completed");
        System.out.println("0. Exit the program");
        System.out.println();
        do {
            System.out.print("Enter choice: ");
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.print("Enter choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline character
        } while (choice < 0 || choice > 4);
        return choice;
    }

    /**
     * Displays the current to-do list.
     */
    public static void showTodoList() {
        System.out.println("To-Do List");

        for (Map.Entry<Integer, String> entry : todoList.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();

        if (!completedList.isEmpty()) {
            System.out.println("Completed Items");
            for (Map.Entry<Integer, String> entry : completedList.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * Prompts the user to enter a new to-do item and adds it to the list.
     */
    public static void addTodoItem() {
        String item;
        do {
            System.out.println("Enter an item: ");
            item = sc.nextLine();
        } while (item.isEmpty());
        int key = todoList.size() + 1;
        todoList.put(key, item);
    }

    /**
     * Prompts the user to select a to-do item to remove and removes it from the list.
     */
    public static void removeTodoItem() {
        int choice;
        showTodoList();

        System.out.println("What do you want to remove?");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline character

        if (todoList.containsKey(choice)) {
            todoList.remove(choice);
            // Reorder the keys in the map
            Map<Integer, String> updatedTodoList = new LinkedHashMap<>();
            int newKey = 1;
            for (Map.Entry<Integer, String> entry : todoList.entrySet()) {
                updatedTodoList.put(newKey, entry.getValue());
                newKey++;
            }
            todoList = updatedTodoList;
        } else if (completedList.containsKey(choice)) {
            completedList.remove(choice);
            // Reorder the keys in the map
            Map<Integer, String> updatedCompletedList = new LinkedHashMap<>();
            int newKey = 1;
            for (Map.Entry<Integer, String> entry : completedList.entrySet()) {
                updatedCompletedList.put(newKey, entry.getValue());
                newKey++;
            }
            completedList = updatedCompletedList;
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Prompts the user to select a to-do item to mark as completed and moves it to the completed list.
     */
    public static void markAsCompleted() {
        int choice;
        showTodoList();

        System.out.println("What do you want to mark as completed?");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline character

        if (todoList.containsKey(choice)) {
            String item = todoList.remove(choice);
            int key = completedList.size() + 1;
            completedList.put(key, item);
            // Reorder the keys in the map
            Map<Integer, String> updatedTodoList = new LinkedHashMap<>();
            int newKey = 1;
            for (Map.Entry<Integer, String> entry : todoList.entrySet()) {
                updatedTodoList.put(newKey, entry.getValue());
                newKey++;
            }
            todoList = updatedTodoList;
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
