package net.ssehub.teaching.exercise_submitter.standalone;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import net.ssehub.teaching.exercise_submitter.standalone.components.LoginFrame;
import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.StumgmtHandler;
import net.ssehub.teaching.exercise_submitter.standalone.themes.ThemeManager;

/**
 * Handles the Standalone submitter.
 * 
 * @author lukas
 * @author adam
 *
 */
public class StandaloneSubmitter {
    
    private static StumgmtHandler handler = new StumgmtHandler();
    

    /**
     * Main function of the Standalone submitter.
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(ThemeManager.getSavedTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        LoginFrame frame = new LoginFrame();
        frame.setSize(250, 150);
        frame.setVisible(true);
        
        
    }
    /**
     * Gets the handler for which holds the exercise manager.
     * @return {@link StumgmtHandler}
     */
    public static synchronized StumgmtHandler getHandler() {
        return handler;
    }
    
    
}
