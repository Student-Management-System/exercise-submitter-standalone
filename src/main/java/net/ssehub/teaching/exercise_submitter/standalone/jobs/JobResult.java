package net.ssehub.teaching.exercise_submitter.standalone.jobs;

import java.util.Optional;

/**
 * This class handles the result of a {@see #Job}.
 * 
 * @author lukas
 *
 * @param <Output>
 * @param <ExceptionType>
 */
public class JobResult<Output, ExceptionType> {
    
    private Optional<Output> output = Optional.empty();
    private Optional<ExceptionType> exception = Optional.empty();
    
    /**
     * Instantiates a new job result.
     */
    public JobResult( ) {
        
    }
    
    /**
     * Sets the output.
     *
     * @param output the new output
     */
    public void setOutput(Output output) {
        this.output = Optional.ofNullable(output);
    }
    
    /**
     * Gets the output.
     *
     * @return the output
     */
    public Optional<Output> getOutput() {
        return this.output;
    }
    
    /**
     * Sets the exception.
     *
     * @param exception the new exception
     */
    public void setException(ExceptionType exception) {
        this.exception = Optional.ofNullable(exception);
    }
    
    /**
     * Gets the exception.
     *
     * @return the exception
     */
    public Optional<ExceptionType> getException() {
        return this.exception;
    }
    
    /**
     * Checks for suceeded.
     *
     * @return true, if successful
     */
    public boolean hasSuceeded() {
        boolean result = false;
        if (this.output.isPresent() && this.exception.isEmpty()) {
            result = true;
        }
        return result;
    }

}
