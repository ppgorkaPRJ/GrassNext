package com.grassnext.grassnextserver.jni;

import com.grassnext.grassnextserver.common.Log;
import com.grassnext.grassnextserver.configurations.GrassNextConfig;
import org.nd4j.shade.guava.util.concurrent.ListenableFuture;
import org.nd4j.shade.guava.util.concurrent.ListeningExecutorService;
import org.nd4j.shade.guava.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * The GrassNextJni class provides a bridge between Java and the native C++ library needed
 * for executing core functionality of calculating air pollution dispersion using the Gaussian Plume formula.
 *
 */
public class GrassNextJni {
    /**
     * A static, thread-safe executor service for asynchronously executing tasks in the GrassNextJni class.
     * This service wraps a cached thread pool with Guava's ListeningExecutorService to provide additional
     * functionality, such as listening for task completion using ListenableFuture.
     *
     */
    private static final ListeningExecutorService leService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    /**
     * A logger instance used for logging messages, warnings, and errors
     * specific to the operations conducted within the GrassNextJni class.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrassNextJni.class);

    /**
     * A native method that executes an echo operation used for receiving futures from the native C++ library.
     *
     * @return a String result returned by the native C++ library.
     *
     */
    private static synchronized native String Echo();
    /**
     * Invokes the native Count method implemented in the underlying C++ library.
     *
     * @param jsonIn a JSON-formatted string containing the input data to be processed by the native library
     * @return a JSON-formatted string containing the result of the processing performed by the native library
     */
    private static synchronized native String Count(String jsonIn);

    /**
     * Dynamically loads the required native libraries for the GrassNext application.
     * The method attempts to load the main library file specified in the configuration
     * along with an optional OpenCV library file.
     *
     * @param gnConfig an instance of GrassNextConfig that contains configuration details
     *
     */
    public static void gnLoadLibrary(GrassNextConfig gnConfig) {
        String msg;
        File f = new File(gnConfig.getGnLibFolder());
        String mcPathLib = f.getAbsolutePath();
        String fullPathLib = mcPathLib + File.separator + gnConfig.getGnLibFile();

        String fullPathOpenCv = mcPathLib + File.separator + gnConfig.getOpenCvLibFile();
        try {
            if(!Objects.equals(gnConfig.getOpenCvLibFile(), "")) {
                System.load(fullPathOpenCv);
            }
            System.load(fullPathLib);
            Log.show("LOAD  SUCCESS dll/so: " + fullPathLib);
        } catch (Exception e) {
            msg = "  [GRASSNEXT-LOG] LOAD FAILURE dll/so: \n" + e.getMessage();
        }
    }

    /**
     * Executes the native Count method via a submitted task and retrieves the result asynchronously.
     *
     * @param jsonIn a JSON-formatted string containing the input data to be processed by the native library
     * @return a JSON-formatted string containing the result of the processing performed by the native library
     * @throws RuntimeException if an InterruptedException or ExecutionException occurs during future execution
     *
     */
    public static String gnExecuteCount(String jsonIn) {
        String jsonOut;

        try {
            ListenableFuture<String> guavaFuture = (ListenableFuture<String>) leService.submit(() -> Count(jsonIn));
            jsonOut = guavaFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return jsonOut;
    }

    /**
     * Executes the native Echo method via a submitted task and retrieves the result asynchronously.
     *
     * @return the String result obtained from the execution of the Echo native method.
     * @throws RuntimeException if an InterruptedException or ExecutionException occurs during future execution.
     *
     */
    public static String gnExecuteEcho() {
        String echoOut;

        try {
            ListenableFuture<String> guavaFuture = (ListenableFuture<String>) leService.submit(() -> Echo());
            echoOut = guavaFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return echoOut;
    }
}
