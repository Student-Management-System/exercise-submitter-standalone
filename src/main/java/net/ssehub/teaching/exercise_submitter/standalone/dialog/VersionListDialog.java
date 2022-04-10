package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.ZoneId;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer.Version;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.DialogResult.UserAction;

/**
 * Displays the Versions of a specific assignment.
 * 
 * @author lukas
 *
 */
public class VersionListDialog extends Dialog<Version> {
    
    private static final long serialVersionUID = 7078834232408235881L;
    
    private List<Version> versions;
    private JFrame frame;
   
    
    /**
     * Creates an instance of {@link VersionListDialog}.
     * 
     * @param versions
     * @param frame
     * @param callback
     */
    public VersionListDialog(List<Version> versions
            , JFrame frame, IDialogCallback<Version> callback) {
        super(frame, callback);
        this.versions = versions;
        this.frame = frame;
        this.createDialog();
    }
    
    /**
     * Creates the Dialog.
     * 
     */
    private void createDialog() {
        
        setLocationRelativeTo(frame);
        setSize(400, 200);
        setTitle("Versions Table");
        
        getContentPane().setLayout(new BorderLayout());
        
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
     * Creates the table.
     * 
     * @param contentPanel
     */
    private void createTable(JPanel contentPanel) {
        Object[][] data  = this.convertListToArray();
        Object[] columnnames = {"Timestamp", "Author"};
        JTable table = new JTable(data, columnnames);
        table.getSelectionModel().addListSelectionListener(event -> {
            getDialogResult().setResult(versions.get(table.getSelectedRow()));
        });
        contentPanel.add(new JScrollPane(table));
    }
    
    /**
     * Converts the List to an Object[][] Array.
     * 
     * @return Object[][]
     */
    private Object[][] convertListToArray() {
        Object[][] result = new Object[this.versions.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = versions.get(i).getTimestamp().atZone(ZoneId.systemDefault()).toString();
            result[i][1] = versions.get(i).getAuthor();
        }
        return result;
        
    }
   

}
