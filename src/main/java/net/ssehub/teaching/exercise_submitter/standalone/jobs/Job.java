package net.ssehub.teaching.exercise_submitter.standalone.jobs;

import java.util.function.Consumer;

/**
 * Runs the given function async.
 * 
 * @author lukas
 *
 * @param <Output>
 * @param <ExceptionType>
 */
public class Job<Output, ExceptionType> extends Thread {
    
    private JobResult<Output, ExceptionType> jobResult;
    private Consumer<Job<Output, ExceptionType>> callback;
    private IRunnableJob<Output, ExceptionType> job;
   
    /**
     * Function which is called when the job is done and the job function which will be executed.
     * 
     * @param callback , job is done
     * @param job , job function that gets executed
     */
    public Job(Consumer<Job<Output, ExceptionType>> callback, IRunnableJob<Output, ExceptionType> job) {
        super();
        this.callback = callback;
        this.job = job;
        this.jobResult = new JobResult<Output, ExceptionType>();
    }
    

    @Override
    public void run() {
        this.job.run(jobResult);
        this.callback.accept(this);
    }
    /**
     * Gets the job output.
     * 
     * @return Optional<Output>
     */
    public JobResult<Output, ExceptionType> getJobResult() {
        return this.jobResult;
    }

}
