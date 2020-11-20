package ui;

import model.RestaurantList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Individual JFrame responsible for selecting from a list of RestaurantLists
 */

public class MergeFrame extends JFrame
                                implements ListSelectionListener {
    int current; // Index of the restaurant list to merge into
    JList list;
    DefaultListModel<RestaurantList> listModel;
    JLabel prompt;
    JButton mergeBtn;

    public MergeFrame(DefaultListModel<RestaurantList> allLists, int currentIdx) {
        super("Merge Lists");
        this.listModel = allLists;
        this.current = currentIdx;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(250, 400));

        // Set up the prompt
        prompt = new JLabel();
        String text = "Select a collection to merge into "
                + listModel.getElementAt(current).getName();
        prompt.setText("<html><p style=\"width:150px\">" + text + "</p></html>");

        // Set up the main list
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        list.setFixedCellWidth(200);
        JScrollPane scrollPane = new JScrollPane(list);

        // Set up the button
        mergeBtn = new JButton("Merge");
        mergeBtn.setActionCommand("Merge");
        mergeBtn.setEnabled(false);

        add(prompt);
        add(scrollPane);
        add(mergeBtn);
        pack();
    }

    public void addListener(ActionListener listener) {
        mergeBtn.addActionListener(listener);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    /**
     * Only enable merge button when selecting something other than the target
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!list.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1 || list.getSelectedIndex() == current) {
                mergeBtn.setEnabled(false);
            } else {
                mergeBtn.setEnabled(true);
            }
        }
    }
}