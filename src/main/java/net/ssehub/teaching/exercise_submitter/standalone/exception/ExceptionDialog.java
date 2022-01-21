package net.ssehub.teaching.exercise_submitter.standalone.exception;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Creates ExceptionDailogs for displaying Exceptions.
 * 
 * @author lukas
 *
 */
public class ExceptionDialog {

    /**
     * Create the exception Dialog which runs on the swing UI thread.
     * 
     * @param message
     * @param parent , can be null if no component is parent.
     */
    public static void createExceptionDialog(String message, Component parent) {
        SwingUtilities.invokeLater(new Runnable()  {

            @Override
            public void run() {               
                JOptionPane.showMessageDialog(parent, message, "Error!" , JOptionPane.ERROR_MESSAGE);               
            }
            
            
        });
        
    }
}
