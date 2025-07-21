package com.grassnext.grassnextserver.filewatcher;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.grassnext.grassnextserver.topodata.TopoDataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * The FileWatcherListener class is responsible for handling file change events in a monitored directory.
 * It listens for file addition events and triggers the parsing and import of the corresponding file data.
 *
 *
 */
@Service
public class FileWatcherListener implements FileChangeListener {
    /**
     * Logger instance for the {@code FileWatcherListener} class.
     * Used to log information, warnings, and errors related to file change events and their processing.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcherListener.class);
    /**
     * A private, final instance of {@link ListeningExecutorService} used for managing
     * asynchronous tasks with support for listening to task completion or failure.
     *
     */
    private final ListeningExecutorService leService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    /**
     * An instance of TopoDataParser used to handle the parsing and importing of
     * topo data from specified files.
     *
     */
    TopoDataParser topoDataParser;

    /**
     * Constructs a new instance of the FileWatcherListener class.
     *
     * @param topoDataParser the TopoDataParser instance responsible for parsing and importing topology data from files
     */
    @Autowired
    FileWatcherListener(TopoDataParser topoDataParser) {
        this.topoDataParser = topoDataParser;
    }

    /**
     * Handles file change events for a specified set of watched files or directories.
     * This implementation processes files added to the watch directory by delegating
     * their import and parsing to the {@code TopoDataParser} service asynchronously.
     *
     * @param changeSet the set of {@code ChangedFiles} objects representing the files
     *                  that have changed.
     *                  Each {@code ChangedFiles} contains details
     *                  about the type and location of the change.
     *                  Files of type
     *                  {@code ChangedFile.Type.ADD} are specifically processed by this method.
     */
    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles cfiles : changeSet) {
            for (ChangedFile cfile : cfiles.getFiles()) {
                if (cfile.getType().equals(ChangedFile.Type.ADD)) {
                    String filePath = cfile.getFile().getAbsolutePath();

                    ListenableFuture<Boolean> guavaFuture = leService.submit( () ->
                            topoDataParser.importTopoData(filePath)
                    );

                    try {
                        Boolean isOk = guavaFuture.get();
                        if( !isOk ) {
                            LOGGER.info("[LOGGER] Error while exporting data from file: " + filePath + "! ");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.info("[LOGGER] Guava get() error while parsing file: " + filePath + "! " + e.getMessage());
                    }

                    LOGGER.info("[LOGGER] File " + filePath + " processed successfully!");
                }
            }
        }
    }
}