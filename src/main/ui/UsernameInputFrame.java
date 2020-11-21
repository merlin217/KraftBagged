package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Individual JFrame responsible for entering the username
 */
public class UsernameInputFrame extends InputFrame {

    /*
     * Initiates the layout, text field and submit button
     */
    public UsernameInputFrame() {
        super("Login");
        setPreferredSize(new Dimension(300, 130));

        JLabel nameLabel = new JLabel("Username");

        submitBtn.setMnemonic(KeyEvent.VK_L);
        submitBtn.setText("Login");
        submitBtn.setActionCommand("Login");

        add(nameLabel);
        add(nameField);
        add(submitBtn);

        pack();
    }

    // Return username
    public String getUsername() {
        return nameField.getText();
    }
}
