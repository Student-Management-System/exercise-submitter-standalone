package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.formdev.flatlaf.ui.FlatBorder;

import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomCellRenderer;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomTreeCelItem;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomTreeCelItem.FileCategorie;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.FileCounter;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.FileFilter;
import net.ssehub.teaching.exercise_submitter.standalone.listener.ResultListener;
import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

/**
 * Display the selection field in the mainframe.
 * 
 * @author lukas
 * @author adam
 *
 */
public class SelectionPanel extends JPanel {

    private static final long serialVersionUID = 2873287812314470834L;
    private JTree tree;
    private JScrollPane scrpane;
    private FileNumberDisplay filesDisplay;
    private FileCounter filecounter;

    /**
     * Instantiates new SelectionPanel.
     * 
     * @param listener
     * @param resultlistener
     */
    public SelectionPanel(SubmissionListener listener, ResultListener resultlistener) {

        JPanel top = new JPanel(new BorderLayout(5, 5));

        JTextField pathField = new JTextField(30);
        pathField.addActionListener(e -> listener.setSelectedPath(pathField.getText()));
        top.add(pathField, BorderLayout.CENTER);
        
        filecounter = new FileCounter();
        
        listener.addPathSelectionListener((path) -> {
            if (path.isPresent()) {
                filecounter.clear();
                resultlistener.clearRows();
                resultlistener.setResultMessage("");
                setTree(createsNodesForDirectory(
                        new DefaultMutableTreeNode(""), path.orElseThrow().toFile(), filecounter));
            }
        }
        );

        JButton button = new JButton("Choose");
        createButtonAction(listener, pathField, button);
        top.add(button, BorderLayout.EAST);

        tree = new JTree(new DefaultMutableTreeNode(""));
        tree.setCellRenderer(new CustomCellRenderer());
        tree.setRootVisible(false);

        this.scrpane = new JScrollPane(this.tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrpane.setBorder(new FlatBorder());
        this.scrpane.setViewportBorder(null);
        
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(this.scrpane, BorderLayout.NORTH);
        
        filesDisplay = new FileNumberDisplay();
        
      
        middle.add(filesDisplay, BorderLayout.CENTER);
        

        this.setLayout(new BorderLayout());
        this.add(top, BorderLayout.NORTH);
        // add(tree, BorderLayout.CENTER);
        this.add(middle, BorderLayout.CENTER);

    }

    /**
     * Create Button Events.
     * 
     * @param listener
     * @param pathField
     * @param button
     */
    private void createButtonAction(SubmissionListener listener, JTextField pathField, JButton button) {
        button.addActionListener(e -> {
        
            Optional<Path> dialogPath = this.openFileDialog();
            listener.setSelectedPath(dialogPath.isPresent() 
                    ? dialogPath.get().toString() : "No Dir selected");
            if (dialogPath.isPresent()) {
                pathField.setText(dialogPath.get().toString());
            }
            
        });
    }
    /**
     * Method that creates a {@see #DefaultMutableTreeNode} from a dir.
     * 
     * 
     * @param parentNode The parent node to add the nodes for the files to.
     * @param dir The directory to create nodes for.
     * @param counter The counter for counting the files.
     * @return  the node
     */
    public DefaultMutableTreeNode createsNodesForDirectory(
            DefaultMutableTreeNode parentNode, File dir, FileCounter counter) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                FileCategorie category = file.isDirectory() ? FileCategorie.DIRECTORY : FileCategorie.FILE;
                
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(
                        new CustomTreeCelItem(category, file.getName()));
                
                FileFilter filter = new FileFilter(file.getName());
                
                if (filter.isAllowed()) {
                    
                    counter.checkFile(file.getName());
                    
                    parentNode.add(fileNode);
                    
                }
                if (file.isDirectory()) {
                    createsNodesForDirectory(fileNode, file, counter);
                }
            }
        }

        return parentNode;
    }
    
    /**
     * Sets the JTree to an new model.
     * @param node
     */
    public void setTree(DefaultMutableTreeNode node) {
        DefaultTreeModel model = new DefaultTreeModel(node);
        this.tree.setModel(model);
        for (int row = 0; row < this.tree.getRowCount(); row++) {
            this.tree.expandRow(row);
        }
        
        filesDisplay.updateDisplay(filecounter);
        
        if (!filecounter.areJavaFilesAvailable()) {
            JOptionPane.showMessageDialog(this, 
                    "No JavaFiles in selected directory",
                    "Warning!" , JOptionPane.WARNING_MESSAGE);
        }
        
    }
    /**
     * Opens the {@see #JFileChooser}.
     * 
     * @return the Path as an Optional.
     */
    private Optional<Path> openFileDialog() {
        Optional<Path> selectedPath = Optional.empty();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedPath = Optional.ofNullable(file.toPath());
        }
     
        return selectedPath;
    }

}
