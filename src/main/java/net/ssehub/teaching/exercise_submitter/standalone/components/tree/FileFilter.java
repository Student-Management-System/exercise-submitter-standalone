package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This filter can filter specific file extensions.
 * 
 * @author lukas
 *
 */
public class FileFilter {
    private String filename;
    
    private List<String> filtered = new ArrayList<String>();
    
    /**
     * Creates an instance of {@link FileFilter}.
     * @param filename
     */
    public FileFilter(String filename) {
        this.filename = filename;
        this.createFilterWords();
    }
    /**
     * Creates a list of file extension that should be filtered.
     * 
     */
    private void createFilterWords() {
        filtered.add(".class");
    }
    /**
     * Checks if an file extension is not in the {@link #filtered} list.
     * 
     * @return true if not in the {@link #filtered} list.
     */
    public boolean isAllowed() {
        Optional<String> findword = filtered.stream().filter(e -> filename.endsWith(e)).findFirst();     
        return findword.isEmpty();
        
    }
    
    

}
