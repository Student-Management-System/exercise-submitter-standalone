package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.nio.file.Path;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.lib.submission.SubmissionException;
import net.ssehub.teaching.exercise_submitter.lib.submission.SubmissionResult;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;



/**
 * This class handles the submission of the files.
 * 
 * @author lukas
 *
 */
public class UploadSubmission {
    
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
     * Called when the Submissionjob is finished.
     * 
     * @param job
     */
    private void onFinishedSubmission(Job<SubmissionResult, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {
            SubmissionResult result = job.getJobResult().getOutput().get();
            System.out.println(result.isAccepted());
            
        } else {
            //TODO: exception handling
        }
    }
    

}
