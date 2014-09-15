package com.sayem.appium.pagefactory.framework.browser.mobile;

import com.sayem.appium.pagefactory.framework.actions.IOSSeleniumActions;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;


/**
 * Known bug of Apple from Xcode 5 and iOS 7.1 Simulator - swipe is not working on simulator.
 * As a workaround, using scrollTo in JavaScript.
 * As in real devices regular swipe works but not scrollTo, using the regular command as well
 */

public class IOSMobileBrowser extends MobileBrowser {


    public IOSMobileBrowser(String baseTestUrl,
                            String browserName,
                            String platformName,
                            String platformVersion,
                            String deviceName,
                            String app,
                            TimeoutsConfig timeouts) throws WebDriverException {
        super(baseTestUrl, timeouts, browserName, platformName, platformVersion, deviceName, app);
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
        desiredCapabilities.setCapability("platformName", platformName);
        desiredCapabilities.setCapability("platformVersion", platformVersion);
        desiredCapabilities.setCapability("deviceName", deviceName);
        desiredCapabilities.setCapability("app", app);
        desiredCapabilities.setCapability("rotatable", true);
        return desiredCapabilities;
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
        super.dragDown();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "up");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the down to up for a second
     */
    public void dragUp() {
        super.dragUp();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "down");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     *
     * @param startX - 0 is the left side of the smart-phone
     * @param endX
     * @param startY - 0 is the upper side of the smart-phone
     * @param endY
     * @param duration - in milliseconds
     * Will function only with real device
     */
    public void swipe(int startX, int endX, int startY, int endY, int duration) {
        webDriver.swipe(startX, startY, endX, endY, duration);
    }

    public void scrollToTop() {
        getWebDriver().findElementByClassName("UIAStatusBar").click();
    }
}
