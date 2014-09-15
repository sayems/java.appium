package com.sayem.appium.pagefactory.framework.browser;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.sayem.appium.pagefactory.framework.browser.mobile.AndroidMobileBrowser;
import com.sayem.appium.pagefactory.framework.browser.mobile.IOSMobileBrowser;
import com.sayem.appium.pagefactory.framework.browser.mobile.MobileBrowser;
import com.sayem.appium.pagefactory.framework.browser.mobile.MobilePlatformName;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>Builder class for creating an App that is running on an emulator or a connected device that connected
 *    to the same host as the test code.
 *    Creates either a {@link com.sayem.appium.pagefactory.framework.browser.mobile.AndroidMobileBrowser},
 *    {@link com.sayem.appium.pagefactory.framework.browser.mobile.IOSMobileBrowser}..</p>
 *
 *  <p>A Browser is basically a wrapper for a WebDriver that greatly simplifies configuration,
 *  adds useful utilities, and has methods
 *  for loading {@link com.sayem.appium.pagefactory.framework.pages.Page}'s.
 *
 *  Pages provide an object-oriented solution to Selenium testing. You can write Page classes that model a web page
 *  in the web app you are testing.</p>
 */

public class MobileBrowserBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MobileBrowserBuilder.class);

    private String baseTestUrl;
    private TimeoutsConfig timeoutsConfig;
    private String browserName;
    private MobilePlatformName platformName;
    private String platformVersion;
    private String deviceName;
    private String app;
    private String appPackage;
    private String appActivity;


    private MobileBrowserBuilder(String baseTestUrl,
                                 MobilePlatformName platformName) {
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl, "You must provide a non-null baseTestUrl!");
        this.timeoutsConfig = TimeoutsConfig.defaultTimeoutsConfig();
        this.platformName = Preconditions.checkNotNull(platformName, "You must provide a non-null platformName!");

    }

    //------------Getters in case the client wants to inspect the config they have so far-----------
    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    public String getBrowserName() {
        return browserName;
    }

    public MobilePlatformName getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getApp() {
        return app;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }


    /**
     * Get a MobileBrowserBuilder for Android and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static MobileBrowserBuilder getAndroidBuilder(String baseTestUrl) {
        return new MobileBrowserBuilder(baseTestUrl, MobilePlatformName.ANDROID);
    }

    /**
     * Get a MobileBrowserBuilder for iOS and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static MobileBrowserBuilder getIOSBuilder(String baseTestUrl) {
        return new MobileBrowserBuilder(baseTestUrl, MobilePlatformName.IOS);
    }


    /**
     * Creates the MobileBrowser instance, which includes creating the actual Browser process via the underlying Appium
     * Server
     * @return - a {@link com.sayem.appium.pagefactory.framework.browser.mobile.AndroidMobileBrowser},
     * {@link com.sayem.appium.pagefactory.framework.browser.mobile.IOSMobileBrowser}
     * @throws com.sayem.appium.pagefactory.framework.exception.WebDriverException
     */
    public MobileBrowser build() throws WebDriverException {
        logger.info("Building Mobile Browser with the following config: \n{}", toString());
        MobileBrowser browser;
        switch (platformName) {
            case ANDROID:
                browser = new AndroidMobileBrowser(baseTestUrl, browserName, platformName.getPlatformName(), platformVersion,
                        deviceName, app, appPackage, appActivity, timeoutsConfig);
                break;
            case IOS:
                browser = new IOSMobileBrowser(baseTestUrl, browserName, platformName.getPlatformName(), platformVersion,
                        deviceName, app, timeoutsConfig);
                break;
            default:
                throw new IllegalArgumentException("Only IOS and Android are currently supported!");
        }
        browser.initializeBrowser();
        return browser;
    }

    public MobileBrowserBuilder withTimeoutsConfig(TimeoutsConfig timeoutsConfig) {
        this.timeoutsConfig = timeoutsConfig == null ? TimeoutsConfig.defaultTimeoutsConfig() : timeoutsConfig;
        return this;
    }

    public MobileBrowserBuilder withBrowserName(String browserName) {
        this.browserName = browserName;
        return this;
    }

    public MobileBrowserBuilder withPlatformName(MobilePlatformName platformName) {
        this.platformName = platformName;
        return this;
    }

    public MobileBrowserBuilder withPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    public MobileBrowserBuilder withDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public MobileBrowserBuilder withApp(String app) {
        this.app = app;
        return this;
    }

    public MobileBrowserBuilder withAppPackage(String appPackage) {
        this.appPackage = appPackage;
        return this;
    }

    public MobileBrowserBuilder withAppActivity(String appActivity) {
        this.appActivity = appActivity;
        return this;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("baseTestUrl", baseTestUrl)
                .add("browserName", browserName)
                .add("platformName", platformName.getPlatformName())
                .add("platformVersion", platformVersion)
                .add("deviceName", deviceName)
                .add("app", app)
                .add("appPackage", appPackage)
                .add("appActivity", appActivity)
                .toString();
    }
}
