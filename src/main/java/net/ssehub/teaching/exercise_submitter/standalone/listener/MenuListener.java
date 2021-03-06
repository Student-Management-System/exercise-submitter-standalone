package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer.Version;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitterVersion;
import net.ssehub.teaching.exercise_submitter.standalone.components.MainFrame;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogResult;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.IDialogCallback;
import net.ssehub.teaching.exercise_submitter.standalone.submission.CompareSubmission;
import net.ssehub.teaching.exercise_submitter.standalone.submission.ListAssignments;
import net.ssehub.teaching.exercise_submitter.standalone.submission.ListVersions;
import net.ssehub.teaching.exercise_submitter.standalone.submission.ReplaySubmission;

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
     * Open the List Version Action.
     * 
     * @param frame
     */
    public void openListVersion(MainFrame frame) {  
        IDialogCallback<Assignment> assignmentCallback = new IDialogCallback<Assignment>() {
       
            @Override
            public void run(DialogResult<Assignment> result) {
              
                IDialogCallback<Version> callback = new IDialogCallback<Version>() {
                    
                    @Override
                    public void run(DialogResult<Version> result) {
                     
                        
                    }
                };
                
                
                ListVersions listVersion = new ListVersions(result.getResult().get(),
                        frame, callback);
                listVersion.startAsyncAndDisplay();

            }
        };
            
        ListAssignments assignments = new ListAssignments(assignmentCallback, frame);  
        assignments.startAsync();
        
    }
    /**
     * Download a version of a selected assignment from a group.
     * 
     * @param mainFrame
     */
    public void downloadSubmission(MainFrame mainFrame) {
        
        IDialogCallback<Assignment> assignmentCallback = new IDialogCallback<Assignment>() {
            
            @Override
            public void run(DialogResult<Assignment> result) {
        
            
                IDialogCallback<Version> callback = new IDialogCallback<Version>() {
    
                    @Override
                    public void run(DialogResult<Version> result) {
                        if (result.getAction() == DialogResult.UserAction.OK && result.getResult().isPresent()) {
                            ReplaySubmission replay = new ReplaySubmission(listener.get().currentAssignment.get(), 
                                    result.getResult().get(), mainFrame);  
                            replay.startAsyncReplay();
                        } else { 
                     
                                if (result.getAction() == DialogResult.UserAction.CANCEL) {
                                    JOptionPane.showMessageDialog(mainFrame, 
                                               "Download submission canceled!", "Warning!" 
                                               , JOptionPane.WARNING_MESSAGE);
                                }  else if (result.getResult().isEmpty()) {
                                    JOptionPane.showMessageDialog(mainFrame, 
                                            "Download submission canceled!: No version selected",
                                            "Warning!" , JOptionPane.WARNING_MESSAGE);
                                }
                        }
                    }
                };
                
                ListVersions listVersions = new ListVersions(result.getResult().get(), mainFrame, callback);
                listVersions.startAsyncAndDisplay();
            
            }
        };
    
        ListAssignments assignments = new ListAssignments(assignmentCallback, mainFrame);  
        assignments.startAsync();
            
    }
    /**
     * Compare the selected file ant the current file on the server if they are identical.
     * 
     * @param frame
     */
    public void compareSubmissions(MainFrame frame) {
        if (listener.isPresent()) {
            CompareSubmission comparesubmissions = new CompareSubmission(frame, listener.get().currentAssignment.get(), 
                    listener.get().currentSelection.get());
            comparesubmissions.startAsync();
        }
    }
    
    /**
     * Opens the about dialog.
     * 
     * @param frame
     */
    public void openAbout(MainFrame frame) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, 
                                        "Version: " +  StandaloneSubmitterVersion.VERSION,
                                        "About" , JOptionPane.INFORMATION_MESSAGE));
        
    }
    
}
