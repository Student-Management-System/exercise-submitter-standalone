package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;
import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

/**
 * Displays the submissionfiled in the mainframe.
 * 
 * @author lukas
 * @author adam
 *
 */
public class SubmissionPanel extends JPanel {

    private static final long serialVersionUID = -7484887017026208115L;
    private JComboBox<String> homework;
    
    private List<Assignment> assignments = new ArrayList<Assignment>();

    /**
     * Instantiates new SubmissionPanel.
     * 
     * @param listener
     */
    public SubmissionPanel(SubmissionListener listener) {
        this.homework = new JComboBox<String>();
        this.homework.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                if (event.getActionCommand().equals("comboBoxChanged")) {
                    if (!assignments.isEmpty()) {
                        listener.setAssignment(assignments.get(homework.getSelectedIndex()));                       
                    }
                }
            }
        });

        this.loadAssignmentsIntoComboBox();

        this.setLayout(new FlowLayout());
        this.add(this.homework);

        JButton submit = new JButton("Submit");
        this.add(submit);
        submit.setEnabled(false);
        submit.setToolTipText("You need to select an assignment and a directory");
        listener.addPathAndAssignmentSelectionListener(l -> {
            submit.setEnabled(l);
            submit.setToolTipText(l ? "Submit" : "You need to select an assignment and a directory");    
        });
        submit.addActionListener(e -> {
            listener.submit();
        });
    }
    /**
     * Load the assignments in the combobox.
     */
    private void loadAssignmentsIntoComboBox() {

        IRunnableJob<List<Assignment>, ApiException> func = new IRunnableJob<List<Assignment>, ApiException>() {

            @Override
            public void run(JobResult<List<Assignment>, ApiException> result) {
                try {
                    result.setOutput(StandaloneSubmitter.getHandler().getManager().getAllSubmittableAssignments());
                } catch (ApiException e) {
                    result.setException(e);
                }

            }
        };
        
        Job<List<Assignment>, ApiException> job = 
                new Job<List<Assignment>, ApiException>(this::onFinishedLoadAssignments, func);
        job.start();
        

    }
    /**
     * Called when the load assignment job is finished.
     * 
     * @param job
     */
    private void onFinishedLoadAssignments(Job<List<Assignment>, ApiException> job) {
        homework.removeAllItems();
        if (job.getJobResult().hasSuceeded()) {
            this.assignments = job.getJobResult().getOutput().get();
            SwingUtilities.invokeLater(new Runnable()  {
                @Override
                public void run() {               
                    for (Assignment assignment : job.getJobResult().getOutput().get()) {
                        homework.addItem(assignment.getName());
                    }
                    if (homework.getItemCount() > 0) {
                        homework.setSelectedIndex(0);
                    }             
                }
                  
            });
        } else {
            ExceptionDialog.createExceptionDialog("Submission uploading not succeeded", this);
        }
        
    }

}
