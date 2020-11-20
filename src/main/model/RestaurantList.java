package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/*
 * An ordered list of restaurants,
 *  with function to return a random one on the list as a suggestion.
 */
public class RestaurantList {
    private String name;
    private ArrayList<Restaurant> restaurants;

    /*
     * EFFECTS: constructs an empty ArrayList representing the list of restaurants
     */
    public RestaurantList(String name) {
        this.name = name;
        restaurants = new ArrayList<Restaurant>();
    }

    // EFFECTS: overloads the above constructor, sets name to a default value.
    public RestaurantList() {
        this("My Restaurant List");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    // EFFECTS: returns the number of restaurants in the list
    public int size() {
        return restaurants.size();
    }

    /*
     * REQUIRES: index is not out of bound
     * EFFECTS: return the restaurant corresponding to the index
     * NOTE: method called 'get' instead of 'getRestaurant' to
     *      1. be consistent with the method naming that an ArrayList would have
     *      2. help distinguish between this and getRestaurants()
     */
    public Restaurant get(int index) {
        return restaurants.get(index);
    }

    /*
     * EFFECTS: return false if restaurant is already in 'restaurants'
     *          otherwise, add the restaurant to the list and return true
     */
    public boolean add(Restaurant restaurant) {
        if (restaurants.contains(restaurant)) {
            return false;
        }
        restaurants.add(restaurant);
        return true;
    }

    /*
     * REQUIRES: 'list' is not null;
     *           elements of 'list' can be casted to Restaurant.
     *
     * EFFECTS: overloads the above method;
     *          goes through the 'list' and adds the individual elements
     *        into restaurants
     */
    public void add(Collection list) {
        for (Object element : list) {
            add((Restaurant) element);
        }
    }

    /*
     * EFFECTS: removes a restaurant object from current list and return true
     *          if restaurant does not exist, return false
     */
    public boolean remove(Restaurant restaurant) {
        int idx = restaurants.indexOf(restaurant);
        return remove(idx);
    }

    /*
     * EFFECTS: removes a restaurant object by index
     */
    public boolean remove(int idx) {
        if (idx < 0 || idx >= restaurants.size()) {
            return false;
        }
        restaurants.remove(idx);
        return true;
    }

    /*
     * EFFECTS: returns a random restaurant object which is not the last visited;
     *          if restaurants is empty, return null;
     *          if restaurants has 1 element, return the element.
     */
    public Restaurant suggestRandom(int lastVisitedId) {
        if (restaurants.size() == 0) {
            return null;
        } else if (restaurants.size() == 1) {
            return restaurants.get(0);
        }

        Restaurant suggested = null;
        int suggestedId = lastVisitedId;
        while (suggestedId == lastVisitedId) {
            int randomIdx = (int) (Math.random() * restaurants.size());
            suggested = restaurants.get(randomIdx);
            suggestedId = suggested.getId();
        }
        return suggested;
    }

    // EFFECTS: overloads the above method, where last visited is not defined
    public Restaurant suggestRandom() {
        return suggestRandom(-1);
    }

    /*
     * EFFECTS: Converts the current object to a JSONObject
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("restaurants", restaurantsToJson());
        return json;
    }

    // EFFECTS: returns restaurant objects in restaurants as a json array
    public JSONArray restaurantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Restaurant r : restaurants) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: toString() returns the name of this restaurant list only
    @Override
    public String toString() {
        return this.name;
    }
}
