package ui;

import model.Restaurant;
import model.RestaurantList;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * The main GUI class
 *
 * Modified from:
 *  https://docs.oracle.com/javase/tutorial/uiswing/examples/components
 *    /ListDemoProject/src/components/ListDemo.java
 */

public class MainPanel extends JPanel
        implements ListSelectionListener, ActionListener {

    private static final String ADD_RESTAURANT = "Add Restaurant";
    private static final String ADD_LIST = "Add New List";
    private static final String MERGE_LISTS = "Merge Lists";
    private static final String SAVE = "Save Profile";
    private static final int WIDTH = 500;

    private String userFile; // Location of current user's savefile
    private User currentUser; // current user object

    private JList leftList;
    private JList rightList;
    private DefaultListModel<RestaurantList> leftListModel;
    private DefaultListModel<Restaurant> rightListModel;
    private JButton addRestaurantBtn;
    private JButton mergeListBtn;

    // secondary frames:
    private RestrInputFrame restrInputFrame;
    private RestrListInputFrame listInputFrame;
    private MergeFrame mergeFrame;

    // Setup a panel with two JLists one at LINE_START, one at CENTER
    public MainPanel(String username) {
        super(new BorderLayout());

        initUserProfile(username);
        // set up lists to be displayed
        setUpLists();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        // set up buttons in buttonPane
        setUpButtons(buttonPane);

        JScrollPane leftScrollPane = new JScrollPane(leftList);
        JScrollPane rightScrollPane = new JScrollPane(rightList);

        add(leftScrollPane, BorderLayout.LINE_START);
        add(rightScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(WIDTH, 400));

        setUpSecondaryFrames();
    }

    /**
     * EFFECTS: if a file of the specified name does not exist, initialise a new User
     *          else, try reading user object from the file
     */
    private void initUserProfile(String username) {
        userFile = String.format("./data/%s.json", username);
        File destination = new File(userFile);
        if (!destination.exists()) {
            currentUser = new User(username);
            greetUser(true);
            return;
        }

        JsonReader jsonReader = new JsonReader(userFile);
        try {
            currentUser = jsonReader.read();
            greetUser(false);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                                    "Error occurred when reading " + userFile);
        }
    }

    /**
     * Plays a sound and display a custom icon when a user logs in
     */
    private void greetUser(boolean isNew) {
        // Source: https://www.kronos.com/industry-solutions/food-service/food-service-action
        ImageIcon greetIcon = new ImageIcon("./data/Kronos-Fork-Spoon-cropped.gif");
        SoundPlayer player = new SoundPlayer();
        String title;
        String greetingMsg;

        try {
            player.playChime();
        } catch (Exception e) {
            System.out.println("Exception when playing chime sound: \n" + e.toString());
        }

        if (isNew) {
            title = "New User";
            greetingMsg = "Welcome, " + currentUser.getUsername() + "! ";
        } else {
            title = "User Found";
            greetingMsg = "Welcome back, " + currentUser.getUsername() + "! ";
        }

        JOptionPane.showMessageDialog(this,
                greetingMsg, title, JOptionPane.ERROR_MESSAGE, greetIcon);
    }

    /**
     * EFFECTS: set up the secondary (pop-up) frames
     */
    private void setUpSecondaryFrames() {
        restrInputFrame = new RestrInputFrame();
        restrInputFrame.setVisible(false);
        restrInputFrame.addListener(new SubmitButtonListener(this));

        listInputFrame = new RestrListInputFrame();
        listInputFrame.setVisible(false);
        listInputFrame.addListener(new SubmitButtonListener(this));
    }

    /**
     * EFFECTS: set up list models, and
     *          add listModels to corresponding lists, and set up display settings
     */
    private void setUpLists() {
        rightListModel = new DefaultListModel<Restaurant>();
        leftListModel = new DefaultListModel<RestaurantList>();
        updateLeftList();

        leftList = new JList(leftListModel);
        leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftList.setSelectedIndex(-1);
        leftList.addListSelectionListener(this);
        leftList.setVisibleRowCount(5);
        leftList.setPreferredSize(new Dimension(200, 100));

        rightList = new JList(rightListModel);
        rightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rightList.setSelectedIndex(-1);
        rightList.setVisibleRowCount(5);
    }

    /**
     * Set up JButton fields of this class
     */
    private void setUpButtons(JPanel panel) {
        addRestaurantBtn = new JButton(ADD_RESTAURANT);
        addRestaurantBtn.setActionCommand(ADD_RESTAURANT);
        addRestaurantBtn.addActionListener(this);
        addRestaurantBtn.setEnabled(false);

        JButton addListBtn = new JButton(ADD_LIST);
        addListBtn.setActionCommand(ADD_LIST);
        addListBtn.addActionListener(this);

        mergeListBtn = new JButton(MERGE_LISTS);
        mergeListBtn.setActionCommand(MERGE_LISTS);
        mergeListBtn.addActionListener(this);
        mergeListBtn.setEnabled(false);

        JButton saveBtn = new JButton(SAVE);
        saveBtn.setActionCommand(SAVE);
        saveBtn.addActionListener(this);

        panel.add(addRestaurantBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(addListBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(mergeListBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(saveBtn);
    }

    /**
     * EFFECTS: clear leftList and read all the data from current user
     */
    private void updateLeftList() {
        leftListModel.clear();
        for (int i = 0; i < currentUser.numOfLists(); i++) {
            leftListModel.addElement(currentUser.getList(i));
        }
    }

    /**
     * EFFECTS: refresh elements of the currently selected restaurantList into the rightList column
     */
    void updateRightList() {
        rightListModel.clear();
        RestaurantList selected = getSelectedRestaurantList();
        if (selected != null) {
            for (int i = 0; i < selected.size(); i++) {
                rightListModel.addElement(selected.get(i));
            }
        }
    }

    /**
     * Event listener for when left list selection changes
     *
     * EFFECTS: When the selection in leftList changes,
     *          (1) refresh the content of right list to the appropriate collection
     *          (2) activate/deactivate addRestaurantBtn and mergeListBtn
     * REQUIRES: Members of leftList are Objects of RestaurantList
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!leftList.getValueIsAdjusting()) {
            // When there is no selection
            if (leftList.getSelectedIndex() == -1) {
                addRestaurantBtn.setEnabled(false);
                mergeListBtn.setEnabled(false);
            } else {
                updateRightList();
                addRestaurantBtn.setEnabled(true);
                mergeListBtn.setEnabled(true);
            }

            if (leftListModel.getSize() <= 1) {
                mergeListBtn.setEnabled(false);
            }
        }
    }

    /**
     * EFFECTS: returns the currently selected restaurant list
     */
    public RestaurantList getSelectedRestaurantList() {
        int idx = leftList.getSelectedIndex();
        if (idx == -1) {
            return null;
        } else {
            return leftListModel.getElementAt(idx);
        }
    }

    /**
     * triggers correct windows depending on which button is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ADD_RESTAURANT)) {
            // Reset and open the inputFrame window
            restrInputFrame.resetText();
            restrInputFrame.setVisible(true);
        }
        if (e.getActionCommand().equals(ADD_LIST)) {
            listInputFrame.resetText();
            listInputFrame.setVisible(true);
        }
        if (e.getActionCommand().equals(MERGE_LISTS)) {
            mergeFrame = new MergeFrame(leftListModel, leftList.getSelectedIndex());
            mergeFrame.addListener(new MergeButtonListener());
            mergeFrame.setVisible(true);
        }
        if (e.getActionCommand().equals(SAVE)) {
            int result = JOptionPane.showConfirmDialog(this,
                    "Save current profile? ", "Saving Profile",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                saveUserProfile();
            }
        }
    }

    /**
     * saves current user to 'userFile'
     */
    private void saveUserProfile() {
        userFile = String.format("./data/%s.json", currentUser.getUsername());
        JsonWriter jsonWriter = new JsonWriter(userFile);
        try {
            jsonWriter.open();
            jsonWriter.write(currentUser);
            jsonWriter.close();
            System.out.println("Saved " + currentUser.getUsername() + " to " + userFile);
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + userFile);
        }
    }

    /**
     * EFFECTS: when 'submit' button is pressed on restrInputFrame,
     *          add the new restaurant object to the current RestaurantList
     *          when pressed in listInputFrame,
     *          add the new RestaurantList to leftList and update rightList
     */
    class SubmitButtonListener implements ActionListener {
        Component parent;

        public SubmitButtonListener(Component parentComp) {
            parent = parentComp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Submit")) {
                Restaurant restaurant;
                try {
                    restaurant = restrInputFrame.getRestaurant();
                    // if not successfully added to list
                    if (!getSelectedRestaurantList().add(restaurant)) {
                        JOptionPane.showMessageDialog(parent, "Restaurant already exists! ");
                    } else {
                        updateRightList();
                        restrInputFrame.setVisible(false);
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(parent, "Invalid Rating. Please enter an integer. ");
                }
            }

            if (e.getActionCommand().equals("Submit List")) {
                RestaurantList list = listInputFrame.getRestrList();
                currentUser.addList(list);
                updateLeftList();
                leftList.setSelectedIndex(leftListModel.size() - 1);
                updateRightList();
                listInputFrame.setVisible(false);
            }
        }
    }

    /**
     * EFFECTS: when merge button is pressed in mergeFrame,
     *          merge the 'selected' restaurant list into the 'current' one
     *          and remove the 'selected' restaurant list from 'leftList'
     */
    class MergeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Merge")) {
                RestaurantList current = getSelectedRestaurantList();
                int selectedIdx = mergeFrame.getSelectedIndex();
                if (selectedIdx != -1) {
                    current.add(leftListModel.getElementAt(selectedIdx).getRestaurants());
                    currentUser.removeListAt(selectedIdx);
                    updateLeftList();
                    leftList.setSelectedIndex(-1);
                    updateRightList();
                }
                mergeFrame.setVisible(false);
            }
        }
    }
}
