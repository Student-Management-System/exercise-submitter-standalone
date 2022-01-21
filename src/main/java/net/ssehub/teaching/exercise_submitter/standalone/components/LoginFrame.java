package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import net.ssehub.teaching.exercise_submitter.standalone.listener.LoginListener;


/**
 * This class creates and handles the login form.
 * 
 * @author lukas
 *
 */
public class LoginFrame extends JFrame {
    
   
    private static final long serialVersionUID = -2056964420650568007L;

    /**
     * Instantiates a new {@link LoginFrame}.
     */
    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/components/logo.png")));
       
        
        this.setBounds(100, 100, 246, 249);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel loginPane = new JPanel(gridBagLayout);
        
        LoginListener listener = new LoginListener();
        
        this.createLoginPane(loginPane, listener);
        
        
        this.add(loginPane, BorderLayout.CENTER);
       
        
        
        JButton login = new JButton("Login");
        login.setEnabled(false);
        login.addActionListener(e -> listener.login(this)
        );
        listener.setButtonConsumer(e -> login.setEnabled(e));
        this.add(login, BorderLayout.SOUTH);
        this.pack();
        
        this.getContentPane().add(loginPane);
        this.setTitle("Standalone Exercise Submitter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        
    }
    /**
     * Creates the UI for the Loginform.
     * 
     * @param loginPane
     * @param listener
     */
    private void createLoginPane(JPanel loginPane, LoginListener listener) {
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.putClientProperty("FlatLaf.styleClass", "h2");
        GridBagConstraints gbclblUsername = new GridBagConstraints();
        gbclblUsername.insets = new Insets(0, 0, 5, 5);
        gbclblUsername.gridx = 1;
        gbclblUsername.gridy = 1;
        loginPane.add(lblUsername, gbclblUsername);
        
        JTextField txtUsername = new JTextField();
        GridBagConstraints gbctxtUsername = new GridBagConstraints();
        gbctxtUsername.insets = new Insets(0, 0, 5, 0);
        gbctxtUsername.fill = GridBagConstraints.HORIZONTAL;
        gbctxtUsername.gridx = 3;
        gbctxtUsername.gridy = 1;
        loginPane.add(txtUsername, gbctxtUsername);
        txtUsername.setColumns(10);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.putClientProperty("FlatLaf.styleClass", "h2");
        GridBagConstraints gbclblPassword = new GridBagConstraints();
        gbclblPassword.insets = new Insets(0, 0, 5, 5);
        gbclblPassword.gridx = 1;
        gbclblPassword.gridy = 3;
        loginPane.add(lblPassword, gbclblPassword);
        
        JPasswordField txtPassword = new JPasswordField();
        GridBagConstraints gbctxtPassword = new GridBagConstraints();
        gbctxtPassword.insets = new Insets(0, 0, 5, 0);
        gbctxtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbctxtPassword.gridx = 3;
        gbctxtPassword.gridy = 3;
        loginPane.add(txtPassword, gbctxtPassword);
        
        this.createKeyListenerEvents(txtUsername, txtPassword, listener); 
    }
    /**
     * Create the key listener to catch changes in the textboxes.
     * 
     * @param txtUsername
     * @param txtPassword
     * @param listener
     */
    private void createKeyListenerEvents(JTextField txtUsername, JPasswordField txtPassword, LoginListener listener) {
        txtUsername.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent event) {
                listener.setUsername(txtUsername.getText() + Character.toString(event.getKeyChar()));
            }

            @Override
            public void keyPressed(KeyEvent event) {}

            @Override
            public void keyReleased(KeyEvent event) {}
            
        });
        txtPassword.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent event) {
                char[] password = new char[txtPassword.getPassword().length + 1];
                for (int i = 0; i < txtPassword.getPassword().length; i++) {
                    password[i] = txtPassword.getPassword()[i];
                }
                password[password.length - 1] = event.getKeyChar();
                listener.setPassword(password);
            }
            
            @Override
            public void keyReleased(KeyEvent event) {  }
            
            @Override
            public void keyPressed(KeyEvent event) {}
        });
    }
    
    
   
}
