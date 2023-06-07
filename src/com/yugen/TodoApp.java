package com.yugen;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * @author: DatBH
 * @since: 6/7/2023 11:27 AM
 * @description: This is a simple command-line to-do list application written in Java.
 * It allows users to manage their to-do list by adding, removing, and marking items as completed.
 * @update:
 * */
public class TodoApp {

    private static Map<Integer, String> todoList = new LinkedHashMap<>();
    private static Map<Integer, String> completedList = new LinkedHashMap<>();
    private static final Map<String, List<Integer>> categories = new LinkedHashMap<>();
    private static final Map<Integer, String> dueDates = new LinkedHashMap<>();
    private static final Map<Integer, String> completedDueDates = new HashMap<>();

    private static final Scanner sc = new Scanner(System.in);

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
                default -> System.out.println("Invalid!");
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
     * Displays the current to-do list grouped by categories
     */
    public static void showTodoList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MMMM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        System.out.println("To-Do List \n" + formattedDate);

        //Iterate over the categories and display their to-do items
        for (Map.Entry<String, List<Integer>> category : categories.entrySet()) {
            System.out.println(category.getKey() + ":");
            for (Integer itemId : category.getValue()) {
                // Retrieve and display the due date
                String dueDate = dueDates.get(itemId);
                System.out.println("  " + itemId + " " + todoList.get(itemId) + " (Due: " + dueDate + ")");
            }
        }

        System.out.println();

        if (!completedList.isEmpty()) {
            System.out.println("Completed Items");
            for (Map.Entry<Integer, String> entry : completedList.entrySet()) {
                // Retrieve and display the due date
                String dueDate = completedDueDates.get(entry.getKey());
                System.out.println(entry.getKey() + " " + entry.getValue() + " (Completed: " + dueDate + ")");
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

        // Prompt the user to enter a due date
        System.out.println("Enter a due date (DD/MM/YYYY): ");
        String dueDate = sc.nextLine();
        dueDates.put(key, dueDate);

        // Prompt the user to select a category
        System.out.println("Select a category: ");
        for (String category : categories.keySet()) {
            System.out.println(category);
        }
        System.out.println("Or enter a new category: ");
        String category = sc.nextLine();

        // Add the new to-do item to the selected category
        categories.computeIfAbsent(category, k -> new ArrayList<>()).add(key);

    }

    /**
     * Reorders the keys in a map so that they are consecutive integers starting from 1.
     *
     * @param originalMap the map to reorder
     * @return a new map with the same values as the original map but with reordered keys
     */
    public static Map<Integer, String> reorderedKeys(Map<Integer, String> originalMap) {
        Map<Integer, String> updatedMap = new HashMap<>();
        int key = 1;
        for (Map.Entry<Integer, String> entry : originalMap.entrySet()) {
            updatedMap.put(key, entry.getValue());
            key++;
        }
        return updatedMap;
    }

    public static void removeItemFromCategories(int itemId) {
        // Remove the item ID from its category
        for (Map.Entry<String, List<Integer>> entry : categories.entrySet()) {
            List<Integer> itemIds = entry.getValue();
            itemIds.remove(Integer.valueOf(itemId));
            // Remove the category if it no longer contains any items
            if (itemIds.isEmpty()) {
                categories.remove(entry.getKey());
            }
        }
    }

    /**
     * Prompts the user to select a to-do item to remove
     * and removes it from the list.
     */
    public static void removeTodoItem() {
        int choice;
        showTodoList();

        System.out.println("What do you want to remove?");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline character

        if (todoList.containsKey(choice)) {
            todoList.remove(choice);
            dueDates.remove(choice); // remove the due date
            // Remove the item ID from its category
            removeItemFromCategories(choice);
            // Reorder the keys in the map
            todoList = reorderedKeys(todoList);
        } else if (completedList.containsKey(choice)) {
            completedList.remove(choice);
            dueDates.remove(choice);
            // Reorder the keys in the map
            completedList = reorderedKeys(completedList);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Prompts the user to select a to-do item to mark  as completed
     * and moves it to the completed list.
     */
    public static void markAsCompleted() {
        int choice;
        showTodoList();

        System.out.println("What do you want to mark as completed?");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline character

        if (todoList.containsKey(choice)) {
            String item = todoList.remove(choice);
            String dueDate = dueDates.remove(choice); // remove the due date
            int key = completedList.size() + 1;
            completedList.put(key, item);
            completedDueDates.put(key, dueDate); // add the due date to the completedDueDates map
            // Remove the item ID from its category
            removeItemFromCategories(choice);

            // Reorder the keys in the map
            todoList = reorderedKeys(todoList);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}