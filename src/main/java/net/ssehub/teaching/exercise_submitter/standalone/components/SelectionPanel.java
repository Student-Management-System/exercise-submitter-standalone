package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.formdev.flatlaf.ui.FlatBorder;

import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomCellRenderer;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomTreeCelItem;
import net.ssehub.teaching.exercise_submitter.standalone.components.tree.CustomTreeCelItem.FileCategorie;
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

    /**
     * Instantiates new SelectionPanel.
     * 
     * @param listener
     */
    public SelectionPanel(SubmissionListener listener) {

        JPanel top = new JPanel(new FlowLayout());

        JTextField pathField = new JTextField(30);
        pathField.addActionListener((e) -> listener.setSelectedPath(pathField.getText()));
        top.add(pathField);

        JButton button = new JButton("Choose");
        button.addActionListener((e) -> {
            if (pathField.getText().equals("")) {
                Optional<Path> dialogPath = openFileDialog();
                listener.setSelectedPath(dialogPath.isPresent() 
                        ? dialogPath.get().toString() : "No Dir selected");
            } else {
                listener.setSelectedPath(pathField.getText());               
            }
            listener.addPathSelectionListener((path) -> {
                if (path.isPresent()) {
                    setTree(createsNodesForDirectory(new DefaultMutableTreeNode(""), path.orElseThrow().toFile()));
                }
            }
            );

        });
        top.add(button);

        tree = new JTree(new DefaultMutableTreeNode(""));
        tree.setCellRenderer(new CustomCellRenderer());
        tree.setRootVisible(false);

        scrpane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrpane.setBorder(new FlatBorder());
        scrpane.setViewportBorder(null);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        // add(tree, BorderLayout.CENTER);
        add(scrpane, BorderLayout.CENTER);

    }
    /**
     * Method that creates a {@see #DefaultMutableTreeNode} from a dir.
     * 
     * 
     * @param parentNode The parent node to add the nodes for the files to.
     * @param dir The directory to create nodes for.
     * @return  the node
     */
    public DefaultMutableTreeNode createsNodesForDirectory(DefaultMutableTreeNode parentNode, File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                FileCategorie category = file.isDirectory() ? FileCategorie.DIRECTORY : FileCategorie.FILE;
                
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(
                        new CustomTreeCelItem(category, file.getName()));
                
                parentNode.add(fileNode);
                
                if (file.isDirectory()) {
                    createsNodesForDirectory(fileNode, file);
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
