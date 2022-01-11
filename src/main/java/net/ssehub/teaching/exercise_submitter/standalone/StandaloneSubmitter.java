package net.ssehub.teaching.exercise_submitter.standalone;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;

/**
 * Handles the Standalone submitter.
 * 
 * @author lukas
 * @author adam
 *
 */
public class StandaloneSubmitter {

    /**
     * Main function of the Standalone submitter.
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        new MainFrame().setVisible(true);
    }
    
}
