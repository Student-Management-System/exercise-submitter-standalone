package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.util.Optional;

import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer.Version;
import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogCallback;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogResult;
import net.ssehub.teaching.exercise_submitter.standalone.submission.ListVersions;

/**
 * Handles the menu.
 * 
 * @author lukas
 *
 */
public class MenuListener {
    
    private Optional<SubmissionListener> listener = Optional.empty();
    
    /**
     * Creates a new instance of {@link MenuListener}.
     */
    public MenuListener() {
        
    }
    /**
     * Sets the submissionlistener.
     * 
     * @param listener
     */
    public void setSubmissionListener(SubmissionListener listener) {
        this.listener = Optional.ofNullable(listener); 
    }
    /**
     * Open the List Version Actiom.
     * 
     * @param frame
     */
    public void openListVersion(MainFrame frame) {     
        if (listener.isPresent()) {
            DialogCallback<Version> callback = new DialogCallback<Version>() {
                
                @Override
                public void run(DialogResult<Version> result) {
                 
                    
                }
            };
            
            
            ListVersions listVersion = new ListVersions( listener.get().currentAssignment.get(),
                    frame, callback);  
            listVersion.startAsyncAndDisplay();
        }
    }
    

}
