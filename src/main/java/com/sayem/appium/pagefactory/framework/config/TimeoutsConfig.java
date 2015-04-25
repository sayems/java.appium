package com.sayem.appium.pagefactory.framework.config;

import com.google.common.base.Preconditions;

import java.util.concurrent.TimeUnit;

/**
 * <p>Represents the timeout configuration used by a {@link com.sayem.appium.pagefactory.framework.browser.Browser}.</p>
 * <p>
 * <p>Use TimeoutsConfig.defaultTimeoutsConfig() to get an instance with default timeout values.</p>
 * <p>
 * <p>Use TimeoutsConfig.builder() to get a Builder that begins with default timeout values.
 * Then call builder.build() to get a finalized TimeoutsConfig instance.</p>
 *
 * @see TimeoutType
 */
public final class TimeoutsConfig {

    // Standard timeouts for most common usages
    private final int clickTimeoutSeconds;
    private final int webElementPresenceTimeoutSeconds;
    private final int pollingWithRefreshTimeoutSeconds;
    private final int pageRefreshTimeoutSeconds;

    // Arbitrary timeouts configured by client
    private final int shortTimeoutSeconds;
    private final int mediumTimeoutSeconds;
    private final int longTimeoutSeconds;

    // Pauses when polling or entering keys
    private final int pauseBetweenKeysMillis;
    private final int pauseBetweenTriesMillis;
    private final int pauseBetweenRefreshSeconds;

    // Timeouts used for configuring the underlying WebDriver
    private final int pageLoadTimeoutSeconds;
    private final int implicitWaitTimeoutMillis;

    private TimeoutsConfig(int clickTimeoutSeconds, int webElementPresenceTimeoutSeconds, int pollingWithRefreshTimeoutSeconds,
                           int pageRefreshTimeoutSeconds, int shortTimeoutSeconds, int mediumTimeoutSeconds, int longTimeoutSeconds,
                           int pauseBetweenKeysMillis, int pauseBetweenTriesMillis, int pauseBetweenRefreshSeconds,
                           int pageLoadTimeoutSeconds, int implicitWaitTimeoutMillis) {
        this.clickTimeoutSeconds = clickTimeoutSeconds;
        this.webElementPresenceTimeoutSeconds = webElementPresenceTimeoutSeconds;
        this.pollingWithRefreshTimeoutSeconds = pollingWithRefreshTimeoutSeconds;
        this.pageRefreshTimeoutSeconds = pageRefreshTimeoutSeconds;
        this.shortTimeoutSeconds = shortTimeoutSeconds;
        this.mediumTimeoutSeconds = mediumTimeoutSeconds;
        this.longTimeoutSeconds = longTimeoutSeconds;
        this.pauseBetweenKeysMillis = pauseBetweenKeysMillis;
        this.pauseBetweenTriesMillis = pauseBetweenTriesMillis;
        this.pauseBetweenRefreshSeconds = pauseBetweenRefreshSeconds;
        this.pageLoadTimeoutSeconds = pageLoadTimeoutSeconds;
        this.implicitWaitTimeoutMillis = implicitWaitTimeoutMillis;
    }

    /**
     * Return a Builder for constructing a TimeoutsConfig instance.
     * The Builder is populated with default timeouts, and you can modify them as desired, then call builder.build().
     *
     * @return - a {@link com.sayem.appium.pagefactory.framework.config.TimeoutsConfig.Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return a {@link TimeoutsConfig} with default timeout values
     */
    public static TimeoutsConfig defaultTimeoutsConfig() {
        return new Builder().build();
    }

    /**
     * Return the timeout in seconds for the given TimeoutType.
     * <p>
     * It doesn't make sense to pass in the DEFAULT timeout type, because that is a context-sensitive timeout.
     *
     * @param timeout - the TimeoutType (other than DEFAULT) to get the timeout in seconds for.
     * @return - the timeout in seconds
     */
    public int getTimeoutInSeconds(TimeoutType timeout) {
        Preconditions.checkNotNull(timeout, "Cannot get timeout for null timeout.");
        Preconditions.checkArgument(timeout != TimeoutType.DEFAULT, "Can only get the standard timeout for timeout types other than DEFAULT");
        switch (timeout) {

            case CLICK_TIMEOUT:
                return getClickTimeoutSeconds();
            case WEB_ELEMENT_PRESENCE_TIMEOUT:
                return getWebElementPresenceTimeoutSeconds();
            case POLLING_WITH_REFRESH_TIMEOUT:
                return getPollingWithRefreshTimeoutSeconds();
            case PAGE_REFRESH_TIMEOUT:
                return getPageRefreshTimeoutSeconds();
            case PAGE_LOAD_TIMEOUT:
                return getPageLoadTimeoutSeconds();
            case SHORT:
                return getShortTimeoutSeconds();
            case MEDIUM:
                return getMediumTimeoutSeconds();
            case LONG:
                return getLongTimeoutSeconds();
            case ONE_SECOND:
                return 1;
            case TWO_SECONDS:
                return 2;
            case FIVE_SECONDS:
                return 5;
            case TEN_SECONDS:
                return 10;
            case TWENTY_SECONDS:
                return 20;
            case SIXTY_SECONDS:
                return 60;
            case THIRTY_MINUTES:
                return (int) TimeUnit.MINUTES.toSeconds(30);
            case SIXTY_MINUTES:
                return (int) TimeUnit.MINUTES.toSeconds(60);
            case NINETY_MINUTES:
                return (int) TimeUnit.MINUTES.toSeconds(90);
            case TWO_HOURS:
                return (int) TimeUnit.HOURS.toSeconds(2);
            default:
                return getWebElementPresenceTimeoutSeconds();
        }
    }

