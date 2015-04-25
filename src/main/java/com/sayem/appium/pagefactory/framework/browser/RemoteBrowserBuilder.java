package com.sayem.appium.pagefactory.framework.browser;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.sayem.appium.pagefactory.framework.browser.web.*;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.IWebDriverException;
import org.openqa.selenium.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.logging.Level;

/**
 * <p>Builder class for creating a {@link com.sayem.appium.pagefactory.framework.browser.web.RemoteBrowser}.
 * A RemoteBrowser is a browser running in a Selenium Grid, that works
 * by connecting to a Selenium Hub. See https://code.google.com/p/selenium/wiki/Grid2</p>
 * <p>
 * <p>In other words, a RemoteBrowser is a wrapper around a {@link org.openqa.selenium.remote.RemoteWebDriver}
 * that simplifies configuration and unifies options across all Browsers.</p>
 * <p>
 * <p>You can call {@link #getBuilder(WebBrowserType, String, String)} to get a builder, or you can equivalently call
 * {@link #getChromeBuilder(String, String)}, {@link #getFirefoxBuilder(String, String)}, or {@link #getInternetExplorerBuilder(String, String)}.
 * <p>
 * Calling RemoteBrowserBuilder.getBuilder(BrowserType.CHROME, ...)
 * is equivalent to calling RemoteBrowserBuilder.getChromeBuilder(...).</p>
 */
public class RemoteBrowserBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RemoteBrowserBuilder.class);

    private final WebBrowserType browserType;
    private final String baseTestUrl;
    private final String seleniumHubURL;

    private TimeoutsConfig timeoutsConfig;

    private Optional<String> browserVersion = Optional.empty();
    private Optional<String> browserLocale = Optional.empty();
    private Optional<Integer> startWindowWidth = Optional.empty();
    private Optional<Integer> startWindowHeight = Optional.empty();
    private Optional<Level> browserLogLevel = Optional.empty();
    private Optional<String> browserLogFile = Optional.empty();
    private Optional<Platform> platform = Optional.empty();

    private RemoteBrowserBuilder(WebBrowserType browserType,
                                 String baseTestUrl,
                                 String seleniumHubURL) {
        this.browserType = Preconditions.checkNotNull(browserType, "You must provide a non-null BrowserType!");
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl, "You must provide a non-null baseTestUrl!");
        this.seleniumHubURL = Preconditions.checkNotNull(seleniumHubURL, "You must provide a non-null seleniumHubURL");
        this.timeoutsConfig = TimeoutsConfig.defaultTimeoutsConfig();
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid.
     *
     * @param browserType    - CHROME, FIREFOX, or IE
     * @param baseTestUrl    - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getBuilder(WebBrowserType browserType,
                                                  String baseTestUrl,
                                                  String seleniumHubURL) {
        return new RemoteBrowserBuilder(browserType, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For CHROME browser.
     *
     * @param baseTestUrl    - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getChromeBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.CHROME, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For FIREFOX browser.
     *
     * @param baseTestUrl    - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getFirefoxBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.FIREFOX, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For IE browser.
     *
     * @param baseTestUrl    - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getInternetExplorerBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.IE, baseTestUrl, seleniumHubURL);
    }

    //------------Getters in case the client wants to inspect the config they have so far-----------
    public WebBrowserType getBrowserType() {
        return browserType;
    }

    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public String getSeleniumHubURL() {
        return seleniumHubURL;
    }

    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    public Optional<String> getBrowserVersion() {
        return browserVersion;
    }

    public Optional<String> getBrowserLocale() {
        return browserLocale;
    }

    public Optional<Integer> getStartWindowWidth() {
        return startWindowWidth;
    }

    public Optional<Integer> getStartWindowHeight() {
        return startWindowHeight;
    }

    public Optional<Level> getBrowserLogLevel() {
        return browserLogLevel;
    }

    public Optional<String> getBrowserLogFile() {
        return browserLogFile;
    }

    public Optional<Platform> getPlatform() {
        return platform;
    }

    /**
     * Creates the RemoteBrowser instance, which includes creating the actual Browser process via the underlying WebDriver.
     *
     * @return - a {@link com.sayem.appium.pagefactory.framework.browser.web.RemoteBrowser},
     * @throws IWebDriverException when something goes wrong with creating a new WebDriver.
     */
    public RemoteBrowser build() throws IWebDriverException {
        logger.info("Building Remote Browser with the following config: \n{}", toString());
        WebBrowser browser;
        switch (browserType) {
            case FIREFOX:
                browser = new FirefoxBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight, platform);
                break;
            case CHROME:
                browser = new ChromeBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, platform);
                break;
            case IE:
                browser = new InternetExplorerBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, platform);
                break;
            case SAFARI:
                browser = new SafariBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, Optional.empty());
                break;
            default:
                throw new IllegalArgumentException("Only Firefox, Chrome, and IE are currently supported!");
        }
        RemoteBrowser remoteBrowser = new RemoteBrowser(browser, seleniumHubURL);
        remoteBrowser.initializeBrowser();
        return remoteBrowser;
    }

    public RemoteBrowserBuilder withTimeoutsConfig(TimeoutsConfig timeoutsConfig) {
        this.timeoutsConfig = timeoutsConfig == null ? TimeoutsConfig.defaultTimeoutsConfig() : timeoutsConfig;
        return this;
    }

    public RemoteBrowserBuilder withBrowserVersion(String browserVersion) {
        this.browserVersion = Optional.ofNullable(browserVersion);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLocale(String browserLocale) {
        this.browserLocale = Optional.ofNullable(browserLocale);
        return this;
    }

    public RemoteBrowserBuilder withStartWindowWidth(Integer startWindowWidth) {
        this.startWindowWidth = Optional.ofNullable(startWindowWidth);
        return this;
    }

    public RemoteBrowserBuilder withStartWindowHeight(Integer startWindowHeight) {
        this.startWindowHeight = Optional.ofNullable(startWindowHeight);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLogLevel(Level browserLogLevel) {
        this.browserLogLevel = Optional.ofNullable(browserLogLevel);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLogFile(String browserLogFile) {
        this.browserLogFile = Optional.ofNullable(browserLogFile);
        return this;
    }

    public RemoteBrowserBuilder withPlatform(Platform platform) {
        this.platform = Optional.ofNullable(platform);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("browserType", browserType)
                .add("baseTestUrl", baseTestUrl)
                .add("seleniumHubURL", seleniumHubURL)
                .add("browserVersion", browserVersion)
                .add("browserLocale", browserLocale)
                .add("startWindowWidth", startWindowWidth)
                .add("startWindowHeight", startWindowHeight)
                .add("browserLogLevel", browserLogLevel)
                .add("browserLogFile", browserLogFile)
                .add("platform", platform)
                .toString();
    }

}
