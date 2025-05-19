package be.pxl.researchproject.exception;

public class InvalidDewormingScheduleException extends RuntimeException {

    public InvalidDewormingScheduleException(String message) {
        super(message);
    }

    public InvalidDewormingScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
