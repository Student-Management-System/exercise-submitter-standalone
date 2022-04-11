package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogResult;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.IDialogCallback;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.SelectAssignmentDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;

/**
 * This lists the current assignments in a dialog.
 * 
 * @author lukas
 *
 */
public class ListAssignments {
    
    private IDialogCallback<Assignment> callback;
    private JFrame frame;
    
    /**
     * Creates a new instatnce {@link OfDouble} {@link ListAssignments}.
     * @param callback
     * @param frame
     */
    public ListAssignments(IDialogCallback<Assignment> callback, JFrame frame) {
        this.callback = callback;
        this.frame = frame;
    }
    
    /**
     * Starts the download and display of the assignments asynchronous.
     * 
     */
    public void startAsync() {
        
        IRunnableJob<List<Assignment>, ApiException> func = new IRunnableJob<List<Assignment>, ApiException>() {

            @Override
            public void run(JobResult<List<Assignment>, ApiException> result) {
                try {
                    result.setOutput(StandaloneSubmitter.getHandler().getManager().getAllAssignments());
                } catch (ApiException e) {
                    result.setException(e);
                }

            }
        };
        
        Job<List<Assignment>, ApiException> job = 
                new Job<List<Assignment>, ApiException>(this::onFinishedDownladingAssignments, func);
        job.start();
        
    }
    /**
     * Triggerd when the assignments are finished downloading or it failed.
     * 
     * @param job
     */
    private void onFinishedDownladingAssignments(Job<List<Assignment>, ApiException> job) {
        if (job.getJobResult().hasSuceeded()) {
            
            IDialogCallback<Assignment> dialogcallback = new IDialogCallback<Assignment>() {
                
                @Override
                public void run(DialogResult<Assignment> result) {
                    callback.run(result);
                    
                }
            };
            SelectAssignmentDialog dialog = new SelectAssignmentDialog(
                    job.getJobResult().getOutput().get(), frame, dialogcallback);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } else {
            ExceptionDialog.createExceptionDialog("Can't retrieve assignments", frame);
        }
    }

}
