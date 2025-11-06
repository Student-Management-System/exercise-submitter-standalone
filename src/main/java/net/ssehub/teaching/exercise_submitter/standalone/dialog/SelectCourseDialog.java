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

    /**
     * Create a new instance of {@link SelectCourseDialog}
     *
     * @param courses
     * @param frame
     * @param callback
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

        try {
            createTable(contentPanel);
        } catch (IOException e) {
            ExceptionDialog.createExceptionDialog(e.getLocalizedMessage(), null);
        }

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
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates the table and fits the header column to the content.
     *
     * @param contentPanel
     */
    private void createTable(JPanel contentPanel) throws IOException {
        Object[][] data  = this.convertListToArray();
        Object[] columnnames = {"Name", "ID", "Enrollment"};
        table = new JTable(data, columnnames) {

            /**
             *
             */
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
            public boolean editCellAt(int row, int column, java.util.EventObject evento) {
                return false;
            }

        };
        table.getSelectionModel().addListSelectionListener(event -> {
            getDialogResult().setResult(courses.get(table.getSelectedRow()));
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

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

