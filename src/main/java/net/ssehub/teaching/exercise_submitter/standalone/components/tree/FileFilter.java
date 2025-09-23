package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.StumgmtHandler;

import java.io.IOException;
import java.util.*;

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
        Properties prop = new Properties();
        try {
            prop.load(StumgmtHandler.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Could not load config file");
        }
        String filterExpressions = prop.getProperty("excluded");
        filtered.addAll(Arrays.asList(filterExpressions.split(",")));
        // Todo: current filter does not change which files are submitted, only which are displayed
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
