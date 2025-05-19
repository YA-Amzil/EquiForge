package be.pxl.researchproject.exception;

public class FoalNotFoundException extends RuntimeException {

    public FoalNotFoundException(String message) {
        super(message);
    }

    public FoalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
