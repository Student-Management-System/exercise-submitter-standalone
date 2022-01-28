package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import java.util.Optional;


/**
 * The Result from a {@link Dialog} operation.
 * 
 * @author lukas
 *
 * @param <Result>
 */
public class DialogResult<Result> {
    
    private UserAction action;
    private Optional<Result> result = Optional.empty();
    
    /**
     * If the dialog was canceld or not.
     * 
     * @author lukas
     *
     */
    public enum UserAction {
        OK, CANCEL
    }
    
    /**
     * Creates an instance of {@link DialogResult}.
     */
    public DialogResult() {
        this.action = UserAction.CANCEL;
    }
    /**
     * Returns the {@link UserAction}.
     * 
     * @return {@link UserAction}
     */
    public UserAction getAction() {
        return action;
    }

    /**
     * Sets the Useraction.
     * 
     * @param action
     */
    public void setUserAction(UserAction action) {
        this.action = action;
    }
    /**
     * Gets the result from the dialog.
     * 
     * @return Optional<Result>
     */
    public Optional<Result> getResult() {
        return result;
    }
    /**
     * Sets the result.
     * 
     * @param result
     */
    public void setResult(Result result) {
        this.result = Optional.ofNullable(result);
    }

}
