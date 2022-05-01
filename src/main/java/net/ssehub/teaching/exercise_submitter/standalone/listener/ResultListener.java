package net.ssehub.teaching.exercise_submitter.standalone.listener;


import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.ssehub.teaching.exercise_submitter.standalone.components.ResultPanel;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;

/**
 * Handles the data on the {@link ResultPanel}.
 * 
 * @author lukas
 *
 */
public class ResultListener {
    
    private Optional<DefaultTableModel> model = Optional.empty();
    private Optional<JLabel> resultLabel = Optional.empty();
    /**
     * Creates an new instance of {@link ResultListener}.
     */
    public ResultListener() {
        
    }
    /**
     * Sets the table model for the {@link ResultListener}.
     * @param model
     */
    public void setModel(DefaultTableModel model) {
        this.model = Optional.ofNullable(model);
    }
    /**
     * Clears the rows of the result table. Runs on the UI thread.
     * 
     */
    public void clearRows() {
        if (model.isPresent()) {
            try {
                if (!EventQueue.isDispatchThread()) {
                    SwingUtilities.invokeAndWait(() -> {
                        for (int i = 0; i < this.model.get().getRowCount();) {
                            model.get().removeRow(i);
                        }            
                    });
                } else {
                    for (int i = 0; i < this.model.get().getRowCount();) {
                        model.get().removeRow(i);
                    }    
                }
                    
            } catch (InvocationTargetException | InterruptedException e) {
                ExceptionDialog.createExceptionDialog("Cant clear resulttable", null);
            }
        }
    }
    
    /**
     * Adds the rows from a 2d String array to the table. Runs in the UI thread.
     * 
     * @param rows
     */
    public void addRows(String[][] rows) {
        if (model.isPresent()) {
            SwingUtilities.invokeLater(() -> {
                for (String[] row : rows) {
                    model.get().addRow(row);
                }
            });
        }
    }
    /**
     * Sets the resultlabel as a parameter.
     * 
     * @param label
     */
    public void setResultLabel(JLabel label) {
        this.resultLabel = Optional.ofNullable(label);
    }
    /**
     * Sets the message that is displayed in the result label.
     * 
     * @param message
     */
    public void setResultMessage(String message) {
        if (this.resultLabel.isPresent()) {
            SwingUtilities.invokeLater(() -> {
                this.resultLabel.get().setText(message);
            });
        }
    }
   

}
