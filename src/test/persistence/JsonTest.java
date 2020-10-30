package persistence;

import model.Restaurant;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * MODIFIED FROM CPSC210/JsonSerializationDemo
 *          GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonTest {
    protected void checkRestaurant(int id, String name, Restaurant restaurant) {
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
    }
}
