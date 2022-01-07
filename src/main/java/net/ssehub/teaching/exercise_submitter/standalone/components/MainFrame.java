package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 197841797135847656L;
    
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        SubmissionListener listener = new SubmissionListener();
        
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new ResultPanel();
        
        left.add(new SelectionPanel(listener), BorderLayout.CENTER);
        left.add(new SubmissionPanel(listener), BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setContinuousLayout(true);

        add(splitPane);
        
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        
        
        JMenu submitMenu = new JMenu("Submission");
        submitMenu.add(new JMenuItem("Submit"));
        submitMenu.add(new JMenuItem("Show Versions"));
        submitMenu.add(new JMenuItem("Download Submission"));
        submitMenu.add(new JMenuItem("Compare Submission"));
        menu.add(submitMenu);
        
        JMenu help = new JMenu("Help");
        help.add(new JMenuItem("About"));
        menu.add(help);
        
        setTitle("Standalone Exercise Submitter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

}
