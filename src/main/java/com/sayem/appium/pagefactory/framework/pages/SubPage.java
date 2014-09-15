package com.sayem.appium.pagefactory.framework.pages;

import org.openqa.selenium.By;

public interface SubPage extends Page {
    /**
     * Set parent page for the subpages.
     */
    void setParent(Page parent);

    /**
     * get parent page from the subpages.
     */
    Page getParent();

    boolean hasParent();

    /**
     * A Selenium locator that returns page container locator; useful for nav bars, side bars,
     * inbox message items.
     */
    By getPageContainer();
}
