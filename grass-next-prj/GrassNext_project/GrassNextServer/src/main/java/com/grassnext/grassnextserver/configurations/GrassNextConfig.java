package com.grassnext.grassnextserver.configurations;

import com.grassnext.grassnextserver.jni.GrassNextJni;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A configuration class for setting up GrassNext specific properties and behaviors.
 *
 */
@Configuration
@Data
public class GrassNextConfig implements WebMvcConfigurer, InitializingBean {
    /**
     * Represents the file system path to the topological data used by the GrassNext application.
     *
     */
    @Value("${grass-next.topo.path}")
    private String topoDataPath;
    /**
     * Duration in milliseconds for monitoring file system changes by the file watcher.
     *
     */
    @Value("${grass-nedxt.file-watcher.duration}")
    private Long fileWatcherDuration;
    /**
     * Represents a configurable time duration during which the file watcher remains in a "quiet" state
     *
     */
    @Value("${grass-nedxt.file-watcher.quiet-time}")
    private Long fileWatcherQuietTime;

    /**
     * Represents the file system directory path where the GrassNext library files are stored.
     *
     */
    @Value("${gn.lib_folder}")
    String gnLibFolder;
    /**
     * Represents the name of the shared library (.dll/.so) file required for GrassNext's native operations.
     *
     */
    @Value("${gn.lib_file}")
    String gnLibFile;
    /**
     * This variable holds the file name of the OpenCV library file as specified in the application properties.
     * It is used to dynamically load the OpenCV shared library during the
     * initialization of the GrassNextJni runtime.
     *
     */
    @Value("${gn.opencv_file}")
    String openCvLibFile;

    /**
     * This method is invoked after all the properties of the containing class have been set.
     * It is responsible for loading the native libraries required by the application using the
     * gnLoadLibrary method.
     *
     * @throws Exception if an error occurs while loading the native libraries.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        GrassNextJni.gnLoadLibrary(this);
    }
    /**
     * Configures CORS (Cross-Origin Resource Sharing) for incoming HTTP requests.
     * This method allows all HTTP methods for all endpoint patterns.
     *
     * @param registry the CorsRegistry instance used to define CORS configurations
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*");
    }
}
