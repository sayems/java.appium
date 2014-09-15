package com.sayem.appium.pagefactory.framework.exception;

public class SeleniumActionsException extends Exception {
    public SeleniumActionsException(String msg) {
        super(msg);
    }

    public SeleniumActionsException(String msg, Exception e) {
        super(msg, e);
    }
}