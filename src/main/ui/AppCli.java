package ui;

import model.*;
import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The Command Line Interface of the app
 */
public class AppCli {
    private Scanner input;
    private User user1;
    private RestaurantList selectedList;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String userFile;
    private static final String COUNTER_FILE = "./data/counter/Restaurants.json";

    /*
     * Partial code borrowed from CPSC210/TellerApp
     */
    public AppCli() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            printMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                saveCounter(); 
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    /*
     * Initialise fields
     */
    private void init() {
        input = new Scanner(System.in);
        String username = promptForString("Welcome! Enter a username: ");
        initUserProfile(username);
        if (user1.numOfLists() == 0) {
            user1.addList(new RestaurantList("default"));
        }
        selectedList = user1.getList(0);
        syncCounter();
    }

    /*
     * Prompts for a string
     */
    private String promptForString(String msg) {
        System.out.print(msg);
        String s;
        s = input.next();
        return s;
    }

    /*
     * Repeatedly prompts for a valid entry of index between min and max (inclusive)
     */
    private int promptForIndex(String msg, int min, int max) {
        int index = -1;
        boolean keepGoing = true;

        while (keepGoing) {
            System.out.print(msg);
            try {
                index = input.nextInt();
            } catch (Exception e) {
                // do nothing
            }
            if (index >= min && index <= max) {
                keepGoing = false;
            } else {
                System.out.println("Selection not valid...");
                input.nextLine();
            }
        }
        return index;
    }

    /*
     * Prints all menu options
     */
    private void printMenu() {
        System.out.printf("\nWelcome, %s. Your current list is (%s). ",
                user1.getUsername(), selectedList.getName());
        System.out.println("Please Select from:");
        System.out.println("\t1 -> Add A Restaurant");
        System.out.println("\t2 -> Add Restaurants From Another List");
        System.out.println("\t3 -> Rate A Restaurant");
        System.out.println("\t4 -> Get A Suggestion");
        System.out.println("\t5 -> View Restaurants");
        System.out.println("\t6 -> Switch To New List");
        System.out.println("\ts -> Save Current Profile");
        System.out.println("\tq -> Quit");
    }