    public int getClickTimeoutSeconds() {
        return clickTimeoutSeconds;
    }

    public int getWebElementPresenceTimeoutSeconds() {
        return webElementPresenceTimeoutSeconds;
    }

    public int getPollingWithRefreshTimeoutSeconds() {
        return pollingWithRefreshTimeoutSeconds;
    }

    public int getPageRefreshTimeoutSeconds() {
        return pageRefreshTimeoutSeconds;
    }

    public int getShortTimeoutSeconds() {
        return shortTimeoutSeconds;
    }

    public int getMediumTimeoutSeconds() {
        return mediumTimeoutSeconds;
    }

    public int getLongTimeoutSeconds() {
        return longTimeoutSeconds;
    }

    public int getPauseBetweenKeysMillis() {
        return pauseBetweenKeysMillis;
    }

    public int getPauseBetweenTriesMillis() {
        return pauseBetweenTriesMillis;
    }

    public int getPauseBetweenRefreshSeconds() {
        return pauseBetweenRefreshSeconds;
    }

    public int getPageLoadTimeoutSeconds() {
        return pageLoadTimeoutSeconds;
    }

    public int getImplicitWaitTimeoutMillis() {
        return implicitWaitTimeoutMillis;
    }

    public static final class Builder {
        // Standard timeouts for most common usages, all in seconds
        private int clickTimeoutSeconds;
        private int webElementPresenceTimeoutSeconds;
        private int pollingWithRefreshTimeoutSeconds;
        private int pageRefreshTimeoutSeconds;
        // Arbitrary timeouts configured by client, all in seconds
        private int shortTimeoutSeconds;
        private int mediumTimeoutSeconds;
        private int longTimeoutSeconds;
        // Pauses when polling or entering keys
        private int pauseBetweenKeysMillis;
        private int pauseBetweenTriesMillis;
        private int pauseBetweenRefreshSeconds;
        // Timeouts used for configuring the underlying WebDriver
        private int pageLoadTimeoutSeconds;
        private int implicitWaitTimeoutMillis;

        public Builder() {
            this.clickTimeoutSeconds = DefaultTimeouts.CLICK_TIMEOUT_SECONDS;
            this.webElementPresenceTimeoutSeconds = DefaultTimeouts.PRESENCE_TIMEOUT_SECONDS;
            this.pollingWithRefreshTimeoutSeconds = DefaultTimeouts.POLLING_WITH_REFRESH_TIMEOUT_SECONDS;
            this.pageRefreshTimeoutSeconds = DefaultTimeouts.REFRESH_TIMEOUT_SECONDS;
            this.shortTimeoutSeconds = DefaultTimeouts.SHORT_TIMEOUT_SECONDS;
            this.mediumTimeoutSeconds = DefaultTimeouts.MEDIUM_TIMEOUT_SECONDS;
            this.longTimeoutSeconds = DefaultTimeouts.LONG_TIMEOUT_SECONDS;
            this.pauseBetweenKeysMillis = DefaultTimeouts.PAUSE_BETWEEN_KEYS_MILLIS;
            this.pauseBetweenTriesMillis = DefaultTimeouts.PAUSE_BETWEEN_TRIES_MILLIS;
            this.pauseBetweenRefreshSeconds = DefaultTimeouts.PAUSE_BETWEEN_REFRESH_SECONDS;
            this.pageLoadTimeoutSeconds = DefaultTimeouts.PAGE_LOAD_TIMEOUT_SECONDS;
            this.implicitWaitTimeoutMillis = DefaultTimeouts.IMPLICIT_WAIT_TIMEOUT_MILLIS;
        }

        public TimeoutsConfig build() {
            return new TimeoutsConfig(clickTimeoutSeconds,
                    webElementPresenceTimeoutSeconds,
                    pollingWithRefreshTimeoutSeconds,
                    pageRefreshTimeoutSeconds,
                    shortTimeoutSeconds,
                    mediumTimeoutSeconds,
                    longTimeoutSeconds,
                    pauseBetweenKeysMillis,
                    pauseBetweenTriesMillis,
                    pauseBetweenRefreshSeconds,
                    pageLoadTimeoutSeconds,
                    implicitWaitTimeoutMillis);
        }

