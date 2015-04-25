package com.sayem.appium.pagefactory.framework.browser.mobile;

import com.sayem.appium.pagefactory.framework.actions.IOSSeleniumActions;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.IWebDriverException;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


/**
 * Known bug of Apple from Xcode 5 and iOS 7.1 Simulator - swipe is not working on simulator.
 * As a workaround, using scrollTo in JavaScript.
 * As in real devices regular swipe works but not scrollTo, using the regular command as well
 */

public class IOSMobileBrowser extends MobileBrowser {

    public IOSMobileBrowser(String baseTestUrl,
                            String browserName,
                            String platform,
                            String platformName,
                            String platformVersion,
                            String deviceName,
                            String newCommandTimeout,
                            String automationName,
                            String version,
                            String autoLaunch,
                            String app,
                            TimeoutsConfig timeouts) throws IWebDriverException {
        super(baseTestUrl, timeouts, browserName, platform, platformName, platformVersion, deviceName,
                newCommandTimeout, automationName, version, autoLaunch, app);
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
        desiredCapabilities.setCapability("platform", platform);
        desiredCapabilities.setCapability("platformName", platformName);
        desiredCapabilities.setCapability("platformVersion", platformVersion);
        desiredCapabilities.setCapability("deviceName", deviceName);
        desiredCapabilities.setCapability("newCommandTimeout", newCommandTimeout);
        desiredCapabilities.setCapability("automationName", automationName);
        desiredCapabilities.setCapability("version", version);
        desiredCapabilities.setCapability("autoLaunch", autoLaunch);
        desiredCapabilities.setCapability("app", app);
        desiredCapabilities.setCapability("fullReset", "true");
        desiredCapabilities.setCapability("rotatable", "true");
        return desiredCapabilities;
    }

    protected IOSDriver createWebDriver() throws IWebDriverException {
        try {
            printCapabilities(getDesiredCapabilities());
            return new IOSDriver(new URL(getBaseTestUrl()), getDesiredCapabilities());
        } catch (IOException e) {
            throw new IWebDriverException("Error starting appium driver service", e);
        }
    }

    @Override
    public IOSSeleniumActions getActions() {
        return new IOSSeleniumActions(this);
    }

    /**
     * Swipe from the right to left for a second
     */
    public void swipeLeft() {
        super.swipeLeft();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "left");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the left to right for a second
     */
    public void swipeRight() {
        super.swipeRight();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "right");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the top to buttom for a second
     */
    public void dragDown() {
        int midScreen = getScreenWidth() / 2;
        webDriver.swipe(midScreen, 140, midScreen, getScreenHeight() - 140, 1500);
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "up");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the down to up for a second
     */
    public void dragUp() {
        int midScreen = getScreenWidth() / 2;
        webDriver.swipe(midScreen, getScreenHeight() - 140, midScreen, 140, 1500);
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "down");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Will function only with real device
     *
     * @param startX   - 0 is the left side of the smart-phone
     * @param endX     - coordinate to stop swipe
     * @param startY   - 0 is the upper side of the smart-phone
     * @param endY     - coordinate to stop swipe
     * @param duration - in milliseconds
     */
    public void swipe(int startX, int endX, int startY, int endY, int duration) {
        webDriver.swipe(startX, startY, endX, endY, duration);
    }

    /**
     * Uses iOS functionality of automatic scroll to top when clicking status bar
     */
    public void scrollToTop() {
        getWebDriver().findElementByClassName("UIAStatusBar").click();
    }

    public void openNotifications() {
        int midScreenWidth = getScreenWidth() / 2;
        webDriver.swipe(midScreenWidth, 0, midScreenWidth, getScreenHeight(), 1000);
        webDriver.quit();
    }
}
