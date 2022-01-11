package net.ssehub.teaching.exercise_submitter.standalone.jobs;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Runs the given function async.
 * 
 * @author lukas
 *
 * @param <Output>
 */
public class Job<Output> extends Thread {
    
    private Optional<Output> output = Optional.empty();
    private Consumer<Job<Output>> callback;
    private  IRunnableJob<Output> job;
   
    /**
     * Function which is called when the job is done and the job function which will be executed.
     * 
     * @param callback , job is done
     * @param job , job function that gets executed
     */
    public Job(Consumer<Job<Output>> callback, IRunnableJob<Output> job) {
        super();
        this.callback = callback;
        this.job = job;
    }
    
    @Override
    public void run() {
        job.run();
        callback.accept(this);
    }
    /**
     * Gets the job output.
     * 
     * @return Optional<Output>
     */
    public Optional<Output> getOutput() {
        return output;
    }

}
