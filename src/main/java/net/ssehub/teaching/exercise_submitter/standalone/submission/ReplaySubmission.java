package net.ssehub.teaching.exercise_submitter.standalone.submission;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFileChooser;
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
 * This class ahndles the download of the submissions.
 * 
 * @author lukas
 *
 */
public class ReplaySubmission {

    private Assignment assignment;
    private Version version;
    private JFrame frame;
    
    /**
     * Creates an instance of {@link ReplaySubmission}.
     * 
     * @param assignment
     * @param version
     * @param frame
     */
    public ReplaySubmission(Assignment assignment, Version version, JFrame frame) {
        this.assignment = assignment;
        this.version = version;
    }
    /**
     * Starts  the download of the submission asnychronous.
     * 
     */
    public void startAsyncReplay() {

        IRunnableJob<Boolean, Exception> func = new IRunnableJob<Boolean, Exception>() {

            @Override
            public void run(JobResult<Boolean, Exception> result) {
                try {
                    Replayer replayer = StandaloneSubmitter.getHandler().getManager().getReplayer(assignment);
                    File tempFile = replayer.replay(version);
                    Optional<Path> selectedPath = Optional.empty();
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    AtomicInteger option = new AtomicInteger();
                    SwingUtilities.invokeAndWait(() -> option.set(fileChooser.showOpenDialog(frame)));
                    if (option.get() == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        selectedPath = Optional.ofNullable(file.toPath());
                        if (selectedPath.isPresent()) {
                            copyFiles(tempFile.toPath(),  new File(selectedPath.get().toFile(),
                                    assignment.getName() + "_" + version.getTimestamp().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))).toPath());
                            result.setOutput(true);
                        } else {
                            result.setOutput(false);
                        }
                    } else {
                        result.setOutput(false);
                    }
                } catch (IllegalArgumentException | ReplayException 
                        | ApiException | IOException | InvocationTargetException | InterruptedException e) {
                    result.setException(e);
                }
            }
        };

        Job<Boolean, Exception> job = new Job<Boolean, Exception>(this::onReplayFinished, func);
        job.start();

    }
    /**
     * Called when the replay is finished.
     * 
     * @param job
     */
    private void onReplayFinished(Job<Boolean, Exception> job) {
        if (job.getJobResult().hasSuceeded()) {
            boolean hasSelected = job.getJobResult().getOutput().get();
            SwingUtilities.invokeLater(() -> {
                if (hasSelected) {
                    JOptionPane.showMessageDialog(null, 
                            "Submission successfully downloaded", "Success!" , JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, 
                            "Download submission canceled: No directory selected!", 
                            "Warning!" , JOptionPane.WARNING_MESSAGE);
                }
            });
        } else {
            //TODO: exception bsp: already existing.
            JobResultException.handleSubmissionException(job.getJobResult(), "Replaying failed", frame);
        }

    }

    /**
     * Copies files from one path to the other.
     * 
     * @param replayDirectory
     * @param projectDirectory
     * @throws IOException
     */
    public static synchronized void copyFiles(Path replayDirectory, Path projectDirectory) throws IOException {
        try {
            Files.walk(replayDirectory).forEach(sourceFile -> {
                Path targetFile = projectDirectory.resolve(replayDirectory.relativize(sourceFile));

                try {
                    if (Files.isDirectory(sourceFile)) {
                        if (!Files.exists(targetFile)) {
                            Files.createDirectory(targetFile);
                        }
                    } else {
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

}
