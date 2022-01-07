package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

public class SubmissionPanel extends JPanel {

    private static final long serialVersionUID = -7484887017026208115L;

    public SubmissionPanel(SubmissionListener listener) {
        JComboBox<String> homework = new JComboBox<>(new String[] {"Homework01", "Homework02"});
        
        setLayout(new FlowLayout());
        add(homework);
        
        JButton submit = new JButton("Submit");
        add(submit);
        submit.setEnabled(false);
        listener.addPathSelectionListener(path -> {
            submit.setEnabled(path.isPresent());
        });
        submit.addActionListener((e) -> {
            listener.submit();
        });
    }
    
}
