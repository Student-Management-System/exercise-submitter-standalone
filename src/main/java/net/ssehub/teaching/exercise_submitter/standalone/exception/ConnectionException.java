package net.ssehub.teaching.exercise_submitter.standalone.exception;

/**
 * Thrown if there are problems with the connection.
 * 
 * @author lukas
 *
 */
public class ConnectionException extends Exception {
    
    //TODO: add messages
    
    public static final String NONETWORKCONNECTION = "";
    public static final String NOSERVERCONNECTION = "";
    

    private static final long serialVersionUID = -9145088507193391196L;
    

    /**
     * Instantiates a new connection exception.
     *
     * @param message the message
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new connection exception.
     *
     * @param message the message
     * @param th the th
     */
    public ConnectionException(String message, Throwable th) {
        super(message, th);
    }

    /**
     * Instantiates a new connection exception.
     *
     * @param th the th
     */
    public ConnectionException(Throwable th) {
        super(th);
    }

}
