package net.ssehub.teaching.exercise_submitter.standalone.components.tree;

import net.ssehub.teaching.exercise_submitter.standalone.stumgmt.StumgmtHandler;

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
    private Predicate<Path> fileFilter;
    private Predicate<Path> dirFilter;

    /**
     * Creates an instance of {@link FileFilter}.
     */
    public FileFilter() {
        createFilters();
    }

    /**
     * Returns the file filter predicate.
     *
     * @return
     */
    public Predicate<Path> getFilter() {
        return fileFilter;
    }

    /**
     * Creates path predicates which can be used to filter out files before submission and when displaying files or
     * folders in the file tree panel.
     * Uses the config properties "excludedExtensions" to filter out files which end with any of those file extensions,
     * and "excludedDirs" to filter out files whose path starts with any of the dirs.
     * Items in the property lists are separated by ','.
     */
    private void createFilters() {
        Properties prop = new Properties();
        try {
            prop.load(StumgmtHandler.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Could not load config file");
            return;
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
        fileFilter =  shouldExclude.negate().and(Objects::nonNull);
        dirFilter = dirExclusion.negate().and(Objects::nonNull);
    }

    /**
     * Tests a given file against the file filter. Used for the display on the file tree.
     *
     * @param filePath The path of the file to be checked.
     * @return true if the file should not be filtered and is allowed, false otherwise.
     */
    public boolean isAllowedFile(Path filePath) {
        return fileFilter.test(filePath);
    }

    /**
     * Tests a given dir against the dirFilter. Used for the display of dirs in the file tree.
     *
     * @param dirPath The path to the dir to be checked.
     * @return true if the dir should not be filtered and is allowed, false otherwise.
     */
    public boolean isAllowedDir(Path dirPath) {
        return dirFilter.test(dirPath);
    }
}
