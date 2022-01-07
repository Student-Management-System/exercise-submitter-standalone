package net.ssehub.teaching.exercise_submitter.standalone.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.ssehub.teaching.exercise_submitter.standalone.listener.SubmissionListener;

public class SelectionPanel extends JPanel {
    
    private static final long serialVersionUID = 2873287812314470834L;

    public SelectionPanel(SubmissionListener listener) {
        
        JPanel top = new JPanel(new FlowLayout());
        
        JTextField pathField = new JTextField(30);
        pathField.addActionListener((e) -> listener.setSelectedPath(pathField.getText()));
        top.add(pathField);
        
        JButton button = new JButton("Choose");
        button.addActionListener((e) -> listener.setSelectedPath(pathField.getText()));
        top.add(button);
        
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JTextArea(), BorderLayout.CENTER);
        
    }
    
}
