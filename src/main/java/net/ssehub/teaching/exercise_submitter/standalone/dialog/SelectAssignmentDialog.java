package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogResult.UserAction;

/**
 * Dialog for displaying assignments in a table.
 * 
 * @author lukas
 *
 */
public class SelectAssignmentDialog extends Dialog<Assignment> {

    private static final long serialVersionUID = -8590311679935875621L;

    private List<Assignment> assignments;
    private JFrame frame;
    private JTable table;

    /**
     * Create a new instance of {@link SelectAssignmentDialog}.
     * 
     * @param assignments
     * @param frame
     * @param callback
     */
    public SelectAssignmentDialog(List<Assignment> assignments, JFrame frame, IDialogCallback<Assignment> callback) {
        super(frame, callback);
        this.assignments = assignments;
        this.frame = frame;
        this.createDialog();
    }

    /**
     * Creates the Dialog.
     */
    private void createDialog() {

        setLocationRelativeTo(frame);
        setSize(475, 200);
        setTitle("Select Assignment");

        JPanel contentPanel = new JPanel();

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        createTable(contentPanel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> {
            getDialogResult().setUserAction(UserAction.OK);
            doCallback();
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> {
            getDialogResult().setUserAction(UserAction.CANCEL);
            doCallback();
        });
        buttonPane.add(cancelButton);

    }
    /**
     * Creates the table and fits the header column to the content.
     * 
     * @param contentPanel
     */
    private void createTable(JPanel contentPanel) {
        Object[][] data  = this.convertListToArray();
        Object[] columnnames = {"Name", "State"};
        table = new JTable(data, columnnames) {

            /**
             * 
             */
            private static final long serialVersionUID = -5081497479703374256L;
            
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, 
                        tableColumn.getPreferredWidth()));
                return component;
            }
            
        };
        table.getSelectionModel().addListSelectionListener(event -> {
            getDialogResult().setResult(assignments.get(table.getSelectedRow()));
        });
        contentPanel.add(new JScrollPane(table));
    }
    /**
     * Converts the assignment list to an rayy for {@link JTable}.
     * 
     * @return Object[][]
     */
    private Object[][] convertListToArray() {
        Object[][] result = new Object[this.assignments.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = assignments.get(i).getName();
            result[i][1] = assignments.get(i).getState();
        }
        return result;
        
    }


}
