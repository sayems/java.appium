package com.sayem.appium.pagefactory.framework.browser.mobile;

import com.sayem.appium.pagefactory.framework.actions.AndroidSeleniumActions;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.WebDriverException;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

public class AndroidMobileBrowser extends MobileBrowser {

    private String appPackage;
    private String appActivity;

    public AndroidMobileBrowser(String baseTestUrl,
                                String browserName,
                                String platformName,
                                String platformVersion,
                                String deviceName,
                                String app,
                                String appPackage,
                                String appActivity,
                                TimeoutsConfig timeouts) throws WebDriverException {
        super(baseTestUrl, timeouts, browserName, platformName, platformVersion, deviceName, app);
        this.appPackage = appPackage;
        this.appActivity = appActivity;
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        desiredCapabilities.setCapability(MobileCapabilityType.APP, app);
        desiredCapabilities.setCapability(MobileCapabilityType.APP_PACKAGE, appPackage);
        desiredCapabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, appActivity);
        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        return desiredCapabilities;
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
}
