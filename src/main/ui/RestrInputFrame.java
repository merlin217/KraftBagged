package ui;

import model.Restaurant;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Individual JFrame responsible for adding new restaurant objects
 */
public class RestrInputFrame extends InputFrame {
    JTextField ratingField;

    /*
     * Initiates the layout, text fields and submit button
     */
    public RestrInputFrame() {
        super("Add A Restaurant... ");
        setPreferredSize(new Dimension(300, 130));

        JLabel nameLabel = new JLabel("Restaurant Name");
        JLabel ratingLabel = new JLabel("Your Rating (optional)");
        ratingField = new JTextField(10);

        add(nameLabel);
        add(nameField);
        add(ratingLabel);
        add(ratingField);
        add(submitBtn);

        pack();
    }

    // Resets text fields of inputFrame
    public void resetText() {
        nameField.setText("");
        ratingField.setText("");
    }

    // Return a restaurant object based on the information in the text fields
    // THROWS NumberFormatException
    public Restaurant getRestaurant() {
        if (ratingField.getText().length() <= 0) {
            return new Restaurant(nameField.getText());
        } else {
            int rating = Integer.parseInt(ratingField.getText());
            return new Restaurant(nameField.getText(), rating);
        }
    }

    // Test method
    public static void main(String[] args) {
        JFrame f = new RestrInputFrame();
        f.setVisible(true);
    }
}
