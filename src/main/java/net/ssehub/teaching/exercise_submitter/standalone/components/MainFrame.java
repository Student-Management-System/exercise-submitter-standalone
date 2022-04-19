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


import net.ssehub.teaching.exercise_submitter.standalone.listener.MenuListener;
import net.ssehub.teaching.exercise_submitter.standalone.listener.ResultListener;
import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;
import net.ssehub.teaching.exercise_submitter.standalone.themes.ThemeManager;
import net.ssehub.teaching.exercise_submitter.standalone.themes.ThemeManager.Theme;

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
            UIManager.setLookAndFeel(ThemeManager.getSavedTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("logo.png")));
        SubmissionListener listener = new SubmissionListener();
        
        ResultListener resultlistener = new ResultListener();
        
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new ResultPanel(resultlistener);
        
        left.add(new SelectionPanel(listener, resultlistener), BorderLayout.CENTER);
        left.add(new SubmissionPanel(listener), BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setContinuousLayout(true);

        this.getContentPane().add(splitPane);
        
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        
        MenuListener menuListener = new MenuListener();
        
        JMenu submitMenu = createSubmitMenu(listener, menuListener);
        menu.add(submitMenu);
        
        JMenu help = createHelpMenu(menuListener);
             
        JMenu themes = createThemeMenu();
        
        menu.add(themes);
        menu.add(help);
        this.pack();
        this.setTitle("Standalone Exercise Submitter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    
    /**
     * Creates the help menue.
     * 
     * @param menuListener
     * @return {@link JMenu}
     */
    private JMenu createHelpMenu(MenuListener menuListener) {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> menuListener.openAbout(this));
        help.add(about);
        return help;
    }
    /**
     * Create the toolbar menue.
     * 
     * @param listener
     * @param menuListener 
     * @return JMenu
     */
    private JMenu createSubmitMenu(SubmissionListener listener, MenuListener menuListener) {
        menuListener.setSubmissionListener(listener);
        JMenu submitMenu = new JMenu("Submission");
        
        JMenuItem submit = new JMenuItem("Submit");
        submit.addActionListener(e -> listener.submit());
        submit.setEnabled(false);
        submit.setToolTipText("You need to select an assignment and a directory"); 
        listener.addPathAndAssignmentSelectionListener(l -> {
            submit.setEnabled(l);
            submit.setToolTipText(l ? "Submit" : "You need to select an assignment and a directory");    
        });
        
        submitMenu.add(submit);
        
        JMenuItem showVersion = new JMenuItem("Show Versions"); 
        showVersion.addActionListener(e -> menuListener.openListVersion(this));
        showVersion.setEnabled(true);
       
        
        submitMenu.add(showVersion);
        
        JMenuItem downloadSubmission = new JMenuItem("Download Submission");
        downloadSubmission.addActionListener(e -> menuListener.downloadSubmission(this));
        downloadSubmission.setEnabled(true);
       
        
        submitMenu.add(downloadSubmission);
        
        JMenuItem compare = new JMenuItem("Compare Submission");
        compare.addActionListener(e -> menuListener.compareSubmissions(this));
        compare.setEnabled(false);
        compare.setToolTipText("You need to select an assignment and a directory");  
        listener.addPathAndAssignmentSelectionListener(l -> {
            compare.setEnabled(l);
            compare.setToolTipText(l ? "Compare" : "You need to select an assignment and a directory");    
        });
        
        submitMenu.add(compare);
        return submitMenu;
    }
    
    /**
     * Creates the theme Menu.
     * 
     * @return {@link JMenu}
     */
    private JMenu createThemeMenu() {
        JMenu themesMenu =  new JMenu("Themes");
        for (Theme theme : ThemeManager.Theme.values()) {
            JMenuItem item = new JMenuItem(theme.toString());
            item.addActionListener(e -> ThemeManager.changeTheme(theme));
            themesMenu.add(item);
        }       
        return themesMenu;
    }

}
