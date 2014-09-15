package com.sayem.appium.pagefactory.framework.browser.web;

/**
 * Represents the type of Browser being created.
 * TODO: Add support for Safari by creating a SafariBrowser class and figure out the correct DesiredCapabilities for configuring Safari.
 */
public enum WebBrowserType {
    IE, CHROME, FIREFOX, MOBILE;

    public static WebBrowserType forName(String name) {
        for (WebBrowserType type: WebBrowserType.values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("WebBrowserType must be 'IE', 'CHROME', or 'FIREFOX'");
    }
}
