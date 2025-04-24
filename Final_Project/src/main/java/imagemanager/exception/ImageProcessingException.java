package imagemanager.exception;

// This class represents a custom exception for image processing errors.
// It extends the Exception class and provides a constructor to set the error message and cause.
// The exception can be thrown when there are issues with image conversion or processing.
public class ImageProcessingException extends Exception {
    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
