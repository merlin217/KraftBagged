package ui;

import model.Restaurant;
import model.RestaurantList;

import javax.swing.*;
import java.awt.*;

/**
 * Individual JFrame responsible for adding new restaurant list objects
 */
public class RestrListInputFrame extends InputFrame {
    /*
     * Initiates the layout, text field and submit button
     */
    public RestrListInputFrame() {
        super("Create A New List... ");
        setPreferredSize(new Dimension(300, 130));

        JLabel nameLabel = new JLabel("List Name");

        submitBtn.setActionCommand("Submit List");

        add(nameLabel);
        add(nameField);
        add(submitBtn);

        pack();
    }

    // Return a Restaurant List object based on the information in the text field
    public RestaurantList getRestrList() {
        return new RestaurantList(nameField.getText());
    }
}
