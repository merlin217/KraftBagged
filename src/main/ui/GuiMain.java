package ui;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class responsible for the login UI and launching the main panel
 */

public class GuiMain {
    UsernameInputFrame loginFrame;

    public GuiMain() {
        loginFrame = new UsernameInputFrame();
        loginFrame.setVisible(true);
        loginFrame.addListener(new LoginListener());
    }

    /**
     * EFFECTS: Create the MinaPanel and show it.
     */
    void setupMainPanel(String username) {
        loginFrame.setVisible(false);

        //Create and set up the window.
        JFrame frame = new JFrame(username + "'s Restaurants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new MainPanel(username);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * EFFECTS: when login button is pressed,
     */
    public class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Login")) {
                String userName = loginFrame.getUsername();
                setupMainPanel(userName);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GuiMain();
            }
        });
    }
}
