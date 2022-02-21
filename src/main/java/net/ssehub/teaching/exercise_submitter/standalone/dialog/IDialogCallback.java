package net.ssehub.teaching.exercise_submitter.standalone.dialog;

/**
 * Defines the callback for the VersionListDialog.
 * 
 * @author lukas
 * 
 * @param <Result> , the type of object which is expected as the result.
 *
 */
public interface IDialogCallback<Result> {
    
    /**
     * This function gets called when the callback is triggerd.
     * 
     * @param result
     */
    public void run(DialogResult<Result> result);
    
}
