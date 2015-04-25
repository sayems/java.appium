package com.sayem.appium.pagefactory.framework.browser.web;

import com.sayem.appium.pagefactory.framework.actions.FirefoxSeleniumActions;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;
import java.util.Set;

public class FirefoxBrowser extends WebBrowser {

    private static final Logger logger = LoggerFactory.getLogger(FirefoxBrowser.class);

    public FirefoxBrowser(String baseTestUrl,
                          TimeoutsConfig timeoutsConfig,
                          Optional<String> webDriverPath,
                          Optional<String> browserBinaryPath,
                          Optional<String> browserVersion,
                          Optional<String> browserLocale,
                          Optional<Integer> startWindowWidth,
                          Optional<Integer> startWindowHeight,
                          Optional<Platform> platform) {

        super(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, browserVersion, browserLocale, startWindowWidth, startWindowHeight, platform);
    }

    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.FIREFOX;
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();

        setCommonWebBrowserCapabilities(desiredCapabilities);

        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

        FirefoxProfile profile = new FirefoxProfile();
        profile.setEnableNativeEvents(true);
        desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);

        // If the browerBinaryPath is present, and it points to a real file, then set this as the Firefox Binary
        Optional<String> browserBinaryPath = getBrowserBinaryPath();
        if (browserBinaryPath.isPresent() && !browserBinaryPath.get().isEmpty()) {
            final String browserBinaryPathStr = browserBinaryPath.get();
            File file = new File(browserBinaryPathStr);
            if (file.exists()) {
                desiredCapabilities.setCapability(FirefoxDriver.BINARY, new FirefoxBinary(file));
            }
        }

        return desiredCapabilities;
    }

    @Override
    protected WebDriver createWebDriver() {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();
        return new FirefoxDriver(desiredCapabilities);
    }

    @Override
    public FirefoxSeleniumActions getActions() {
        return new FirefoxSeleniumActions(this);
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        if (webDriver == null) {
            logger.info("WebDriver was null in FirefoxBrowser#getBrowserLogEntries! Returning null.");
            return null;
        }
        logger.debug("Getting available log types...");
        Set<String> availableLogTypes = webDriver.manage().logs().getAvailableLogTypes();
        logger.debug("Found log types: {}", availableLogTypes);
        if (availableLogTypes == null || !availableLogTypes.contains(LogType.BROWSER)) {
            return null;
        }
        LogEntries logs = webDriver.manage().logs().get(LogType.BROWSER);
        logger.info("Success - obtained Browser logs for a local FirefoxBrowser!");
        return logs;
    }
}
