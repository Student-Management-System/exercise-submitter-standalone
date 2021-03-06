package net.ssehub.teaching.exercise_submitter.standalone.stumgmt;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Optional;
import java.util.Properties;

import net.ssehub.teaching.exercise_submitter.lib.ExerciseSubmitterFactory;
import net.ssehub.teaching.exercise_submitter.lib.ExerciseSubmitterManager;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.AuthenticationException;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.NetworkException;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.UserNotInCourseException;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ConnectionException;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.LoginException;

/***
 * This class handles the {@link ExerciseSubmitterManager} and the overall connection with the 
 * stumgmgt system.
 * 
 * @author lukas
 *
 */
public class StumgmtHandler {
    
    private ExerciseSubmitterManager manager;
    private Optional<Credentials> credentials;
    
    
    /**
     * Instantiates a new stumgmt handler.
     */
    public StumgmtHandler() {
        
    }
    
    /**
     * Inits the manager.
     *
     * @throws LoginException the login exception
     * @throws ConnectionException 
     */
    private void initManager() throws LoginException, ConnectionException {
        
        try {
            Properties prop = new Properties();
            prop.load(StumgmtHandler.class.getResourceAsStream("config.properties"));
            
            ExerciseSubmitterFactory factory = new ExerciseSubmitterFactory();
            factory
            .withUsername(this.credentials.orElseThrow(() 
                    -> new ConnectException(LoginException.NOUSERNAMESAVED)).getUsername())
            .withPassword(new String(this.credentials.orElseThrow(() 
                    -> new ConnectException(LoginException.NOPASSWORDSAVED)).getPassword()))
            .withCourse(prop.getProperty("courseid"))
            .withAuthUrl(prop.getProperty("authurl"))
            .withMgmtUrl(prop.getProperty("mgmturl"))
                .withExerciseSubmitterServerUrl(prop.getProperty("exerciseSubmitterUrl"));
            this.manager = factory.build();
            
        } catch (NetworkException e) {
            throw new ConnectionException(ConnectionException.NONETWORKCONNECTION);
        } catch (UserNotInCourseException e) {
            ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
        } catch (AuthenticationException e) {          
            throw new LoginException(LoginException.USERNAMEORPASSWORDFALSE);
        } catch (ApiException e) {
            ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
        } catch (IOException e) {
            ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
        }
        
        
    }
    
    
    /**
     * Gets the manager.
     *
     * @return the manager
     */
    public synchronized ExerciseSubmitterManager getManager() {
        if (this.manager == null) {
            try {
                this.initManager();
            } catch (LoginException e) {
                ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
            } catch (ConnectionException e) {
                ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
            }
        }        
        return this.manager;
    }
    
    /**
     * Login.
     *
     * @param credentials the credentials
     * @throws LoginException the login exception
     * @throws ConnectionException 
     */
    public void login(Credentials credentials) throws LoginException, ConnectionException {
        //TODO: better throw system
        try {
            this.credentials = Optional.ofNullable(credentials);
            this.initManager();
        } catch (LoginException e) {
            throw e;
        } catch (ConnectionException e) {
            throw e;
        }
    }
}
