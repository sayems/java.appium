package com.sayem.appium.pagefactory.framework.actions;

import com.sayem.appium.pagefactory.framework.browser.web.FirefoxBrowser;

/**
 * Selenium Actions for Firefox Browser.
 * <p>
 * Currently, this is the same as BaseSeleniumActions, as we don't have any need to implement anything differently
 * for Firefox.
 */
public class FirefoxSeleniumActions extends BaseSeleniumActions<FirefoxBrowser> {
    public FirefoxSeleniumActions(FirefoxBrowser browser) {
        super(browser);
    }
}
