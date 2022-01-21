package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * This class ovrridse the defaultreecellrenderer to make the symbol
 * for an empty folder to a folder icon instead of a file icon.
 * 
 * @author lukas
 */
public class CustomCellRenderer extends DefaultTreeCellRenderer {

    
    private static final long serialVersionUID = 394626640218524456L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if (node.getUserObject() instanceof CustomTreeCelItem) {
                CustomTreeCelItem item = (CustomTreeCelItem) node.getUserObject();
                if (item.isDir()) {
                    if (expanded) {
                        this.setIcon(UIManager.getIcon("Tree.openIcon"));
                    } else {
                        this.setIcon(UIManager.getIcon("Tree.closedIcon"));
                    }
                } else {
                    this.setIcon(UIManager.getIcon("Tree.leafIcon"));
                }
            }
        }
        return this;
    }

}
