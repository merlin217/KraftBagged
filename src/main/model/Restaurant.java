package model;

/*
 * Restaurant object with unique id.
 *  Has a rating field which is the average of all ratings it has received.
 */
public class Restaurant {
    private static int counter = 0; // tracks the number of restaurants created
    private int id;
    private String name;
    private double rating;          // average rating of the restaurant
    private int numRatings;         // number of ratings the restaurant has received

    /*
     * REQUIRES: name length is non-zero
     * EFFECTS: constructs a restaurant object;
     *          updates counter;
     *          sets initial rating to a double between 0 and 10.
     */
    public Restaurant(String name, double initialRating) {
        this.id = ++counter;
        this.name = name;
        this.numRatings = 0;
        if (initialRating > 0 && initialRating <= 10) {
            this.rating = initialRating;
            numRatings++;
        } else {
            this.rating = 0;
        }
    }

    /*
     * EFFECTS: overloads the constructor,
     *          calls the constructor with initialRating = 0
     */
    public Restaurant(String name) {
        this(name, 0);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    /*
     * EFFECTS: returns rating rounded to the closest integer
     */
    public int getRatingRounded() {
        int offset = (rating % 1) < 0.5 ? 0 : 1;
        int roundedRating = (int) this.rating + offset;
        return roundedRating;
    }

    /*
     * REQUIRES: 1 <= newRating <= 10
     * EFFECTS: obtains new aggregate rating
     *          increments the number of ratings received
     *          update the new average rating.
     */
    public void updateRating(double newRating) {
        double aggregateRating = rating * numRatings + newRating;
        numRatings++;
        this.rating = aggregateRating / numRatings;
    }

    /*
     * EFFECTS: returns a string representation of a restaurant object
     */
    @Override
    public String toString() {
        String outputStr = String.format(
                "[id = %d, name = %s, rating = %.2f, numRatings = %d]",
                id, name, rating, numRatings);
        return outputStr;
    }
}
