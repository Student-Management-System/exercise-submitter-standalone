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

import com.formdev.flatlaf.FlatLightLaf;

import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;
import java.awt.Toolkit;

/**
 * Presents the mainframe for the Application.
 * 
 * @author lukas
 * @author Adam
 *
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 197841797135847656L;
    
    /**
     * Instantiates new Mainframe.
     */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/components/logo.png")));
        SubmissionListener listener = new SubmissionListener();
        
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new ResultPanel();
        
        left.add(new SelectionPanel(listener), BorderLayout.CENTER);
        left.add(new SubmissionPanel(listener), BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setContinuousLayout(true);

        getContentPane().add(splitPane);
        
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
        
//        JMenu themes = new JMenu("Themes");
//        themes.add(new JMenuItem("Light"));
//        themes.add(new JMenuItem("Dark"));
//        
        menu.add(help);
        pack();
        setTitle("Standalone Exercise Submitter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
