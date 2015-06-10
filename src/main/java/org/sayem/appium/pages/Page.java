package org.sayem.appium.pages;

import org.sayem.appium.actions.SeleniumActions;
import org.openqa.selenium.By;

import javax.annotation.Nullable;

public interface Page {

    /**
     * Get the SeleniumActions for the current page and browser, for how to interact with the page using Selenium.
     */
    SeleniumActions getActions();

    /**
     * Set the selenium actions.
     */
    void setActions(SeleniumActions actions);

    /**
     * Override this to have a page do something different when a page is loaded.
     * Make sure to call super.pageLoadHook(), since the BasicPage class defines the check that the page identifier is present.
     */
    void pageLoadHook();

    /**
     * A Selenium Locator that uniquely identifies a page as being successfully loaded.
     * Return null to not verify any element is present on page load.
     */
    @Nullable
    By getPageIdentifier();

    /**
     * How to initialize sub-page fields defined with the @SubPage annotation
     */
    void initSubPages();

    /**
     * Call this if a Page has been refreshed by javascript, so it has stale WebElement member variables.
     * This is useful when a method of a Page class performs actions requiring multiple refreshes.
     * In other situations, you can just use Browser#refreshPage() to get an entirely new Page instance.
     */
    void refreshElements();

    /**
     * Call this to do a full page refresh with the browser, and refresh the web elements as well.
     */
    void refreshPage();

}
