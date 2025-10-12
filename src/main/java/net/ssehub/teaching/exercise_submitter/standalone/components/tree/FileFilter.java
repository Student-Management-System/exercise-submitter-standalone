package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.StumgmtHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * This filter can filter specific file extensions.
 * 
 * @author lukas
 *
 */
public class FileFilter {
    private Predicate<Path> filter;
    
    /**
     * Creates an instance of {@link FileFilter}.
     */
    public FileFilter() {
        this.filter = createFilter();
    }

    /**
     * Returns the file filter predicate.
     * @return The file filter predicate.
     */
    public Predicate<Path> getFilter() {
        return filter;
    }

    /**
     * Creates a path predicate which can be used to filter out files before submission and when displaying them
     * on the file tree panel.
     * Uses the config properties "excludedExtensions" to filter out files which end with any of those file extensions,
     * and "excludedDirs" to filter out files whose path starts with any of the dirs.
     * Items in the property lists are separated by ','.
     * If any of the properties are missing will return an empty filter.
     *
     * @return A Predicate<Path> which filters the excluded file extensions and directories. Returns null if the config
     * could not be loaded.
     */
    private static Predicate<Path> createFilter() {
        Properties prop = new Properties();
        try {
            prop.load(StumgmtHandler.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Could not load config file");
            return null;
        }
        String extensionsString = prop.getProperty("excludedExtensions");
        Predicate<Path> extensionExclusion = p -> false;
        if (extensionsString != null) {
            for (String ext : extensionsString.split(",")) {
                final String extension = ext.trim();
                extensionExclusion = extensionExclusion.or(
                        p -> p.getFileName() != null && p.getFileName().toString().endsWith(extension)
                );
            }
        }

        String dirString = prop.getProperty("excludedDirs");
        Predicate<Path> dirExclusion = p -> false;
        if (dirString != null) {
            for (String dir : dirString.split(",")) {
                final Path excludedDirPath = Path.of(dir.trim());

                dirExclusion = dirExclusion.or(
                        p -> p.startsWith(excludedDirPath)
                );
            }
        }
        Predicate<Path> shouldExclude = extensionExclusion.or(dirExclusion);
        return shouldExclude.negate().and(Objects::nonNull);
    }

    /**
     * Tests a given file against the file filter. Used for the display on the file tree.
     * @param file The given file to be checked.
     * @return true if the file should not be filtered and is allowed, false otherwise.
     */
    public boolean isAllowed(File file) {
        String filePath = file.getPath();
        return filter.test(Path.of(filePath));
    }
}
