package com.sayem.appium.pagefactory.framework.tests;

import com.sayem.appium.pagefactory.framework.browser.MobileBrowserBuilder;
import com.sayem.appium.pagefactory.framework.browser.mobile.MobileBrowser;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.models.Android;
import com.sayem.appium.pagefactory.framework.models.User;
import com.sayem.appium.pagefactory.framework.pages.LoginPage;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.Test;

public class LoginTest {

    @Test
    public void loginTest() throws Exception {

        TimeoutsConfig timeouts = TimeoutsConfig.builder()
                .pageLoadTimoutSeconds(10)
                .implicitWaitTimeoutMillis(20000)
                .build();

        MobileBrowser mobile = MobileBrowserBuilder.getAndroidBuilder("http://127.0.0.1:4723/wd/hub")

                .withTimeoutsConfig(timeouts)
                .withPlatformVersion("4.4")
                .withDeviceName("Moto X")
                .withApp("/Users/ssayem/IdeaProjects/Appium/src/test/resources/app-debug-unaligned.apk")
                .withAppPackage("com.company.companyandroid")
                .withAppActivity("com.company.android.activities.HomeActivity")
                .build();

        mobile.getActions().loadTopLevelPage(LoginPage.class).login();
        mobile.quit();
    }
}
