package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import net.ssehub.teaching.exercise_submitter.standalone.listener.MenuListener;
import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

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
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/components/logo.png")));
        SubmissionListener listener = new SubmissionListener();
        
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new ResultPanel();
        
        left.add(new SelectionPanel(listener), BorderLayout.CENTER);
        left.add(new SubmissionPanel(listener), BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setContinuousLayout(true);

        this.getContentPane().add(splitPane);
        
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        
        
        JMenu submitMenu = createSubmitMenu(listener);
        menu.add(submitMenu);
        
        JMenu help = new JMenu("Help");
        help.add(new JMenuItem("About"));
        
//        JMenu themes = new JMenu("Themes");
//        themes.add(new JMenuItem("Light"));
//        themes.add(new JMenuItem("Dark"));
//        
        menu.add(help);
        this.pack();
        this.setTitle("Standalone Exercise Submitter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    /**
     * Create the toolbar menue.
     * 
     * @param listener
     * @return JMenu
     */
    private JMenu createSubmitMenu(SubmissionListener listener) {
        MenuListener menuListener = new MenuListener();
        menuListener.setSubmissionListener(listener);
        JMenu submitMenu = new JMenu("Submission");
        
        JMenuItem submit = new JMenuItem("Submit");
        submit.addActionListener(e -> listener.submit());
        listener.addPathAndAssignmentSelectionListener(l -> {
            submit.setEnabled(l);
            submit.setToolTipText(l ? "Submit" : "You need to select an assignment and a directory");    
        });
        
        submitMenu.add(submit);
        
        JMenuItem showVersion = new JMenuItem("Show Versions"); 
        showVersion.addActionListener(e -> menuListener.openListVersion(this));
        listener.addAssignmentSelectionListener(l -> { 
            showVersion.setEnabled(l.isPresent());
            showVersion.setToolTipText(l.isPresent() ? "Show Versions" : "You need to select an assignment");
        });
        
        submitMenu.add(showVersion);
        
        JMenuItem downloadSubmission = new JMenuItem("Download Submission");
        downloadSubmission.addActionListener(e -> menuListener.downloadSubmission(this));
        listener.addAssignmentSelectionListener(l -> {
            downloadSubmission.setEnabled(l.isPresent());
            downloadSubmission.setToolTipText(l.isPresent() 
                    ? "Download Submission" : "You need to select an assignment");
        });
            
        
        submitMenu.add(downloadSubmission);
        
        JMenuItem compare = new JMenuItem("Compare Submission");
        compare.addActionListener(e -> menuListener.compareSubmissions(this));
        listener.addPathAndAssignmentSelectionListener(l -> {
            compare.setEnabled(l);
            compare.setToolTipText(l ? "Compare" : "You need to select an assignment and a directory");    
        });
        
        submitMenu.add(compare);
        return submitMenu;
    }

}
