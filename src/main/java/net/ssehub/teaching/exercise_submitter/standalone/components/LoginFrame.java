package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitterVersion;
import net.ssehub.teaching.exercise_submitter.standalone.listener.LoginListener;
import net.ssehub.teaching.exercise_submitter.standalone.themes.ThemeManager;


/**
 * This class creates and handles the login form.
 * 
 * @author lukas
 *
 */
public class LoginFrame extends JFrame {
    
   
    private static final long serialVersionUID = -2056964420650568007L;

    private LoginListener listener;
    
    private JTextField username;
    
    private JPasswordField password;
    
    /**
     * Instantiates a new {@link LoginFrame}.
     */
    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(ThemeManager.getSavedTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("logo.png")));
       
        
        this.setBounds(100, 100, 246, 249);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel loginPane = new JPanel(gridBagLayout);
        
        listener = new LoginListener();
        
        createLoginPane(loginPane);        
        
        getContentPane().add(loginPane, BorderLayout.CENTER);
       
        
        JButton login = new JButton("Login");
        login.addActionListener(this::login);
        getContentPane().add(login, BorderLayout.SOUTH);
        this.pack();
        
        this.getContentPane().add(loginPane);
        this.setTitle("Standalone Exercise Submitter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        createJMenubar();
        
        
    }
    /**
     * Creates the menubar.
     */
    private void createJMenubar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu();
        Icon image = new ImageIcon(MainFrame.class.getResource("information-line.png"));
        menu.setIcon(image);
        JMenuItem version = new JMenuItem("Version");
        version.addActionListener(e -> createVersionMessageBox());
        menu.add(version);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
    /**
     * Create a MessageBox which displays the current {@link StandaloneSubmitterVersion}.
     */
    private void createVersionMessageBox() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, 
                "Version: " +  StandaloneSubmitterVersion.VERSION,
                "Version" , JOptionPane.INFORMATION_MESSAGE));
    
    }
    /**
     * Creates the UI for the Loginform.
     * 
     * @param loginPane
     */
    private void createLoginPane(JPanel loginPane) {
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.putClientProperty("FlatLaf.styleClass", "h2");
        GridBagConstraints gbclblUsername = new GridBagConstraints();
        gbclblUsername.insets = new Insets(0, 0, 5, 5);
        gbclblUsername.gridx = 1;
        gbclblUsername.gridy = 1;
        loginPane.add(lblUsername, gbclblUsername);
        
        username = new JTextField();
        GridBagConstraints gbctxtUsername = new GridBagConstraints();
        gbctxtUsername.insets = new Insets(0, 0, 5, 0);
        gbctxtUsername.fill = GridBagConstraints.HORIZONTAL;
        gbctxtUsername.gridx = 3;
        gbctxtUsername.gridy = 1;
        loginPane.add(username, gbctxtUsername);
        username.setColumns(10);
        username.addActionListener(this::login);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.putClientProperty("FlatLaf.styleClass", "h2");
        GridBagConstraints gbclblPassword = new GridBagConstraints();
        gbclblPassword.insets = new Insets(0, 0, 5, 5);
        gbclblPassword.gridx = 1;
        gbclblPassword.gridy = 3;
        loginPane.add(lblPassword, gbclblPassword);
        
        password = new JPasswordField();
        GridBagConstraints gbctxtPassword = new GridBagConstraints();
        gbctxtPassword.insets = new Insets(0, 0, 5, 0);
        gbctxtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbctxtPassword.gridx = 3;
        gbctxtPassword.gridy = 3;
        loginPane.add(password, gbctxtPassword);
        password.addActionListener(this::login);
    }
    
    /**
     * Performs the login.
     * 
     * @param e Ignored.
     */
    private void login(ActionEvent e) {
        listener.setUsername(username.getText());
        listener.setPassword(password.getPassword());
        listener.login(this);
    }
    
}
