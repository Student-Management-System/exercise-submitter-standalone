package net.ssehub.teaching.exercise_submitter.standalone.jobs;

/**
 * This interface can be impelemented if its is user with {@see #Job}.
 * 
 * @author lukas
 *
 * @param <Output>
 */
public interface IRunnableJob<Output> {  
    /**
     * Main run function. The work to do should be in here.
     * 
     * @return Output
     */
    
    public Output run();
}

