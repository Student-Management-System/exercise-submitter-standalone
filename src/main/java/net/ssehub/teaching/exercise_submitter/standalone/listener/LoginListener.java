package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFrame;

import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.components.LoginFrame;
import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ConnectionException;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.LoginException;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;
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
    
    private Optional<JFrame> frame = Optional.empty();
    
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
        
        this.frame = Optional.ofNullable(frame);
        
        IRunnableJob<Boolean, Exception> func = new IRunnableJob<Boolean, Exception>() {
           
            @Override
            public void run(JobResult<Boolean, Exception> result) {
                try {
                    StandaloneSubmitter.getHandler().login(new Credentials(currentUsername.get(),
                            currentPassword.get()));
                    result.setOutput(true);
                } catch (LoginException | ConnectionException e) {
                    result.setOutput(false);
                    result.setException(e);
                }
                
            }
        };
        
        Job<Boolean, Exception> job = new Job<Boolean, Exception>(this::onFinishedLogin, func);
        job.start();
        
        
    }
    /**
     * Called when the loginjob is finished.
     * 
     * @param job
     */
    public void onFinishedLogin(Job<Boolean, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {           
            this.frame.get().setVisible(false);
            new MainFrame().setVisible(true);
        } else {
            Exception ex = job.getJobResult().getException().get();
            ExceptionDialog.createExceptionDialog(ex.getLocalizedMessage(), this.frame.get());
           
        }
        
    }
    
    
    
}
