package com.springcloud.common.exception;

/**
 * The type Service exception.
 */
public class ServiceException extends RuntimeException {

    private String message;

    private String code;

    private Throwable cause;

    private Object data;

    /**
     * Instantiates a new Service exception.
     * @param message the message
     */
    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Instantiates a new Service exception.
     * @param message the message
     * @param cause   the cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * Instantiates a new Service exception.
     * @param cause   the cause
     * @param message the message
     */
    public ServiceException(Throwable cause, String message) {
        super(cause);
        this.message = message;
        this.cause = cause;
    }

    /**
     * Instantiates a new Service exception.
     * @param message the message
     * @param cause   the cause
     * @param data    the data
     */
    public ServiceException(String message, Throwable cause, Object data) {
        super(message, cause);
        this.message = message;
        this.data = data;
        this.cause = cause;
    }

    /**
     * Instantiates a new Service exception.
     * @param message the message
     * @param data    the data
     */
    public ServiceException(String message, Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new Service exception.
     * @param code    the code
     * @param message the message
     */
    public ServiceException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

}
