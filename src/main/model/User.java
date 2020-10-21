package model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static int counter = 0;
    private int id;
    private String userName;

    // ID of the last restaurant the user visited
    private int lastVisitedId;
    // A user can have multiple restaurant lists
    private ArrayList<RestaurantList> allLists;
    // Keeps track of all ratings the user has made
    private HashMap<Integer, Double> allRatings = new HashMap<>();

    /*
     * EFFECTS: constructs a User object;
     *          sets id as a unique integer and updates counter;
     *          initiates myLists as an arraylist of RestaurantLists,
     */
    public User(String userName) {
        this.id = ++counter;
        this.userName = userName;
        this.lastVisitedId = -1;
        this.allLists = new ArrayList<RestaurantList>();
    }

    public String getUsername() {
        return userName;
    }

    public int getId() {
        return id;
    }

    public int getLastVisitedId() {
        return lastVisitedId;
    }

    public HashMap<Integer, Double> getAllRatings() {
        return allRatings;
    }

    public ArrayList<RestaurantList> getAllLists() {
        return allLists;
    }

    /*
     * EFFECTS: adds a RestaurantList object to myLists
     */
    public void addList(RestaurantList someList) {
        allLists.add(someList);
    }

    /*
     * REQUIRES: restaurant is not null
     * EFFECTS: updates lastVisited
     */
    public void visit(Restaurant restaurant) {
        lastVisitedId = restaurant.getId();
    }

    /*
     * REQUIRES: restaurant is not null
     * EFFECTS: add/update a rating entry in myRatings
     *          updates the score in the corresponding restaurant
     */
    public void rate(Restaurant restaurant, double score) {
        allRatings.put(restaurant.getId(), score);
        restaurant.updateRating(score);
    }

    /*
     * REQUIRES: the rating for the corresponding id exists
     * EFFECTS: returns the user rating of the given restaurant id
     */
    public double getRatingById(int id) {
        return allRatings.get(id);
    }
}
