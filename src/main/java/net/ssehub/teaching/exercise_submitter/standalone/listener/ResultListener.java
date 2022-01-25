package net.ssehub.teaching.exercise_submitter.standalone.listener;


import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.ssehub.teaching.exercise_submitter.standalone.components.ResultPanel;

/**
 * Handles the data on the {@link ResultPanel}.
 * 
 * @author lukas
 *
 */
public class ResultListener {
    
    private DefaultTableModel model;
    /**
     * Creates an new instance of {@link ResultListener}.
     * 
     * @param model
     */
    public ResultListener(DefaultTableModel model) {
        this.model = model;
    }
    /**
     * Clears the rows of the result table. Runs on the UI thread.
     * 
     */
    public void clearRows() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < this.model.getRowCount(); i++) {
                model.removeRow(i);
            }            
        });
    }
    
    /**
     * Adds the rows from a 2d String array to the table. Runs in the UI thread.
     * 
     * @param rows
     */
    public void addRows(String[][] rows) {
        SwingUtilities.invokeLater(() -> {
            for (String[] row : rows) {
                model.addRow(row);
            }
        });
    }

}
