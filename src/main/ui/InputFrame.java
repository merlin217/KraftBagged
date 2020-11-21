package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Base class for UsernameInputFrame, RestrInputFrame and RestrListInputFrame
 */
public class InputFrame extends JFrame implements DocumentListener {
    JTextField nameField;
    JButton submitBtn;

    /*
     * Initiates text field and submit button
     */
    public InputFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());

        nameField = new JTextField(10);
        nameField.getDocument().addDocumentListener(this);

        submitBtn = new JButton("Submit");
        submitBtn.setActionCommand("Submit");
        submitBtn.setMnemonic(KeyEvent.VK_S);
        submitBtn.setEnabled(false);
    }

    // Sets the listener of the submit button
    public void addListener(ActionListener listener) {
        submitBtn.addActionListener(listener);
    }

    public void resetText() {
        nameField.setText("");
    }

    // Required by DocumentListener
    // Enable submit button when text is inserted into 'name' field
    @Override
    public void insertUpdate(DocumentEvent e) {
        submitBtn.setEnabled(true);
    }

    // Required by DocumentListener
    // Disable submit button when there is no text in 'name' field
    @Override
    public void removeUpdate(DocumentEvent e) {
        if (fieldIsEmpty()) {
            submitBtn.setEnabled(false);
        }
    }

    // Required by DocumentListener
    // Enable/disable submit button depending on whether the text field has content
    @Override
    public void changedUpdate(DocumentEvent e) {
        if (fieldIsEmpty()) {
            submitBtn.setEnabled(false);
        } else {
            submitBtn.setEnabled(true);
        }
    }

    // Returns whether name field is empty
    public boolean fieldIsEmpty() {
        return nameField.getText().length() <= 0;
    }
}
