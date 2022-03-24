package net.ssehub.teaching.exercise_submitter.standalone.exception;

import javax.swing.JFrame;

import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;


/**
 * This class handles exception from the  {@link JobResult}.
 * 
 * @author lukas
 *
 */
public class JobResultException {
    
    /**
     * This method handles exception that comes from replaying, uploading , comparing the Submission.
     *
     * @param <Output> the generic type
     * @param <Exceptiontype> the generic type
     * @param message the general error message drawn in the dialog
     * @param frame the current frame the user has open, can be null
     * @param result the result
     */
    public static <Output, Exceptiontype> void 
        handleSubmissionException(JobResult<Output, Exceptiontype> result, String message, JFrame frame) {
        if (result.getException().isPresent()) {
            Exception ex = (Exception) result.getException().get();
            ExceptionDialog.createExceptionDialog(message + "\nReason: " + ex.getLocalizedMessage(), frame);
            
        } else {
            ExceptionDialog.createExceptionDialog("Downloading submission failed", frame);
        }
    }
    

}
