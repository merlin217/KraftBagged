package persistence;

import model.User;
import model.Restaurant;
import model.RestaurantList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/*
 * MODIFIED FROM CPSC210/JsonSerializationDemo
 *          GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyUser() {
        try {
            User user = new User("Jack");
            String destination = String.format("./data/%s.json", user.getUsername());
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(user);
            writer.close();

            JsonReader reader = new JsonReader(destination);
            user = reader.read();
            assertEquals("Jack", user.getUsername());
            assertEquals(0, user.numOfLists());
            assertEquals(0, user.getAllRatings().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralUser() {
        Restaurant restaurantA, restaurantB, restaurantC;
        restaurantA = new Restaurant("A");
        restaurantB = new Restaurant("B");
        restaurantC = new Restaurant("C");
        RestaurantList list1 = new RestaurantList("Dinner Deliveries");
        list1.add(Arrays.asList(restaurantA, restaurantB, restaurantC));

        User user = new User("Jack");
        user.addRating(1, 8);
        user.addRating(3, 5);
        user.addList(list1);

        try {
            String destination = String.format("./data/%s.json", user.getUsername());
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(user);
            writer.close();

            JsonReader reader = new JsonReader(destination);
            user = reader.read();
            assertEquals("Jack", user.getUsername());
            assertEquals(1, user.numOfLists());
            assertEquals(3, user.getList(0).size());
            RestaurantList defaultList = user.getList(0);
            checkRestaurant(1, "A", defaultList.get(0));
            checkRestaurant(3, "C", defaultList.get(2));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}