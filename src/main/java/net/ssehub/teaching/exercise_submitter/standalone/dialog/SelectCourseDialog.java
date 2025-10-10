package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import net.ssehub.teaching.exercise_submitter.lib.data.Course;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class SelectCourseDialog extends Dialog<Course> {
    private static final long serialVersionUID = 8427048704579781152L;

    private List<Course> courses;
    private JFrame frame;
    private JTable table;

    /**
     * Create a new instance of {@link SelectCourseDialog}
     *
     * @param courses
     * @param frame
     * @param callback
     */
    public SelectCourseDialog(List<Course> courses,  JFrame frame, IDialogCallback<Course> callback) {
        super(frame, callback);
        this.courses = courses;
        this.frame = frame;
        this.createDialog();
    }

    /**
     * Create the dialog.
     */
    private void createDialog() {
        setLocationRelativeTo(frame);
        setSize(475, 200);
        setTitle("Select Course");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        createTable(contentPanel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> {
            getDialogResult().setUserAction(DialogResult.UserAction.OK);
            doCallback();
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> {
            getDialogResult().setUserAction(DialogResult.UserAction.CANCEL);
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
        Object[] columnnames = {"Name", "ID"};
        table = new JTable(data, columnnames) {

            /**
             *
             */
            private static final long serialVersionUID = 7198620275645798572L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width,
                        tableColumn.getPreferredWidth()));
                return component;
            }
            @Override
            public boolean editCellAt(int row, int column, java.util.EventObject evento) {
                return false;
            }

        };
        table.getSelectionModel().addListSelectionListener(event -> {
            getDialogResult().setResult(courses.get(table.getSelectedRow()));
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Converts the course list to an array for the {@link JTable}.
     *
     * @return Object[][]
     */
    private Object[][] convertListToArray() {
        Object[][] result = new Object[this.courses.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = courses.get(i).getName();
            result[i][1] = courses.get(i).getId();
        }
        return result;
    }
}
