package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * User object, initialised when app starts
 *  Can have multiple RestaurantLists
 *  Keeps track of all restaurant ratings the user has made
 *  Keeps track of the last restaurant the user visited
 */
public class User {
    private String userName;

    // ID of the last restaurant the user visited
    private int lastVisitedId;
    // A user can have multiple restaurant lists
    private ArrayList<RestaurantList> allLists;
    // Keeps track of all ratings the user has made
    private Map<Integer, Integer> allRatings = new HashMap<>();

    /*
     * EFFECTS: constructs a User object;
     *          sets id as a unique integer and updates counter;
     *          initiates myLists as an arraylist of RestaurantLists,
     */
    public User(String userName) {
        this.userName = userName;
        this.lastVisitedId = -1;
        this.allLists = new ArrayList<RestaurantList>();
    }

    public String getUsername() {
        return userName;
    }

    public int getLastVisitedId() {
        return lastVisitedId;
    }

    public Map<Integer, Integer> getAllRatings() {
        return allRatings;
    }

    public void setLastVisitedId(int id) {
        lastVisitedId = id;
    }

    // EFFECTS: returns the number of restaurant lists
    public int numOfLists() {
        return allLists.size();
    }

    // EFFECTS: Returns one of the restaurant lists by index
    public RestaurantList getList(int index) {
        return allLists.get(index);
    }

    // EFFECTS: puts score to a corresponding id in allLists HashMap
    public void addRating(int id, int score) {
        allRatings.put(id, score);
    }

    // EFFECTS: adds a RestaurantList object to myLists
    public void addList(RestaurantList someList) {
        allLists.add(someList);
    }

    // EFFECTS: remove (and return) a list at specified index
    public RestaurantList removeListAt(int idx) {
        return allLists.remove(idx);
    }

    /*
     * REQUIRES: restaurant is not null
     * EFFECTS: updates lastVisited
     */
    public void visit(Restaurant restaurant) {
        setLastVisitedId(restaurant.getId());
    }

    /*
     * REQUIRES: restaurant is not null
     * EFFECTS: add/update a rating entry in myRatings
     *          updates the score in the corresponding restaurant
     */
    public void rate(Restaurant restaurant, int score) {
        addRating(restaurant.getId(), score);
        restaurant.updateRating((double)score);
    }

    /*
     * REQUIRES: the rating for the corresponding id exists
     * EFFECTS: returns the user rating of the given restaurant id
     */
    public double getRatingById(int id) {
        return allRatings.get(id);
    }

    /*
     * EFFECTS: returns whether allRatings contains a rating for the specified restaurant
     */
    public boolean hasRatedRestaurant(int id) {
        return allRatings.containsKey(id);
    }

    /*
     * EFFECTS: Converts the user class to a json object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("userName", userName);
        json.put("lastVisitedId",lastVisitedId);
        json.put("allLists", allListsToJson());
        json.put("allRatings", allRatingsToJson());

        return json;
    }

    /*
     * EFFECTS: Converts each RestaurantList in allLists to a json object,
     *          then return them as a JSONArray
     */
    public JSONArray allListsToJson() {
        JSONArray json = new JSONArray();

        for (RestaurantList rl : allLists) {
            json.put(rl.toJson());
        }

        return json;
    }

    /*
     * EFFECTS: convert allRatings Map to a json object
     *          each key is treated as a field of the object
     */
    public JSONObject allRatingsToJson() {
        JSONObject json = new JSONObject();

        for (Integer id : allRatings.keySet()) {
            json.put(id.toString(), allRatings.get(id));
        }

        return json;
    }
}
