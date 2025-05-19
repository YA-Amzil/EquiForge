package be.pxl.researchproject.exception;

public class HorseNotFoundException extends RuntimeException {
    public HorseNotFoundException(String message) {
        super(message);
    }
}
