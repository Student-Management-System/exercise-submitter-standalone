package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import java.io.Serial;
import java.nio.file.Path;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Displays all files that were submitted to the server.
 *
 * @author marlo
 *
 */
public class SubmissionFilesDialog extends Dialog<Path> {

    @Serial
    private static final long serialVersionUID = -9176664379907656468L;
    private final List<Path> files;
    private final JFrame frame;

    /**
     * Creates a new instance of {@link SubmissionFilesDialog}.
     *
     * @param files     file paths to display on the dialog list.
     * @param frame     can be null. Used to position the dialog.
     */
    public SubmissionFilesDialog(List<Path> files, JFrame frame) {
        super(frame, null);
        this.files = files;
        this.frame = frame;
        this.createDialog();
    }

    /**
     * Creates the dialog with predetermined settings.
     *
     */
    private void createDialog() {
        setLocationRelativeTo(frame);
        setTitle("Submitted Files");

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = createList();
        listPanel.add(scrollPane,  BorderLayout.CENTER);
        getContentPane().add(listPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> this.dispose());
        buttonPanel.add(okButton);
        pack();
    }

    /**
     * Generate a JScrollPane containing a list of the dialogs files.
     *
     * @return JScrollPane containing JList of file paths.
     */
    private JScrollPane createList() {
        String[] pathStrings = files.stream()
                .map(Path::toString)
                .toArray(String[]::new);
        JList<String> list = new JList<>(pathStrings);
        return new JScrollPane(list);
    }
}
