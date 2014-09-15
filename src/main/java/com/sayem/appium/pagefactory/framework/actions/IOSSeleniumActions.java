package com.sayem.appium.pagefactory.framework.actions;

import com.sayem.appium.pagefactory.framework.browser.mobile.IOSMobileBrowser;

/**
 *
 * Selenium Actions for Android Applications
 *
 * Currently, this is the same as BaseSeleniumActions, as we don't have any need to implement anything differently
 * for IOS.
 *
 */

public class IOSSeleniumActions extends BaseSeleniumActions<IOSMobileBrowser> {

    public IOSSeleniumActions(IOSMobileBrowser browser) {
        super(browser);
    }
}
