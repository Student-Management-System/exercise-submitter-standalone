package net.ssehub.teaching.exercise_submitter.standalone.exception;

/**
 * Thrown if there are Problem with the Login. SDuch as false password ore username.
 * 
 * @author lukas
 *
 */
public class LoginException extends Exception {
    //TODO: add messages
  
    public static final String USERNAMEFALSE = "";
    

    public static final String PASSWORDFALSE = "";
    
   
    public static final String NOUSERNAMESAVED = "";
    
 
    public static final String NOPASSWORDSAVED = "";
    
     
    private static final long serialVersionUID = 6086567974758084399L;

    /**
     * Instantiates a new login exception.
     *
     * @param message the message
     */
    public LoginException(String message) {
        super(message);
    }

    /**
     * Instantiates a new login exception.
     *
     * @param message the message
     * @param th the th
     */
    public LoginException(String message, Throwable th) {
        super(message, th);
    }

    /**
     * Instantiates a new login exception.
     *
     * @param th the th
     */
    public LoginException(Throwable th) {
        super(th);
    }

}
