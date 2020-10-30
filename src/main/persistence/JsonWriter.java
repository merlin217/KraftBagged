package persistence;

import model.User;
import org.json.JSONObject;

import java.io.*;

/*
 * MODIFIED FROM CPSC210/JsonSerializationDemo
 *          GitHub Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 * Represents a writer that writes JSON representation of user to file
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(User user) {
        JSONObject json = user.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes an integer with key 'counter', made to keep track of restaurant counters
    public void writeCounter(int counter) {
        JSONObject json = new JSONObject();
        json.put("counter", counter);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
