package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
        setSize(475, 200);
        setTitle("Versions Table");
        
        getContentPane().setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        
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
        JTable table = new JTable(data, columnnames) {
            
            private static final long serialVersionUID = 6624780621547713792L;
            
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
            getDialogResult().setResult(versions.get(table.getSelectedRow()));
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    /**
     * Converts the List to an Object[][] Array.
     * 
     * @return Object[][]
     */
    private Object[][] convertListToArray() {
        Object[][] result = new Object[this.versions.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = versions.get(i).getTimestamp().atZone(
                    ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            result[i][1] = versions.get(i).getAuthor();
        }
        return result;
        
    }
   

}
