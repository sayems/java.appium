package org.sayem.appium.browser.mobile;

import org.sayem.appium.actions.AndroidSeleniumActions;
import org.sayem.appium.config.TimeoutsConfig;
import org.sayem.appium.exception.IWebDriverException;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class AndroidMobileBrowser extends MobileBrowser {

    private static final Logger logger = LoggerFactory.getLogger(AndroidMobileBrowser.class);
    protected boolean touchMode;
    private String appPackage;
    private String appActivity;

    public AndroidMobileBrowser(String baseTestUrl,
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
                                String appPackage,
                                String appActivity,
                                TimeoutsConfig timeouts,
                                boolean touchMode) throws IWebDriverException {
        super(baseTestUrl, timeouts, browserName, platform, platformName, platformVersion, deviceName,
                newCommandTimeout, automationName, version, autoLaunch, app);
        this.touchMode = touchMode;
        this.appPackage = appPackage;
        this.appActivity = appActivity;
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
        desiredCapabilities.setCapability("appPackage", appPackage);
        desiredCapabilities.setCapability("appWaitActivity", appActivity);
        desiredCapabilities.setCapability("fullReset", true);
        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        return desiredCapabilities;
    }

    protected AndroidDriver createWebDriver() throws IWebDriverException {
        try {
            printCapabilities(getDesiredCapabilities());
            return new SwipeableWebDriver(new URL(getBaseTestUrl()), getDesiredCapabilities());
        } catch (IOException e) {
            throw new IWebDriverException("Error starting appium driver service", e);
        }
    }

    @Override
    public AndroidSeleniumActions getActions() {
        return new AndroidSeleniumActions(this);
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    /**
     * @return true if Android is API 17 or down, and as a result uses touch mode
     */
    public boolean isTouchMode() {
        return touchMode;
    }

    /**
     * @param touchMode - true if Android is API 17 or lower
     */
    public void setTouchMode(boolean touchMode) {
        this.touchMode = touchMode;
    }

    /**
     * Swipe from the top to bottom for a second
     */
    @Override
    public void dragDown() {
        int midScreen = getScreenWidth() / 2;
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            action.down(midScreen, 360).move(midScreen, 300).up(midScreen, 300).perform();
        } else {
            webDriver.swipe(midScreen, 450, midScreen, getScreenHeight() - 250, 1500);
        }
    }

    /**
     * Swipe from the down to up for a second
     */
    @Override
    public void dragUp() {
        int midScreen = webDriver.manage().window().getSize().getWidth() / 2;
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            action.down(midScreen, 300).move(midScreen, 250).up(midScreen, 250).perform();
        } else {
            webDriver.swipe(midScreen, getScreenHeight() - 250, midScreen, 250, 2500);
        }
    }

    /**
     * Swipe from the top to bottom for a second
     *
     * @param yStart - coordinate to start swiping
     * @param yEnd   - coordinate to stop swiping
     */
    @Override
    public void drag(int yStart, int yEnd) {
        int midScreen = getScreenWidth() / 2;
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            action.down(midScreen, yStart).move(midScreen, yEnd).up(midScreen, yEnd).perform();
        } else {
            webDriver.swipe(midScreen, yStart, midScreen, yEnd, 2500);
        }
    }

    /**
     * Swipe from the top to bottom for a second
     *
     * @param yStart - coordinate to start swiping
     * @param yEnd   - coordinate to stop swiping
     */

    @Override
    public void drag(int yStart, int yEnd, int duration) {
        int midScreen = getScreenWidth() / 2;
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            action.down(midScreen, yStart).move(midScreen, yEnd).up(midScreen, yEnd).perform();
        } else {
            webDriver.swipe(midScreen, yStart, midScreen, yEnd, duration);
        }
    }

    @Override
    public void tap(int fingersNum, WebElement webElement, int duration) {
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            try {
                action.down(webElement.getLocation().getX(), webElement.getLocation().getY()).clickAndHold()
                        .release(webElement).perform();
            } catch (NullPointerException e) {

            }
        } else {
            webDriver.tap(fingersNum, webElement, duration);
        }
    }

    @Override
    public void tap(int fingersNum, int xLocation, int yLocation, int duration) {
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            try {
                action.down(xLocation, yLocation).clickAndHold().perform();
            } catch (NullPointerException e) {
                logger.error("Failed To Tap due to NullPointerException", e.getStackTrace());
            }
        } else {
            webDriver.tap(fingersNum, xLocation, yLocation, duration);
        }
    }

//    public void clickHomePage() {
//        webDriver.sendKeyEvent(AndroidKeyCode.HOME);
//    }
//
//    public void clickBack() {
//        webDriver.sendKeyEvent(AndroidKeyCode.BACK);
//    }

    @Override
    public void scrollToTop() {
        logger.error("Method ScrollToTop is not yet implemented");
    }
}