    /*
     * Processes menu commands to trigger the right methods
     */
    private void processCommand(String cmd) {
        switch (cmd) {
            case "1":
                promptAddChoice();
                break;
            case "2":
                doGetSuggestion();
                break;
            case "3":
                doRateRestaurant();
                break;
            case "4":
                doViewRestaurants();
                break;
            case "5":
                doNewList();
                break;
            case "s":
                saveUserProfile();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    /*
     * Second-layer menu.
     * Ask if user wants to add a new restaurant or add all restaurants from a list
     */
    private void promptAddChoice() {
        System.out.println("Add a single restaurant or add from another list? ");
        System.out.println("\t1 -> Add A Single Restaurant");
        System.out.println("\t2 -> Add Restaurants From Another List");

        int choice = promptForIndex("Enter (1) or (2): ", 1, 2);
        switch (choice) {
            case 1:
                doAddRestaurant();
            case 2:
                doAddList();
        }
    }

    /*
     * Adds restaurant object to selectedList
     */
    private void doAddRestaurant() {
        String name = promptForString("\nEnter the name of the restaurant: ");
        if (selectedList.add(new Restaurant(name))) {
            System.out.printf("\n%s added to %s! \n", name, selectedList.getName());
        } else {
            System.out.printf("\n%s already exists! \n", name);
        }
    }

    /*
     * Merges content of a list with selectedList.
     */
    private void doAddList() {
        if (user1.numOfLists() <= 1) {
            System.out.println("You do not have another list! Choose (6) to create a new one. ");
            return;
        }

        printAllLists();
        int choice = promptForIndex("Choose a list to merge with your current list: ",
                0, user1.numOfLists() - 1);
        RestaurantList listToAdd = user1.getList(choice);
        selectedList.add(listToAdd.getRestaurants());
        System.out.printf("%s successfully merged with %s. \n",
                listToAdd.getName(), selectedList.getName());
    }

    /*
     * Prints all lists the user has.
     */
    private void printAllLists() {
        System.out.printf("\n%s's lists: \n", user1.getUsername());
        for (int i = 0; i < user1.numOfLists(); i++) {
            System.out.printf("\t%d -> %s\n", i, user1.getList(i).getName());
        }
    }

    /*
     * Rate a restaurant in selectedList
     */
    private void doRateRestaurant() {
        if (selectedList.size() < 1) {
            System.out.println("Your list is empty! Choose (1) to add a restaurant.");
            return;
        }
        printRestaurants();
        int choice = promptForIndex("Choose a restaurant to rate: ",
                0, selectedList.size() - 1);
        String ratingMsg = String.format(
                "Enter a rating between 1 - 10 for %s: ", selectedList.get(choice).getName());
        int rating = promptForIndex(ratingMsg, 1, 10);
        user1.rate(selectedList.get(choice), rating);
        System.out.println("Rating added. ");
    }

    /*
     * Prints all restaurants in selectedList
     */
    private void printRestaurants() {
        if (selectedList.size() < 1) {
            System.out.println("Your list is empty! Choose (1) to add a restaurant.");
            return;
        }
        System.out.printf("\nThere are %d restaurants on list \"%s\": \n",
                selectedList.size(), selectedList.getName());
        for (int i = 0; i < selectedList.getRestaurants().size(); i++) {
            Restaurant restaurant = selectedList.get(i);
            String output = String.format("\t[%d] ... %s | Avg Rating: %d",
                    i, restaurant.getName(), restaurant.getRatingRounded());

            // Also print user rating if it exists
            if (user1.hasRatedRestaurant(restaurant.getId())) {
                output += String.format(" | Your Rating: %.2f", user1.getRatingById(restaurant.getId()));
            } else {
                output += " | You haven't rated this restaurant";
            }
            System.out.println(output);
        }
    }

    /*
     * Gives user a restaurant suggestion. Prompts for visit.
     */
    private void doGetSuggestion() {
        if (selectedList.size() < 1) {
            System.out.println("Your list is empty! Choose (1) to add a restaurant.");
            return;
        }
        Restaurant suggested = selectedList.suggestRandom(user1.getLastVisitedId());
        System.out.printf("\nHow about going to %s? \n", suggested.getName());
        int choice = promptForIndex("1 -> Yes \t2-> No\n", 1, 2);
        if (choice == 1) {
            user1.visit(suggested);
            System.out.printf("Going to %s... Bon appetit! \n", suggested.getName());
        }
    }

    /*
     * Prints all restaurants on the current list
     */
    private void doViewRestaurants() {
        printRestaurants();
        promptForString("\nEnter any key to return to menu: ");
    }

    /*
     * Switch to an existing list or add a new list
     */
    private void doNewList() {
        int max = user1.numOfLists();

        printAllLists();
        System.out.printf("\t%d -> [Create A New List]\n", max);
        int choice = promptForIndex("Choose a new list to switch to: ", 0, max);
        if (choice == max) {
            String listName = promptForString("Enter a name for the new list: ");
            user1.addList(new RestaurantList(listName));
        }
        selectedList = user1.getList(choice);
        System.out.printf("Successfully switched to (%s). \n", selectedList.getName());
    }

    /*
     * CREDIT: modified from WorkRoomApp.saveWorkRoom() found in CPSC210/JsonSerializationDemo
     *         GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
     *
     * EFFECTS: saves current user to a json file
     */
    private void saveUserProfile() {
        userFile = String.format("./data/%s.json", user1.getUsername());
        jsonWriter = new JsonWriter(userFile);
        try {
            jsonWriter.open();
            jsonWriter.write(user1);
            jsonWriter.close();
            System.out.println("Saved " + user1.getUsername() + " to " + userFile);
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + userFile);
        }
    }

    /*
     * EFFECTS: if a file of the specified name does not exist, initialise a new User
     *          else, try reading user object from the file
     */
    private void initUserProfile(String username) {
        userFile = String.format("./data/%s.json", username);
        File destination = new File(userFile);
        if (!destination.exists()) {
            user1 = new User(username);
            return;
        }

        jsonReader = new JsonReader(userFile);
        try {
            user1 = jsonReader.read();
            System.out.println("User profile found! ");
        } catch (IOException e) {
            System.out.println("Error occurred when reading " + userFile);
        }
    }

    /*
     * EFFECTS: tries to read restaurant counter variable saved in COUNTER_FILE
     */
    private void syncCounter() {
        try {
            jsonReader = new JsonReader(COUNTER_FILE);
            int counter = jsonReader.readCounter();
            if (counter != 0) {
                Restaurant.setCounter(counter);
//                System.out.println("DEBUG: Counter set to " + counter);
            }
        } catch (IOException e) {
            System.out.println("No counter file found. ");
        }
    }

    /*
     * REQUIRES: ./data/counter directory exists
     * EFFECTS: saves the current restaurant counter to COUNTER_FILE
     */
    private void saveCounter() {
        jsonWriter = new JsonWriter(COUNTER_FILE);
        try {
            jsonWriter.open();
            jsonWriter.writeCounter(Restaurant.getCounter());
            jsonWriter.close();
//            System.out.println("DEBUG: Current restaurant counter: " + Restaurant.getCounter());
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + COUNTER_FILE);
        }
    }
}
