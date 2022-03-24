package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.ssehub.teaching.exercise_submitter.lib.data.Assignment;
import net.ssehub.teaching.exercise_submitter.lib.replay.ReplayException;
import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer;
import net.ssehub.teaching.exercise_submitter.lib.replay.Replayer.Version;
import net.ssehub.teaching.exercise_submitter.lib.student_management_system.ApiException;
import net.ssehub.teaching.exercise_submitter.standalone.StandaloneSubmitter;
import net.ssehub.teaching.exercise_submitter.standalone.exception.JobResultException;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.IRunnableJob;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.Job;
import net.ssehub.teaching.exercise_submitter.standalone.jobs.JobResult;

/**
 * This class handles the comparing of the local submission and the online.
 * 
 * @author lukas
 *
 */
public class CompareSubmission {

    private JFrame frame;
    private Assignment assignment;
    private Path directory;

    /**
     * Creates an instance of {@link CompareSubmission}.
     * 
     * @param frame
     * @param assignment
     * @param directory
     */
    public CompareSubmission(JFrame frame, Assignment assignment, Path directory) {
        this.frame = frame;
        this.assignment = assignment;
        this.directory = directory;
    }

    /**
     * Start the comparing async.
     */
    public void startAsync() {

        getLatestVersion();

    }
    /**     * 
     * Gets the latest version from the stumgmt.
     */
    private void getLatestVersion() {
        IRunnableJob<List<Version>, Exception> func = new IRunnableJob<List<Version>, Exception>() {

            @Override
            public void run(JobResult<List<Version>, Exception> result) {
                try {
                    Replayer replayer = StandaloneSubmitter.getHandler().getManager().getReplayer(assignment);
                    List<Version> version = replayer.getVersions();
                    result.setOutput(version);
                } catch (IllegalArgumentException | ApiException | ReplayException e) {
                    result.setException(e);
                }

            }
        };

        Job<List<Version>, Exception> job = new Job<List<Version>, Exception>(this::onFinishedLoadingLatestVersion,
                func);
        job.start();

    }
    /**
     * Compare the local submission with the online submission.
     * 
     * @param version
     */
    private void compareSubmissions(Version version) {
        IRunnableJob<Boolean, Exception> func = new IRunnableJob<Boolean, Exception>() {

            @Override
            public void run(JobResult<Boolean, Exception> result) {
                try {
                    Replayer replayer = StandaloneSubmitter.getHandler().getManager().getReplayer(assignment);
                    result.setOutput(replayer.isSameContent(directory.toFile(), version));
                } catch (IllegalArgumentException | ApiException | IOException | ReplayException e) {
                    result.setException(e);
                }

            }
        };

        Job<Boolean, Exception> job = new Job<Boolean, Exception>(this::onFinishedComparingSubmissions, func);
        job.start();
    }
    /**
     * Called when fetching the latest version is done.
     * 
     * @param job
     */
    private void onFinishedLoadingLatestVersion(Job<List<Version>, Exception> job) {

        if (job.getJobResult().hasSuceeded()) {
            if (!job.getJobResult().getOutput().get().isEmpty()) {
                compareSubmissions(job.getJobResult().getOutput().get().get(0));
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "No uploaded version can be found", "Warning!", JOptionPane.WARNING_MESSAGE));
            }
        } else {
            JobResultException.handleSubmissionException(job.getJobResult(),
                    "Loading the version list for the selected Assignment failed", null);
        }

    }
    /**
     * Called when comparing the versions is done.
     * @param job
     */
    private void onFinishedComparingSubmissions(Job<Boolean, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {
            if (job.getJobResult().getOutput().get()) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Your local dir and the current version on the server are the SAME", "Success!",
                        JOptionPane.INFORMATION_MESSAGE));
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Your local dir and the current version on the server are NOT the same", "Warning!",
                        JOptionPane.WARNING_MESSAGE));
            }
        } else {
            JobResultException.handleSubmissionException(job.getJobResult(), "Comparing failed", null);
        }
    }

}
