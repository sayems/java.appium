package com.sayem.appium.pagefactory.framework.exception;

public class WebDriverException extends Exception {
    public WebDriverException(String msg) {
        super(msg);
    }

    public WebDriverException(String msg, Exception e) {
        super(msg, e);
    }
}
