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
     */
    private void initManager() throws LoginException {
        
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
            //TODO: good exception handling
        } catch (UserNotInCourseException e) {
              
        } catch (AuthenticationException e) {          
            throw new LoginException(LoginException.USERNAMEFALSE);
        } catch (ApiException e) {
            
        } catch (IOException e) {
          
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }        
        return this.manager;
    }
    
    /**
     * Login.
     *
     * @param credentials the credentials
     * @throws LoginException the login exception
     */
    public void login(Credentials credentials) throws LoginException {
        //TODO: better throw system
        try {
            this.credentials = Optional.ofNullable(credentials);
            this.initManager();
        } catch (LoginException e) {
            throw e;
        }
    }
}
