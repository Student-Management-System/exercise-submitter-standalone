package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.ssehub.teaching.exercise_submitter.standalone.components.tree.FileCounter;

/**
 * Display the filenumber from a specific directory.
 * 
 * @author lukas
 *
 */
public class FileNumberDisplay extends JPanel {
  
    private static final long serialVersionUID = -745015395965198962L;
    
    private JLabel filesLabel;
    private JLabel fileNumber;
    private JLabel javafilesLabel;
    private JLabel javafileNumber;
    
    /**
     * Creates an instance of {@link FileNumberDisplay}.
     */
    public FileNumberDisplay() {
        setLayout(new FlowLayout());
        
        filesLabel = new JLabel("");
        filesLabel.putClientProperty("FlatLaf.styleClass", "h3");
        add(filesLabel);
        fileNumber = new JLabel("");
        fileNumber.putClientProperty("FlatLaf.styleClass", "h3");
        add(fileNumber);
        javafilesLabel = new JLabel("");
        javafilesLabel.putClientProperty("FlatLaf.styleClass", "h3");
        add(javafilesLabel);
        javafileNumber = new JLabel("");
        javafileNumber.putClientProperty("FlatLaf.styleClass", "h3");
        add(javafileNumber);
        
    }
    /**
     * Updates the display to the given {@link FileCounter}.
     * 
     * @param counter
     */
    public void updateDisplay(FileCounter counter) {
        filesLabel.setText("Files: ");
        fileNumber.setText(Integer.toString(counter.getNumberFiles()));
        javafilesLabel.setText("Java Files: ");
        javafileNumber.setText(Integer.toString(counter.getNumberJavaFiles()));
    }

}
