package org.sayem.appium.browser.web;

import com.google.common.collect.Maps;
import org.sayem.appium.actions.ChromeSeleniumActions;
import org.sayem.appium.config.TimeoutsConfig;
import org.sayem.appium.exception.IWebDriverException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class ChromeBrowser extends WebBrowser {
    private static final Logger logger = LoggerFactory.getLogger(ChromeBrowser.class);

    public ChromeBrowser(String baseTestUrl,
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
        return WebBrowserType.CHROME;
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
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

        setCommonWebBrowserCapabilities(desiredCapabilities);

        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

        // If the locale option is present and is not empty, then set this option in Chromedriver
        Optional<String> browserLocale = getBrowserLocale();
        if (browserLocale.isPresent() && !browserLocale.get().isEmpty()) {
            Map<String, String> chromePrefs = Maps.newHashMap();
            chromePrefs.put("intl.accept_languages", browserLocale.get());
            desiredCapabilities.setCapability("chrome.prefs", chromePrefs);
        }

        // If the browser binary path is present and not empty, then set this as the Chrome Binary file
        Optional<String> browserBinaryPath = getBrowserBinaryPath();
        if (browserBinaryPath.isPresent() && !browserBinaryPath.get().isEmpty()) {
            desiredCapabilities.setCapability("chrome.binary", browserBinaryPath.get());
        }

        // ChromeOptions
        //ChromeOptions options = new ChromeOptions();

        // This tells Chromedriver we're running tests.
        // This eliminates the banner with the message "You are using an unsupported command-line flag --ignore-certificate-errors"
        //options.addArguments("test-type");

        //desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        return desiredCapabilities;
    }

    @Override
    public ChromeSeleniumActions getActions() {
        return new ChromeSeleniumActions(this);
    }

    @Override
    protected WebDriver createWebDriver() throws IWebDriverException {
        Optional<String> driverPath = getWebDriverPath();
        Optional<String> browserLogFile = getBrowserLogFile();

        ChromeDriverService.Builder builder = new ChromeDriverService.Builder()
                .usingAnyFreePort();

        if (driverPath.isPresent() && !driverPath.get().isEmpty()) {
            File chromedriverFile = new File(driverPath.get());
            builder.usingDriverExecutable(chromedriverFile);
        }

        if (browserLogFile.isPresent() && !browserLogFile.get().isEmpty()) {
            builder.withLogFile(new File(browserLogFile.get()));
        }

        ChromeDriverService service = builder.build();

        try {
            service.start();
        } catch (IOException e) {
            throw new IWebDriverException("Error starting Chrome driver service", e);
        }
        return new ChromeDriver(service, getDesiredCapabilities());
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
