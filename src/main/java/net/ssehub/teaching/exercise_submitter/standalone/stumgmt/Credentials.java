package net.ssehub.teaching.exercise_submitter.standalone.stumgmt;

/**
 * This class holds the username and password from the user as member.
 * 
 * @author lukas
 *
 */
public class Credentials {
    
    private String username;
    private char[] password;
    
    /**
     * Instantiates a new credentials.
     *
     * @param username the username
     * @param password the password
     */
    public Credentials(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public char[] getPassword() {
        return this.password;
    }
    
    

}
