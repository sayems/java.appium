package com.sayem.appium.pagefactory.framework.browser.test;

import com.sayem.appium.pagefactory.framework.browser.LocalBrowserBuilder;
import com.sayem.appium.pagefactory.framework.browser.web.WebBrowser;
import com.sayem.appium.pagefactory.framework.pages.HomePage;
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