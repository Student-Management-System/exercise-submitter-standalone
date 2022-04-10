package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.ssehub.teaching.exercise_submitter.standalone.listener.ResultListener;
import net.ssehub.teaching.exercise_submitter.standalone.submission.UploadSubmission;

/**
 * This class displayes the ResultPane on the {@link MainFrame}.
 * 
 * @author lukas
 *
 */
public class ResultPanel extends JPanel {

    private static final long serialVersionUID = 1082164071465357984L;
    private JTable table;
    private JLabel lblSubmissionStatus;
    
    private ResultListener listener;

    /**
     * Instantiates a new result panel.
     * @param listener
     */
    public ResultPanel(ResultListener listener) {
        //TODO: better design
        JPanel northpanel = new JPanel(new FlowLayout());
        
        JLabel label = new JLabel("");
        label.putClientProperty("FlatLaf.styleClass", "h3");
        lblSubmissionStatus = new JLabel("");
        lblSubmissionStatus.putClientProperty("FlatLaf.styleClass", "h3");
        
        setLayout(new BorderLayout());
        northpanel.add(label);
        northpanel.add(lblSubmissionStatus);
        add(northpanel, BorderLayout.NORTH);
        
        table = new JTable() {

            private static final long serialVersionUID = 1L;
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
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        listener.setModel(createTableColumns());
        listener.setResultLabel(lblSubmissionStatus);
        
        UploadSubmission.setResultListener(listener);
     
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(new JLabel(""));
        
        add(southPanel, BorderLayout.SOUTH);
        
    }
    /**
     * Creates the columns for the table.
     * 
     * @return DefaultTableModel.
     */
    private DefaultTableModel createTableColumns() {
        DefaultTableModel model = new DefaultTableModel();
       
        table.setModel(model);
        model.addColumn("Type");
        model.addColumn("Message");
        model.addColumn("File");
        return model;
    }
    /**
     * Gets the result listener.
     * 
     * @return ResultListenr
     */
    public ResultListener listener()  {
        return this.listener;
    }

}
