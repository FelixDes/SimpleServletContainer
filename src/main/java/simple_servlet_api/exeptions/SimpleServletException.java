package simple_servlet_api.exeptions;

public class SimpleServletException extends Exception {
    private Throwable rootCause;

    public SimpleServletException() {
    }

    public SimpleServletException(String message) {
        super(message);
    }

    public SimpleServletException(String message, Throwable rootCause) {
        super(message, rootCause);
        this.rootCause = rootCause;
    }

    public SimpleServletException(Throwable rootCause) {
        super(rootCause);
        this.rootCause = rootCause;
    }

    public Throwable getRootCause() {
        return this.rootCause;
    }
}
