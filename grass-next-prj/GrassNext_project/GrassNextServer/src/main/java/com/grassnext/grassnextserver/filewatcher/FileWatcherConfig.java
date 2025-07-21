package com.grassnext.grassnextserver.filewatcher;

import com.grassnext.grassnextserver.configurations.GrassNextConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;

import java.io.File;
import java.time.Duration;

/**
 * Configuration class for setting up the File System Watcher in the application.
 * The file watcher monitors a specific directory for changes and processes added files
 * using a listener implementation (FileWatcherListener).
 *
 */
@Configuration
public class FileWatcherConfig {
    /**
     * Logger instance for the {@code FileWatcherConfig} class.
     * Used for logging informational, debug, and error messages related to
     * the configuration and operation of the file watcher within the application.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcherConfig.class);
    /**
     * An instance of FileWatcherListener responsible for handling file system change events
     * detected by the file watcher.
     *
     */
    FileWatcherListener fwl;
    /**
     * Instance of GrassNextConfig used to provide configuration properties specific
     * to the GrassNext application.
     *
     */
    GrassNextConfig gnc;

    /**
     * Constructs an instance of the {@code FileWatcherConfig} class, initializing its dependencies.
     *
     * @param fwl an instance of the {@code FileWatcherListener}, responsible for handling file change events.
     * @param gnc an instance of the {@code GrassNextConfig}, providing configuration values for the file watcher.
     */
    @Autowired
    FileWatcherConfig(FileWatcherListener fwl, GrassNextConfig gnc) {
        this.fwl = fwl;
        this.gnc = gnc;
    }

    /**
     * Creates and configures a {@link FileSystemWatcher} bean to monitor file system changes.
     * It uses a listener, implemented in {@link FileWatcherListener}, to handle file additions.
     *
     * @return the configured and started {@link FileSystemWatcher} instance
     *
     */
    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(
                true,
                Duration.ofMillis( gnc.getFileWatcherDuration() ),
                Duration.ofMillis( gnc.getFileWatcherQuietTime() )
        );

        fileSystemWatcher.addSourceDirectory( new File( new File( gnc.getTopoDataPath() ).getAbsolutePath() ) );
        fileSystemWatcher.addListener( fwl );
        fileSystemWatcher.start();
        LOGGER.info("[LOGGER] File watcher started!");

        return fileSystemWatcher;
    }
}
