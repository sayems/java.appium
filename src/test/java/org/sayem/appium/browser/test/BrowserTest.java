package org.sayem.appium.browser.test;

import org.sayem.appium.browser.LocalBrowserBuilder;
import org.sayem.appium.browser.web.WebBrowser;
import org.sayem.appium.pages.HomePage;
import org.testng.annotations.Test;

/**
 * Created by sayem on 4/25/15.
 */

public class BrowserTest {

    @Test
    public void openFirefox() throws Exception {
        WebBrowser browser = LocalBrowserBuilder
                .getFirefoxBuilder("http://enterprise-demo.user.magentotrial.com/")
                .build();

        browser.openPageByURL(browser.getBaseTestUrl());
        browser.loadTopLevelPage(HomePage.class)
                .register();

        browser.quit();
    }
}