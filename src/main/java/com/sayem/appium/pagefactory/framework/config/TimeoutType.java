package com.sayem.appium.pagefactory.framework.config;

/**
 * <p>Represents the type of Timeout. This is used rather than allowing Magic Numbers to be used arbitrarily for timeouts.
 * All timeouts represent values in seconds.</p>
 * <p>
 * <p>A TimeoutType is passed into various methods of {@link com.sayem.appium.pagefactory.framework.actions.SeleniumActions}.
 * This indicates whether to use the DEFAULT timeout for the action, or to use a different TimeoutType.</p>
 * <p>
 * <p>The "DEFAULT" value is special. It causes the timeout used to be the default timeout for the context.
 * DEFAULT causes the TimeoutType that is the default for the given method being called to be used.</p>
 * <p>
 * <p>You want to use DEFAULT in the vast majority of cases. The TimeoutType used for a SeleniumActions method should
 * only be overridden when absolutely necessary.</p>
 *
 * @see com.sayem.appium.pagefactory.framework.config.TimeoutsConfig
 * @see com.sayem.appium.pagefactory.framework.browser.LocalBrowserBuilder
 * @see com.sayem.appium.pagefactory.framework.browser.RemoteBrowserBuilder
 */
public enum TimeoutType {
    /**
     * Default timeout. The timeout used is context-sensitive based on the method in SeleniumActions you are calling.
     * This is what you want to use most of the time. *
     */
    DEFAULT,
    /**
     * Timeout to wait for an element to be click-able when attempting to click. *
     */
    CLICK_TIMEOUT,
    /**
     * Timeout to wait for an element to be present on the DOM or to be visible. *
     */
    WEB_ELEMENT_PRESENCE_TIMEOUT,
    /**
     * Timeout waiting for the page to refresh, or for an element to become stale or removed from the DOM. *
     */
    PAGE_REFRESH_TIMEOUT,
    /**
     * Timeout waiting for a new page to load *
     */
    PAGE_LOAD_TIMEOUT,
    /**
     * Timeout waiting on anything that requires refreshing the page many times, e.g. something like an Activity Stream.
     * Used by SeleniumActions#findElementContainingTextWithRefresh and other methods that refresh the page until something is present. *
     */
    POLLING_WITH_REFRESH_TIMEOUT,
    /**
     * Arbitrary short timeout configured by the client. *
     */
    SHORT,
    /**
     * Arbitrary medium timeout configured by the client. *
     */
    MEDIUM,
    /**
     * Arbitrary long timeout configured by the client. *
     */
    LONG,
    /**
     * Fixed timeouts not affected by configuration.
     * Usage is discouraged; but this is useful when you need an exact timeout that can't be changed by configuration. *
     */
    ONE_SECOND, TWO_SECONDS, FIVE_SECONDS, TEN_SECONDS, TWENTY_SECONDS, SIXTY_SECONDS, THIRTY_MINUTES, SIXTY_MINUTES, NINETY_MINUTES, TWO_HOURS
}
