package ui;

import model.*;

import java.util.Scanner;

public class WhereToEatApp {
    private Scanner input;
    private User user1;
    private RestaurantList selectedList;

    /*
     * Partial code borrowed from CPSC210/TellerApp
     */
    public WhereToEatApp() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            printMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
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
        user1 = new User(promptForString("Welcome! Enter a username: "));
        if (user1.getAllLists().size() == 0) {
            user1.addList(new RestaurantList("default"));
        }
        selectedList = user1.getAllLists().get(0);
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

    private void printMenu() {
        System.out.printf("\nWelcome, %s. Your current list is (%s). ",
                user1.getUsername(), selectedList.getName());
        System.out.println("Please Select from:");
        System.out.println("\t1 -> Add A Restaurant");
        System.out.println("\t2 -> Add Restaurants From A List");
        System.out.println("\t3 -> Rate A Restaurant");
        System.out.println("\t4 -> Get A Suggestion");
        System.out.println("\t5 -> View Restaurants");
        System.out.println("\t6 -> Switch To New List");
        System.out.println("\tq -> Quit");
    }

    private void processCommand(String cmd) {
        switch (cmd) {
            case "1":
                doAddRestaurant();
                break;
            case "2":
                doAddList();
                break;
            case "3":
                doRateRestaurant();
                break;
            case "4":
                doGetSuggestion();
                break;
            case "5":
                doViewRestaurants();
                break;
            case "6":
                doNewList();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
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
        if (user1.getAllLists().size() <= 1) {
            System.out.println("You do not have another list! Choose (6) to create a new one. ");
            return;
        }

        printAllLists();
        int choice = promptForIndex("Choose a list to merge with your current list: ",
                0, user1.getAllLists().size() - 1);
        RestaurantList listToAdd = user1.getAllLists().get(choice);
        selectedList.add(listToAdd.getRestaurants());
        System.out.printf("%s successfully merged with %s. \n",
                listToAdd.getName(), selectedList.getName());
    }

    /*
     * Prints all lists the user has.
     */
    private void printAllLists() {
        System.out.printf("\n%s's lists: \n", user1.getUsername());
        for (int i = 0; i < user1.getAllLists().size(); i++) {
            System.out.printf("\t%d -> %s\n", i, user1.getAllLists().get(i).getName());
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
        System.out.printf("\nThere are %d restaurants on list \"%s\": \n",
                selectedList.size(), selectedList.getName());
        for (int i = 0; i < selectedList.getRestaurants().size(); i++) {
            Restaurant restaurant = selectedList.get(i);
            System.out.printf("\t%d -> %s\n", i, restaurant.toString());
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

    private void doViewRestaurants() {
        printRestaurants();
        promptForString("\nEnter any key to return to menu: ");
    }

    /*
     * Switch to an existing list or add a new list
     */
    private void doNewList() {
        int max = user1.getAllLists().size();

        printAllLists();
        System.out.printf("\t%d -> [Create A New List]\n", max);
        int choice = promptForIndex("Choose a new list to switch to: ", 0, max);
        if (choice == max) {
            String listName = promptForString("Enter a name for the new list: ");
            user1.addList(new RestaurantList(listName));
        }
        selectedList = user1.getAllLists().get(choice);
        System.out.printf("Successfully switched to (%s). \n", selectedList.getName());
    }
}
