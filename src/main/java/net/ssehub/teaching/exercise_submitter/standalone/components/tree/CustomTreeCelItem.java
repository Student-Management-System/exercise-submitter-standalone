package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

/**
 * This class is an Item for the {@see #CustomCellRenderer}.
 * 
 * @author lukas
 *
 */
public class CustomTreeCelItem {

    private FileCategorie categorie;
    private String name;
    
    /**
     * This enum is for setting the categorie for the icons.
     * @author lukas
     *
     */
    public enum FileCategorie {
        DIRECTORY, FILE
    }

    /**
     * Creates an instance of {@see #CustomTreeCellItem}.
     * 
     * @param categorie
     * @param name
     */
    public CustomTreeCelItem(FileCategorie categorie, String name) {
        this.categorie = categorie;
        this.name = name;
    }
    /**
     * Return true if the item is a dir.
     * 
     * @return boolean
     */
    public boolean isDir() {
        boolean result = false;
        if (categorie == FileCategorie.DIRECTORY) {
            result = true;
        }
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
