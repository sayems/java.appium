package org.sayem.appium.browser.web;

import org.sayem.appium.actions.SafariSeleniumActions;
import org.sayem.appium.config.TimeoutsConfig;
import org.sayem.appium.exception.IWebDriverException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class SafariBrowser extends WebBrowser {
    private static final Logger logger = LoggerFactory.getLogger(SafariBrowser.class);

    public SafariBrowser(String baseTestUrl,
                         TimeoutsConfig timeouts,
                         Optional<String> driverPath,
                         Optional<String> browserBinaryPath,
                         Optional<String> browserVersion,
                         Optional<String> browserLocale,
                         Optional<Integer> startWindowWidth,
                         Optional<Integer> startWindowHeight,
                         Optional<Level> browserLogLevel,
                         Optional<String> browserLogFile,
                         Optional<Platform> platform) {

        super(baseTestUrl, timeouts, driverPath, browserBinaryPath, browserVersion, browserLocale,
                startWindowWidth, startWindowHeight, browserLogLevel, browserLogFile, platform);
    }

    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.SAFARI;
    }

    @Override
    public LoggingPreferences getLoggingPreferences() {
        Level level = getLogLevel();
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, level);
        loggingPreferences.enable(LogType.DRIVER, level);
        return loggingPreferences;
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.safari();

        setCommonWebBrowserCapabilities(desiredCapabilities);

        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseCleanSession(true);

        desiredCapabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);

        return desiredCapabilities;
    }

    @Override
    public SafariSeleniumActions getActions() {
        return new SafariSeleniumActions(this);
    }

    @Override
    protected WebDriver createWebDriver() throws IWebDriverException {

        return new SafariDriver(getDesiredCapabilities());
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        if (webDriver == null) {
            logger.info("WebDriver was null in ChromeBrowser#getBrowserLogEntries! Returning null.");
            return null;
        }
        logger.debug("Getting available log types...");
        Set<String> availableLogTypes = webDriver.manage().logs().getAvailableLogTypes();
        logger.debug("Found log types: {}", availableLogTypes);
        if (availableLogTypes == null || !availableLogTypes.contains(LogType.BROWSER)) {
            return null;
        }
        LogEntries logs = webDriver.manage().logs().get(LogType.BROWSER);
        logger.info("Success - obtained Browser logs for a local ChromeBrowser!");
        return logs;
    }
}
