package net.ssehub.teaching.exercise_submitter.standalone;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;

public class StandaloneSubmitter {


    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        new MainFrame().setVisible(true);
    }
    
}
