package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JButton;
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

            listener.setSelectedPath(pathField.getText());
            listener.addPathSelectionListener((path) -> {
                if (path.isPresent()) {
                    setTree(createNodes(new DefaultMutableTreeNode("File Tree"), null, path.orElseThrow().toFile()));
                }
            }
            );

        });
        top.add(button);

        tree = new JTree(new DefaultMutableTreeNode("File Tree"));
        tree.setCellRenderer(new CustomCellRenderer());

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
     * @param node the root node.
     * @param currentFile always set to null.
     * @param dir the dir that should be noded.
     * @return  the node
     */
    public DefaultMutableTreeNode createNodes(DefaultMutableTreeNode node, File currentFile, File dir) {
        if (currentFile == null) {
            currentFile = dir;
        }
        if (currentFile.isDirectory()) {
            File[] files = currentFile.listFiles();
            if (files != null && files.length > 0) {
                DefaultMutableTreeNode subDir = new DefaultMutableTreeNode(
                        new CustomTreeCelItem(FileCategorie.DIRECTORY, currentFile.getName()));
                for (int i = 0; i < files.length; i++) {
                    node.add(createNodes(subDir, files[i], dir));
                }

            } else {
                node.add(new DefaultMutableTreeNode(
                        new CustomTreeCelItem(FileCategorie.DIRECTORY, currentFile.getName())));
            }
        } else {
            DefaultMutableTreeNode file = new DefaultMutableTreeNode(
                    new CustomTreeCelItem(FileCategorie.FILE, currentFile.getName()));
            node.add(file);
        }

        return node;
    }
    
    /**
     * Sets the JTree to an new model.
     * @param node
     */
    public void setTree(DefaultMutableTreeNode node) {
        DefaultTreeModel model = new DefaultTreeModel(node);
        this.tree.setModel(model);
    }

}
