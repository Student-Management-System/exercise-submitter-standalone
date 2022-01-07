package net.ssehub.teaching.exercise_submitter.standalone.listener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

public class SubmissionListener {

    private Optional<Path> currentSelection;
    
    private List<Consumer<Optional<Path>>> pathSelectionListener = new LinkedList<>();
    
    public void addPathSelectionListener(Consumer<Optional<Path>> listener) {
        pathSelectionListener.add(listener);
    }
    
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
        pathSelectionListener.stream().forEach(l -> l.accept(currentSelection));
    }
    
    public void submit() {
        System.out.println("Submitting " + currentSelection);
    }
    
}
