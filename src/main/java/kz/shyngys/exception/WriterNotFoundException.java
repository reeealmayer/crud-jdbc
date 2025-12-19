package kz.shyngys.exception;

public class WriterNotFoundException extends RuntimeException {
    public WriterNotFoundException(String message) {
        super(message);
    }
}
