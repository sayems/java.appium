package com.sayem.appium.pagefactory.framework.exception;

public class InvalidPageUrlException extends RuntimeException {
    public InvalidPageUrlException(String msg) {
        super(msg);
    }

    public InvalidPageUrlException(String msg, Exception cause) {
        super(msg, cause);
    }
}
