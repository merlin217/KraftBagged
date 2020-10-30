package persistence;

import model.RestaurantList;
import model.User;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/*
 * MODIFIED FROM CPSC210/JsonSerializationDemo
 *          GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            User user = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyUserFile() {
        JsonReader reader = new JsonReader("./data/emptyUserTest.json");
        try {
            User user = reader.read();
            assertEquals("new user", user.getUsername());
            assertEquals(0, user.numOfLists());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralUser() {
        JsonReader reader = new JsonReader("./data/generalUserTest.json");
        try {
            User user = reader.read();
            assertEquals("general user", user.getUsername());
            assertEquals(-1, user.getLastVisitedId());
            assertEquals(2, user.numOfLists());
            assertEquals(2, user.getList(0).size());

            RestaurantList defaultList = user.getList(0);
            checkRestaurant(1, "Joey", defaultList.get(0));
            checkRestaurant(2, "Mcdonald's", defaultList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
