package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

/**
 * 
 * This class count the javafiles and the generall files.
 * 
 * @author lukas
 *
 */
public class FileCounter {
    
    private int numberJavaFiles = 0;
    private int numberFiles = 0;
     
    /**
     * Creates an instance of {@link FileCounter}.
     */
    public FileCounter() {
        
    }
    
    /**
     * Checks if the files is a java or another and count it.
     * 
     * @param filename
     */
    public void checkFile(String filename) {
        if (filename.endsWith(".java")) {
            numberJavaFiles++;
        }
        numberFiles++;
    }
    /**
     * Clears the counted files.
     */
    public void clear() {
        numberFiles = 0;
        numberJavaFiles = 0;
    }
    /**
     * Returns True if at least one javaFile is counted.
     * 
     * @return bolean
     */
    public boolean areJavaFilesAvailable() {
        boolean value = false;
        if (numberJavaFiles > 0) {
            value = true;
        }
        return value;
    }
    /**
     * Returns the number of counted javas files.
     * 
     * @return Int
     */
    public int getNumberJavaFiles() {
        return numberJavaFiles;
    }
    /**
     * Returns the number of counted files.
     * 
     * @return Int
     */
    public int getNumberFiles() {
        return numberFiles;
    }
}
