package dev.upcraft.charactersinmotion;

import nu.pattern.OpenCV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CharactersInMotion implements AutoCloseable {

    private static final Logger logger = LogManager.getLogger("CharactersInMotion");

    public static Logger getLogger() {
        return logger;
    }

    public CharactersInMotion() {
        // try to load OpenCV from global install, fall back to the contained native library if no global install is found
        try {
            OpenCV.loadShared();
            logger.debug("Loaded global OpenCV install.");
        } catch (ExceptionInInitializerError e) {
            @Nullable Throwable cause = e.getCause();
            if (cause != null && "Failed to get field handle to set library path".equals(cause.getMessage())) {
                logger.trace("Unable to find OpenCV install, loading locally...");
                OpenCV.loadLocally();
                logger.debug("Loaded OpenCV from library.");
            } else throw e;
        }
    }

    public void start() {
        // TODO run main application loop
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        logger.info("mat = \n{}", mat.dump());
        // throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public void close() throws Exception {

    }
}
