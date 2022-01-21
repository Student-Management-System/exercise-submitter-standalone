package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.util.Optional;
import java.util.function.Consumer;

import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.components.LoginFrame;
import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;
import net.ssehub.teaching.exercise_submitter.standalone.exception.LoginException;
import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.Credentials;

/**
 * Handles the data from the login from including the login button.
 * 
 * @author lukas
 *
 */
public class LoginListener {
    private Optional<String> currentUsername = Optional.empty();
    private Optional<char[]> currentPassword = Optional.empty();
    
    private Optional<Consumer<Boolean>> buttonConsumer = Optional.empty();
    
    
    /**
     * Instantiates a new login listener.
     */
    public LoginListener() {
        
    }
    
    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.currentUsername = Optional.ofNullable(username);
        this.checkIfBothAreFilled();
    }
    
    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(char[] password) {
        this.currentPassword = Optional.ofNullable(password);
        this.checkIfBothAreFilled();
    }
    
    
    /**
     * Sets the button consumer.
     *
     * @param buttonConsumer the new button consumer
     */
    public void setButtonConsumer(Consumer<Boolean> buttonConsumer) {
        this.buttonConsumer = Optional.ofNullable(buttonConsumer);
    }
    
    /**
     * Check if both are filled.
     */
    private void checkIfBothAreFilled() {
        if (this.buttonConsumer.isPresent() && this.currentUsername.isPresent() && this.currentPassword.isPresent()) {
            boolean result = !this.currentUsername.get().equals("") && this.currentPassword.get().length > 0;
            
            this.buttonConsumer.get().accept(result);
        }
    }
    
    /**
     * Try tho lgin the user with the password.
     *
     * @param frame the frame
     */
    public void login(LoginFrame frame) {
        try {
            StandaloneSubmitter.getHandler().login(new Credentials(this.currentUsername.get(),
                    this.currentPassword.get()));
            frame.setVisible(false);
            new MainFrame().setVisible(true);
        } catch (LoginException e) {
            System.out.println("False");
        }
    }
    
    
    
}
