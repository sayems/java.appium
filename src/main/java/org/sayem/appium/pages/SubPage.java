package org.sayem.appium.pages;

import org.openqa.selenium.By;

public interface SubPage extends Page {
    /**
     * get parent page from the subpages.
     */
    Page getParent();

    /**
     * Set parent page for the subpages.
     */
    void setParent(Page parent);

    boolean hasParent();

    /**
     * A Selenium locator that returns page container locator; useful for nav bars, side bars,
     * inbox message items.
     */
    By getPageContainer();
}
