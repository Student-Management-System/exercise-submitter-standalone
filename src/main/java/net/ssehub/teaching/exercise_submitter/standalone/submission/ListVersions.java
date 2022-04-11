package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.replay.ReplayException;
import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer.Version;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.IDialogCallback;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.VersionListDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;

/**
 * List the version of a specific assignment.
 * 
 * @author lukas
 *
 */
public class ListVersions {
    
    private Assignment assignment;
    private JFrame frame;
    private IDialogCallback<Version> callback;
    
   
    /**
     * Creates an instance of {@link ListVersions}.
     * 
     * @param callback
     * @param assignment
     * @param frame , can be null
     */
    public ListVersions(Assignment assignment, JFrame frame, IDialogCallback<Version> callback) {
        this.assignment = assignment;
        this.frame = frame;
        this.callback = callback;
    }
    
    /**
     * Starts the listVersion async and display the {@link VersionListDialog}.
     * 
     */
    public void startAsyncAndDisplay() {
        IRunnableJob<List<Version>, Exception> func = new IRunnableJob<List<Version>, Exception>() {
            
            @Override
            public void run(JobResult<List<Version>, Exception> result) {
                try  {
                    result.setOutput(StandaloneSubmitter.getHandler().getManager()
                            .getReplayer(assignment).getVersions());
                } catch (ApiException | IllegalArgumentException | ReplayException e) {
                    result.setException(e);
                }
            }
        };
        
        Job<List<Version>, Exception> job = new Job<List<Version>, Exception>(this::onListVersionFinished, func);
        job.start();
        
    }
    
    /**
     * Called when the ListVersionJob is finished.
     * 
     * @param job
     */
    private void onListVersionFinished(Job<List<Version>, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {           
            SwingUtilities.invokeLater(() -> {
                VersionListDialog dialog = new VersionListDialog(job.getJobResult().getOutput().get(),
                        frame, callback);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            });
        } else {
            ExceptionDialog.createExceptionDialog(job.getJobResult().getException().get().getLocalizedMessage(), null);
        }
    }

}
