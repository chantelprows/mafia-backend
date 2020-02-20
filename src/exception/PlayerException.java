package exception;

public class PlayerException extends Exception {
    public String message;

    public PlayerException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
