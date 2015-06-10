package org.sayem.appium.actions;


import org.sayem.appium.browser.mobile.AndroidMobileBrowser;

/**
 * Selenium Actions for Android Applications
 * <p>
 * Currently, this is the same as BaseSeleniumActions, as we don't have any need to implement anything differently
 * for Android.
 */
public class AndroidSeleniumActions extends BaseSeleniumActions<AndroidMobileBrowser> {

    public AndroidSeleniumActions(AndroidMobileBrowser browser) {
        super(browser);
    }
}
