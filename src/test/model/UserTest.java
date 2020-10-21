package model;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user1, user2;
    Restaurant restaurant1, restaurant2, restaurant3;

    @BeforeEach
    public void setUp() {
        user1 = new User("TestUser1");
        user2 = new User("TestUser2");

        restaurant1 = new Restaurant("Frank's");
        restaurant2 = new Restaurant("John's", 8);
        restaurant3 = new Restaurant("Jack's",  12);
    }

    @Test
    // Test id, name and lastVisited
    public void test1() {
        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertEquals("TestUser1", user1.getUsername());
        assertEquals("TestUser2", user2.getUsername());
        assertEquals(-1, user1.getLastVisitedId());
        user1.visit(restaurant3);
        assertEquals(restaurant3.getId(), user1.getLastVisitedId());
    }

    @Test
    // Test ratings
    public void test2() {
        assertEquals(0, user1.getAllRatings().size());
        user1.rate(restaurant1, 10);
        user1.rate(restaurant2, 10);
        user1.rate(restaurant1, 5);
        assertEquals(5, user1.getRatingById(restaurant1.getId()));
        assertEquals(10, user1.getRatingById(restaurant2.getId()));
        assertEquals(2, user1.getAllRatings().size());
    }

    @Test
    // Test lists
    public void test3() {
        RestaurantList testList = new RestaurantList();
        testList.add(Arrays.asList(restaurant1, restaurant2));
        user1.addList(testList);
        assertEquals(2, user1.getAllLists().get(0).size());
        testList.add(restaurant3);
        assertEquals(3, user1.getAllLists().get(0).size());
    }
}