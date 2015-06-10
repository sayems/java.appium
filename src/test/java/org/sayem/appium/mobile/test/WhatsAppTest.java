package org.sayem.appium.mobile.test;

import org.sayem.appium.browser.MobileBrowserBuilder;
import org.sayem.appium.browser.mobile.MobileBrowser;
import org.sayem.appium.config.TimeoutType;
import org.sayem.appium.config.TimeoutsConfig;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class WhatsAppTest {

    @Test
    public void whatsAppTest() throws Exception {

        TimeoutsConfig timeouts = TimeoutsConfig.builder()
                .pageLoadTimoutSeconds(10)
                .implicitWaitTimeoutMillis(2000)
                .build();

        MobileBrowser mobileBrowser = MobileBrowserBuilder.getAndroidBuilder("http://127.0.0.1:4723/wd/hub")

                .withTimeoutsConfig(timeouts)
                .withPlatformVersion("4.4")
                .withDeviceName("Moto X")
                .withApp("/Users/ssayem/Downloads/Mobile/src/test/resources/com.whatsapp.apk")
                .withAppPackage("com.whatsapp")
                .withAppActivity("com.whatsapp.Main")
                .build();

        mobileBrowser.getActions().click(
                By.id("com.whatsapp:id/eula_accept"), TimeoutType.DEFAULT);
        mobileBrowser.quit();
    }

}