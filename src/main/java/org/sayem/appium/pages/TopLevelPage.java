package org.sayem.appium.pages;

import javax.annotation.Nonnull;

/**
 * Represents a Page that is a top-level web page.
 */
public interface TopLevelPage extends Page {

    @Nonnull
    String getWebPagePath();

    void leavePageHook();

    long getPageLoadTime();

}
