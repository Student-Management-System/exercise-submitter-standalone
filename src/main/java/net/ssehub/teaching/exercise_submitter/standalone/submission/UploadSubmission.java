package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.lib.submission.Problem;
import net.ssehub.teaching.exercise_submitter.lib.submission.SubmissionException;
import net.ssehub.teaching.exercise_submitter.lib.submission.SubmissionResult;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.components.ResultPanel;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.JobResultException;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;
import net.ssehub.teaching.exercise_submitter.standalone.listener.ResultListener;



/**
 * This class handles the submission of the files.
 * 
 * @author lukas
 *
 */
public class UploadSubmission {
    
    private static Optional<ResultListener> listener = Optional.empty();
    
    private Path dirPath;
    private Assignment assignment;
    
    /**
     * Creates an instance of {@see #UploadSubmission}.
     * 
     * @param dirPath
     * @param assignment
     */
    public UploadSubmission(Path dirPath, Assignment assignment) {
        this.dirPath = dirPath;
        this.assignment = assignment;
    }
    /**
     * Runs the upload prozess Async.
     * 
     * 
     */
    public void startAsync() {
        
        IRunnableJob<SubmissionResult, Exception> func = new IRunnableJob<SubmissionResult, Exception>() {

            @Override
        public void run(JobResult<SubmissionResult, Exception> result) {
                try {
                    result.setOutput(
                        StandaloneSubmitter
                        .getHandler()
                        .getManager()
                        .getSubmitter(assignment)
                        .submit(dirPath.toFile()));
                } catch (IllegalArgumentException | SubmissionException | ApiException e) {
                    result.setException(e);
                }
            
            }
        };
        
        Job<SubmissionResult, Exception> job =
                new Job<SubmissionResult, Exception>(this::onFinishedSubmission, func);
        job.start();
    }
    /**
     * Sets the {@link ResultListener}.
     * 
     * @param listener
     */
    public static void setResultListener(ResultListener listener) {       
        UploadSubmission.listener = Optional.ofNullable(listener);
    }
    
    
    /**
     * Called when the Submissionjob is finished.
     * 
     * @param job
     */
    private void onFinishedSubmission(Job<SubmissionResult, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {
            SubmissionResult result = job.getJobResult().getOutput().get();
            if (result.isAccepted()) {
                JOptionPane.showMessageDialog(null, 
                        "Submission was accepted", "Success!" , JOptionPane.INFORMATION_MESSAGE);
                changeResultLabel("Submission was accepted");
            } else {
                ExceptionDialog.createExceptionDialog(
                        "Submission is NOT accepted see the result on the right side", null);
                changeResultLabel("Submission is NOT accepted");
            }
            resultToTable(result);
           
            
        } else {
            JobResultException.handleSubmissionException(job.getJobResult(), "Submission failed", null);
        }
    }
    
    /**
     * Displays the {@link SubmissionResult} to the {@link ResultPanel}.
     *
     * @param result
     */
    private void resultToTable(SubmissionResult result)  {
        List<Problem> problems = result.getProblems();
        if (!problems.isEmpty() && listener.isPresent()) {
            listener.get().clearRows();
            String[][] rows = new String[problems.size()][3];
            for (int i = 0; i < problems.size(); i++) {
                rows[i][0] = problems.get(i).getCheckName();
                rows[i][1] = problems.get(i).getMessage();
                rows[i][2] = problems.get(i).getFile().orElse(new File("")).toString();                     
            }            
            listener.get().addRows(rows);
        }
        
    }
    
    /**
     * Changes the Resultlable in the {@link ResultPanel}.
     * @param message
     */
    private void changeResultLabel(String message) {
        if (listener.isPresent()) {
            listener.get().setResultMessage(message);
        }
    }
    

}
