package persistence;

import model.Restaurant;
import model.RestaurantList;
import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.json.*;

/*
 * MODIFIED FROM CPSC210/JsonSerializationDemo
 *          GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 * Represents a reader that reads a user object from JSON data stored in file
 */
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads user from file and returns it;
    //          if JSONException occurs, return a new user object
    //          throws IOException if an error occurs reading data from file
    public User read() throws IOException {
        String jsonData = readFile(source);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return parseUser(jsonObject);
        } catch (JSONException e) {
            return new User("new user");
        }
    }

    // EFFECTS: read an integer from a simple counter file
    public int readCounter() throws IOException {
        String jsonData = readFile(source);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject.getInt("counter");
        } catch (JSONException e) {
            return 0;
        }
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses user from JSON object and returns it
    private User parseUser(JSONObject jsonObject) throws JSONException {
        String userName = jsonObject.getString("userName");
        int lastVisitedId = jsonObject.getInt("lastVisitedId");
        User user = new User(userName);
        user.setLastVisitedId(lastVisitedId);
        addAllRatings(user, jsonObject);
        addAllLists(user, jsonObject);
        return user;
    }

    // MODIFIES: user
    // EFFECTS: parses allRatings from JSON object and adds it to user
    private void addAllRatings(User user, JSONObject jsonObject) {
        JSONObject allRatings = (JSONObject) jsonObject.get("allRatings");
        for (String key : allRatings.keySet()) {
            int id = Integer.parseInt(key);
            int score = allRatings.getInt(key);
            user.addRating(id, score);
        }
    }

    // MODIFIES: user
    // EFFECTS: parses allLists from JSON object and adds them to user
    private void addAllLists(User user, JSONObject jsonObject) {
        JSONArray allLists = jsonObject.getJSONArray("allLists");
        for (Object json : allLists) {
            user.addList(parseRestaurantLis((JSONObject) json));
        }
    }

    // EFFECTS: returns a JSON object as a RestaurantList object
    private RestaurantList parseRestaurantLis(JSONObject json) {
        String name = json.getString("name");
        JSONArray restaurants = json.getJSONArray("restaurants");

        RestaurantList list = new RestaurantList(name);
        for (Object restaurant: restaurants) {
            list.add(parseRestaurant((JSONObject) restaurant));
        }
        return list;
    }

    // EFFECTS: returns a JSON object as a Restaurant object
    private Restaurant parseRestaurant(JSONObject json) {
        String name = json.getString("name");
        int id = json.getInt("id");
        double rating = json.getDouble("rating");
        int numRatings = json.getInt("numRatings");

        Restaurant restaurant = new Restaurant(name);
        restaurant.setId(id);
        restaurant.setRating(rating);
        restaurant.setNumRatings(numRatings);
        return restaurant;
    }

}
