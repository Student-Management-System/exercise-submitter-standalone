package net.ssehub.teaching.exercise_submitter.standalone.jobs;

/**
 * This interface can be impelemented if its is user with {@see #Job}.
 * 
 * @author lukas
 * 
 * @param <Output> the class for the output.
 * @param <ExceptionType> the class for the exception.
 *
 */
public interface IRunnableJob<Output, ExceptionType> {  
    
    /**
     * Main run function. The work to do should be in here.
     *
     * @param result the result
     */
    
    public void run(JobResult<Output, ExceptionType> result);
}