        /**
         * Set the timeout waiting for an element to be clickable, in seconds
         *
         * @param clickTimeoutSeconds - time in seconds
         */
        public Builder clickTimeoutSeconds(int clickTimeoutSeconds) {
            this.clickTimeoutSeconds = clickTimeoutSeconds;
            return this;
        }

        /**
         * Set the timeout waiting for a web element to be present on the DOM, in seconds.
         *
         * @param webElementPresenceTimeoutSeconds - time in seconds
         */
        public Builder webElementPresenceTimeoutSeconds(int webElementPresenceTimeoutSeconds) {
            this.webElementPresenceTimeoutSeconds = webElementPresenceTimeoutSeconds;
            return this;
        }

        /**
         * Set the timeout for long polling activities such as polling for an element to be present by
         * refreshing the page repeatedly.
         *
         * @param pollingWithRefreshTimeoutSeconds - time in seconds
         */
        public Builder pollingWithRefreshTimeoutSeconds(int pollingWithRefreshTimeoutSeconds) {
            this.pollingWithRefreshTimeoutSeconds = pollingWithRefreshTimeoutSeconds;
            return this;
        }

        /**
         * Set the timeout for waiting for an element to become stale or for the page to be refreshed.
         *
         * @param pageRefreshTimeoutSeconds - time in seconds
         */
        public Builder pageRefreshTimeoutSeconds(int pageRefreshTimeoutSeconds) {
            this.pageRefreshTimeoutSeconds = pageRefreshTimeoutSeconds;
            return this;
        }

        /**
         * Set the "short" timeout in seconds. Arbitrary timeout configurable by client.
         *
         * @param shortTimeoutSeconds - time in seconds
         */
        public Builder shortTimeoutSeconds(int shortTimeoutSeconds) {
            this.shortTimeoutSeconds = shortTimeoutSeconds;
            return this;
        }

        /**
         * Set the "medium" timeout in seconds. Arbitrary timeout configurable by client.
         *
         * @param mediumTimeoutSeconds
         */
        public Builder mediumTimeoutSeconds(int mediumTimeoutSeconds) {
            this.mediumTimeoutSeconds = mediumTimeoutSeconds;
            return this;
        }

        /**
         * Set the "long" timeout in seconds. Arbitrary timeout configurable by client.
         *
         * @param longTimeoutSeconds
         */
        public Builder longTimeoutSeconds(int longTimeoutSeconds) {
            this.longTimeoutSeconds = longTimeoutSeconds;
            return this;
        }

        /**
         * Set the pause between sending keys when entering text slowly.
         *
         * @param pauseBetweenKeysMillis - time in ms
         * @return - the Builder
         */
        public Builder pauseBetweenKeysMillis(int pauseBetweenKeysMillis) {
            this.pauseBetweenKeysMillis = pauseBetweenKeysMillis;
            return this;
        }

        /**
         * Set the pause between tries in milliseconds. This is used for pausing between checks to see if an element
         * is visible.
         *
         * @param pauseBetweenTriesMillis - time in ms
         * @return - the Builder
         */
        public Builder pauseBetweenTriesMillis(int pauseBetweenTriesMillis) {
            this.pauseBetweenTriesMillis = pauseBetweenTriesMillis;
            return this;
        }

        /**
         * Set the pause between refreshing the page when polling for something to be present by refreshing the page
         * repeatedly.
         *
         * @param pauseBetweenRefreshSeconds - time in seconds
         * @return - the Builder
         */
        public Builder pauseBetweenRefreshSeconds(int pauseBetweenRefreshSeconds) {
            this.pauseBetweenRefreshSeconds = pauseBetweenRefreshSeconds;
            return this;
        }

        /**
         * Set the timeout waiting for a new page to load in the web browser.
         * This is both used by the framework and passed on to the Selenium WebDriver for its configuration.
         *
         * @param pageLoadTimeoutSeconds - time in seconds
         * @return - the Builder
         */
        public Builder pageLoadTimoutSeconds(int pageLoadTimeoutSeconds) {
            this.pageLoadTimeoutSeconds = pageLoadTimeoutSeconds;
            return this;
        }

        /**
         * Set the implicit wait timeout for checking if a web element is present.
         * This is used when configuring a Selenium WebDriver.
         *
         * @param implicitWaitTimeoutMillis
         * @return - the Builder
         */
        public Builder implicitWaitTimeoutMillis(int implicitWaitTimeoutMillis) {
            this.implicitWaitTimeoutMillis = implicitWaitTimeoutMillis;
            return this;
        }
    }
}
