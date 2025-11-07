package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import net.ssehub.teaching.exercise_submitter.lib.data.Course;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.StumgmtHandler;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.io.IOException;
import java.io.Serial;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SelectCourseDialog extends Dialog<Course> {
    @Serial
    private static final long serialVersionUID = 8427048704579781152L;
    public static final String ALREADY_ENROLLED_MESSAGE = "Already enrolled!";

    private final List<Course> courses;
    private final List<Course> userCourses;
    private final JFrame frame;
    private JTable table;
    private JButton okButton;

    /**
     * Create a new instance of {@link SelectCourseDialog}
     *
     * @param courses List of all courses in the student management system.
     * @param frame Frame for the positional basis of the dialog.
     * @param callback Callback to be called after the dialog is finished.
     */
    public SelectCourseDialog(List<Course> courses, List<Course> userCourses,
                              JFrame frame, IDialogCallback<Course> callback) {
        super(frame, callback);
        this.courses = courses;
        this.userCourses = userCourses;
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

        // table
        try {
            createTable();
            configureListSelectionListener();
            configureHyperlinkHandling();
        } catch (IOException e) {
            ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
        }


        // layout
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttonPane = createButtonPanel();
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }

    /**
     * Creates the table and fits the header column to the content.
     */
    private void createTable() throws IOException {
        Object[][] data  = this.convertListToArray();
        Object[] columnNames = {"Name", "ID", "Enrollment"};
        table = new JTable(data, columnNames) {

            @Serial
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
            public boolean editCellAt(int row, int column, java.util.EventObject event) {
                return false;
            }

        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
     * Create the button panel for the dialog, including refresh, ok and cancel buttons and their functionality.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            getDialogResult().setUserAction(DialogResult.UserAction.OK);
            getDialogResult().setResult(null);
            doCallback();
        });
        buttonPane.add(refreshButton);

        okButton = new JButton("OK");
        okButton.addActionListener(event -> {
            getDialogResult().setUserAction(DialogResult.UserAction.OK);
            doCallback();
        });
        okButton.setEnabled(false);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> {
            getDialogResult().setUserAction(DialogResult.UserAction.CANCEL);
            doCallback();
        });
        buttonPane.add(cancelButton);
        return buttonPane;
    }

    /**
     * Configures the ListSelectionListener such that the ok button is only clickable if a course is selected in which
     * the current user is enrolled. Also sets the appropriate dialog result.
     */
    private void configureListSelectionListener() {
        table.getSelectionModel().addListSelectionListener(event -> {
            Course course = courses.get(table.getSelectedRow());
            boolean isEnrolled = userCourses.contains(course);
            if (isEnrolled) {
                getDialogResult().setResult(course);
                okButton.setEnabled(true);
            } else {
                okButton.setEnabled(false);
            }
        });
    }

    /**
     * Configure the handling of mouse clicks on hyperlinks in the dialog table. Assures that openLink is only called
     * when an actual hyperlink is clicked.
     */
    private void configureHyperlinkHandling() {
        table.getColumnModel().getColumn(2).setCellRenderer(new HyperlinkRenderer());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                String value =  table.getValueAt(row, column).toString();
                boolean isLink = column == 2 && !Objects.equals(value, ALREADY_ENROLLED_MESSAGE);
                if (isLink) {
                    openLink(value);
                }
            }
        });
    }

    /**
     * Converts the course list to an array for the {@link JTable}.
     *
     * @return Object[][]
     */
    private Object[][] convertListToArray() throws IOException {
        Properties prop = new Properties();
        prop.load(StumgmtHandler.class.getResourceAsStream("config.properties"));

        String baseURL = prop.getProperty("coursesUrl");
        Object[][] result = new Object[this.courses.size()][3];
        for (int i = 0; i < result.length; i++) {
            Course course = this.courses.get(i);
            boolean enrolled = this.userCourses.contains(course);
            result[i][0] = course.getName();
            result[i][1] = course.getId();
            if (enrolled) {
                result[i][2] = ALREADY_ENROLLED_MESSAGE;
            } else {
                String link = baseURL + "/" + course.getId();
                result[i][2] = link;
            }
        }
        return result;
    }

    /**
     * Open the given url in the default browser.
     *
     * @param url String of the url to open.
     */
    private void openLink(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new java.net.URI(url));
            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                runtime.exec("xdg-open " + url);
            } else {
                System.err.println("Unsupported operating system: " + os);
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Renderer for hyperlinks in the dialog table. Used for enrollment links.
     */
    static class HyperlinkRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value == ALREADY_ENROLLED_MESSAGE) {
                label.setForeground(Color.BLACK);
                label.setText("<html>" + value + "</html>");
                return label;
            }

            label.setForeground(Color.BLUE);
            label.setText("<html><u>" + value + "</u></html>");
            return label;
        }
    }
}

