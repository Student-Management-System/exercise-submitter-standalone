package net.ssehub.teaching.exercise_submitter.standalone.stumgmt;

import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.lib.data.Course;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.IDialogCallback;
import net.ssehub.teaching.exercise_submitter.standalone.dialog.SelectCourseDialog;
import net.ssehub.teaching.exercise_submitter.standalone.exception.ExceptionDialog;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class lists the available courses in a dialog.
 */
public class ListCourses {
    private final JFrame frame;
    private final IDialogCallback<Course> callback;

    public ListCourses(JFrame frame, IDialogCallback<Course> callback) {
        this.frame = frame;
        this.callback = callback;
    }

    /**
     * Starts the asynchronous download and display of the courses.
     */
    public void startAsync() {
        IRunnableJob<Set<Course>, Exception> func = result -> {
            try {
                result.setOutput(StandaloneSubmitter.getHandler().getManager().getAllCourses());
            } catch (ApiException e) {
                result.setException(e);
            }
        };

        Job<Set<Course>, Exception> job = new Job<>(this::onListCourseFinished, func);
        job.start();
    }

    /**
     * Called when the ListCourseJob is finished to open the {@link SelectCourseDialog}.
     *
     * @param job The finished ListCourseJob.
     */
    private void onListCourseFinished(Job<Set<Course>, Exception> job) {
        if (!job.getJobResult().hasSuceeded()) {
            String message = job.getJobResult().getException().get().getLocalizedMessage();
            ExceptionDialog.createExceptionDialog(message, null);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Set<Course> courseSet = job.getJobResult().getOutput().get();
            List<Course> courseList = new ArrayList<>(courseSet);
            SelectCourseDialog dialog = new SelectCourseDialog(courseList, frame, callback);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
    }
}
