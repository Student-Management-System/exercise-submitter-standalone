package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.standalone.submission.UploadSubmission;


/**
 * Handles the events for that are relevant to the submission.
 * 
 * @author lukas
 *
 */
public class SubmissionListener {

    protected Optional<Path> currentSelection = Optional.empty();
    protected Optional<Assignment> currentAssignment = Optional.empty();
    
    private List<Consumer<Optional<Path>>> pathSelectionListener = new LinkedList<>();
    private List<Consumer<Optional<Assignment>>> assignmentSelectionListener = new LinkedList<>();
    private List<Consumer<Boolean>> pathAndAssignmentSelectionListener = new LinkedList<>();
    
    /**
     * Adds a PathSelectionListener.
     * @param listener
     */
    public void addPathSelectionListener(Consumer<Optional<Path>> listener) {
        this.pathSelectionListener.add(listener);
        
    }
    /**
     * Add a AssignmentSelectionListener.
     * 
     * @param listener
     */
    public void addAssignmentSelectionListener(Consumer<Optional<Assignment>> listener) {
        this.assignmentSelectionListener.add(listener);
    }
    /**
     * Adds a Path and AssignmentListener.
     * 
     * @param listener
     */
    public void addPathAndAssignmentSelectionListener(Consumer<Boolean> listener) {
        this.pathAndAssignmentSelectionListener.add(listener);
    }
    
    /**
     * Sets the selected Path.
     * 
     * @param path
     */
    public void setSelectedPath(String path) {
        Path p = Path.of(path);
        if (Files.isDirectory(p)) {
            this.currentSelection = Optional.of(p);     
        } else {
            this.currentSelection = Optional.empty();
            JOptionPane.showMessageDialog(null, path + " is not a directory", "Not a directory",
                    JOptionPane.ERROR_MESSAGE);
            // TODO: improve error message
        }
        
        if (currentAssignment.isPresent()) {
            this.pathSelectionListener.stream().forEach(l -> l.accept(currentSelection));
            this.pathAndAssignmentSelectionListener.stream().forEach(l ->
                l.accept(currentSelection.isPresent() && currentAssignment.isPresent()));
        }
    }       
    /**
     * Sets the current selected assignment.
     * 
     * @param assignment
     */
    public void setAssignment(Assignment assignment) {
        this.currentAssignment = Optional.ofNullable(assignment);
        if (currentAssignment.isPresent()) {
            this.assignmentSelectionListener.stream().forEach(l -> l.accept(currentAssignment));
            this.pathAndAssignmentSelectionListener.stream().forEach(l ->
                l.accept(currentSelection.isPresent() && currentAssignment.isPresent()));
        }
    }
    
    /**
     * Submits the current file from the selected path.
     */
    public void submit() {
        UploadSubmission uploadsubmission =
                new UploadSubmission(this.currentSelection.get(), this.currentAssignment.get());
        uploadsubmission.startAsync();
    }
    
    
    
}
