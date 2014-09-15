package com.sayem.appium.pagefactory.framework.actions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.sayem.appium.pagefactory.framework.browser.Browser;
import com.sayem.appium.pagefactory.framework.config.TimeoutType;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.WebDriverException;
import com.sayem.appium.pagefactory.framework.exception.SeleniumActionsException;
import com.sayem.appium.pagefactory.framework.pages.SubPage;
import com.sayem.appium.pagefactory.framework.pages.TopLevelPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * <p>
 * This interface represents actions that one can perform with a Browser interacting with a web page.
 * Upon creation, a Browser instance will instantiate the correct type of SeleniumActions.
 * Pages are endowed with a SeleniumActions when created via standard methods such as
 * {@link Browser#loadTopLevelPage}.
 * </p>
 * <p>
 * Throughout, the {@link com.sayem.appium.pagefactory.framework.config.TimeoutType} enum is used.
 * This works as follows: if you provide DEFAULT as
 * the argument to a method, then the default Timeout for the given context is used. For example, if you are
 * calling "findElementContainingTextWithRefresh" then using DEFAULT will result in TimeoutType.POLLING_WITH_REFRESH_TIMEOUT
 * being used, because that method refreshes the page and polls until it finds an element containing the given text.
 * </p>
 * <p>
 * If you use anything other than TimeoutType.DEFAULT, then you are overriding the timeout for the given method.
 * This is still restricted to the timeouts defined by the enum, so that timeouts are configurable and we have
 * fewer magic numbers in our code.
 * </p>
 */
public interface SeleniumActions {

    /**
     * Get a {@link org.openqa.selenium.interactions.Actions} object--used to build sequences of actions like clicking + dragging
     */
    public Actions getActionsBuilder();

    public <B extends Browser> B getBrowser();

    public void setBrowser(Browser browser);

    /**
     * Wait for a javascript confirmation dialog to be present, then accept it.
     */
    public void acceptAlert(TimeoutType timeout);

    /**
     * Wait for a javascript confirmation dialog to be present, then dismiss it.
     */
    public void dismissAlert(TimeoutType timeout);

    /**
     * Click without polling for the element to be clickable or waiting until it's ready.
     * Uses the implicit wait timeout built-in to Selenium.
     *
     * @throws com.sayem.appium.pagefactory.framework.exception.WebDriverException - if the element isn't clickable when this method is called.
     */
    public WebElement clickNoWait(By locator) throws WebDriverException;

    /**
     * Click the web element defined by the given CSS, with proper waiting until the element is clickable.
     */
    public WebElement click(By locator, TimeoutType timeout);

    /**
     * Click the given web element, with proper waiting until the element is clickable.
     */
    public WebElement click(WebElement el, TimeoutType timeout);

    /**
     * Click a button or link that redirects the browser to a new Page, then load the new Page and return it.
     *
     * @param locatorToClick - locator of the element to be clicked
     * @param pageClass  - Class of the {@link TopLevelPage} object to be returned.
     * @param timeout    - timeout object indicating how long to wait, or just TIMEOUT.DEFAULT to use the default
     * @return - the fully initialized {@link TopLevelPage} object
     */
    public <T extends TopLevelPage> T clickAndLoadTopLevelPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout);

    public <T extends TopLevelPage> T clickAndLoadTopLevelPage(WebElement el, Class<T> pageClass, TimeoutType timeout);

    /**
     * Click a button or link, then load a SubPage that will be added to the DOM after the click.
     *
     * @param locatorToClick - locator of the element to be clicked
     * @param pageClass  - class of the {@link SubPage} object to be returned.
     * @param timeout    - timeout object indicating how long to wait, or just TIMEOUT.DEFAULT to use the default
     * @return - the fully initialized {@link SubPage} object
     */
    public <T extends SubPage> T clickAndLoadSubPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout);

    public <T extends SubPage> T clickAndLoadSubPage(WebElement el, Class<T> pageClass, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is present on the DOM (not necessarily visible).
     *
     * @return - the WebElement we verified was present
     */
    public WebElement clickAndVerifyPresent(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout);

    public WebElement clickAndVerifyPresent(WebElement elToClick, By locatorToVerifyPresent, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is present on the DOM (not necessarily visible).
     *
     * @return - the WebElement we verified was present
     */
    public WebElement clickAndVerifyVisible(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout);

    public WebElement clickAndVerifyVisible(WebElement elToClick, By locatorToVerifyPresent, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is NOT present on the DOM (so also not visible).
     */
    public void clickAndVerifyNotPresent(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout);

    public void clickAndVerifyNotPresent(WebElement elToClick, By locatorToVerifyPresent, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is NOT present on the DOM (so also not visible).
     */
    public void clickAndVerifyNotVisible(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout);

    public void clickAndVerifyNotVisible(WebElement elToClick, By locatorToVerifyPresent, TimeoutType timeout);

    /**
     * Click a web element defined by CSS cssToClick, then click a popup that is required to be displayed after clicking.
     *
     * @param locatorToClick - locator for the element to be clicked
     * @param popoverLocator - locator for the popover element that must be present after clicking
     */
    public void clickAndSelectFromList(By locatorToClick, By popoverLocator);

    public void clickAndSelectFromList(WebElement clickable, By popoverLocator);

    /**
     * Clear text from an input element.
     *
     * @param locator - locator defining the input element
     * @return - the input element
     */
    public WebElement clearText(By locator);

    public WebElement clearText(WebElement el);

    /**
     * Execute the given javascript synchronously and return the result.
     *
     * @return - see Selenium docs for what can be returned here, probably just Integer, Boolean, Double, or null
     */
    public Object executeJavascript(String script);

    /**
     * Wait for the given symbol to be defined AND non-null in javascript
     */
    public void waitForJavascriptSymbolToBeDefined(String symbol, TimeoutType timeout);

    public void waitForJavascriptSymbolToHaveValue(String symbol, String value, TimeoutType timeout);

    /**
     * Immediately return true or false as to whether a web element exists on the page.
     */
    public boolean exists(By locator);

    public boolean exists(By locator, WebElement parentEl);

    public WebElement findElementWithRefresh(By locator, TimeoutType timeout);

    public WebElement findVisibleElementWithRefresh(By locator, TimeoutType timeout);

    public WebElement findElementContainingText(By locator, String text);

    public WebElement findVisibleElementContainingText(By locator, String text);

    public WebElement findVisibleElementContainingTextWithWait(final By locator, final String text, TimeoutType timeout);

    public WebElement findElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout);

    public WebElement findVisibleElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout);

    public WebElement findElementContainingTextWithWait(By locator, String text, TimeoutType timeout);

    /**
     * Immediately try to return a WebElement without any implicit or explicit waiting.
     *
     * @return - the WebElement, or null if not present.
     */
    public
    @Nullable
    WebElement getElement(By locator);

    public
    @Nullable
    WebElement getChildElement(By locator, WebElement parentEl);

    /**
     * Get a WebElement using the implicit wait configured for the Selenium WebDriver.
     *
     * @return the WebElement when found. Null is never returned.
     * @throws RuntimeException - if the web element isn't present after waiting.
     */
    public
    @Nonnull
    WebElement getElementWithWait(By locator);

    public
    @Nonnull
    WebElement getChildElementWithWait(By locator, WebElement parentEl);

    public List<WebElement> getElements(By locator);

    public List<WebElement> getChildElements(By locator, WebElement parentEl);

    public WebElement getParentElement(WebElement el);

    public WebElement inputText(By locator, String text);

    public WebElement inputText(@Nonnull WebElement el, String text);

    public WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException;

    public WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator, int withRetryCount) throws SeleniumActionsException;

    public WebElement inputTextSlowly(By locator, String text);

    public WebElement inputTextSlowly(WebElement el, String text);

    public WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException;

    public WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator, int withRetryCount) throws SeleniumActionsException;

    /**
     * Enter the given text into the input defined by inputCSS, one character at a time.
     * At each step, verify the previous popup was removed from the DOM, and find the new popup.
     * Then see if there is a popup containing the required text on the page. If so, click it and return.
     *
     * @param inputLocator          - locator for the input element
     * @param text              - text you are entering into the input element
     * @param popoverLocator      - locator for the popup element containing required text, or list element if there multiple
     * @param requiredPopupText - text required to be present in popup element defined by popupItemCss
     */
    public void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, String text, By popoverLocator,
                                                            String requiredPopupText);

    /**
     * Same as above, but enter minChars characters before checking for the popup to exist.
     *
     * @param minChars
     */
    public void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, int minChars, String text, By popoverLocator,
                                                            String requiredPopupText);


    /**
     * Enter text into the active tiny MCE editor.
     */
    public void inputTinyMceText(String text);

    /**
     * Wait for tinyMCE.activeEditor.initialized to be true, see Tiny MCE documentation online for why.
     */
    public void waitForTinyMceToBeReady();

    /**
     * Return immediately with an answer as to whether an element is clickable.
     *
     * @return - true if the element is present and clickable, false otherwise.
     */
    public boolean isClickable(By locator);

    public boolean isClickable(WebElement el);

    /**
     * Return immediately with an answer as to whether an element is visible.
     *
     * @return - true if the element is present and visible, false otherwise.
     * See Selenium's docs for the definition of visible, it has to be on the page, scrolled into view,
     * have a height and width > 0, etc.
     */
    public boolean isVisible(By locator);

    public boolean isVisible(WebElement css);

    /**
     * Scroll so that the element is in the middle of the page.
     */
    public void scrollIntoView(By locator);

    public void scrollIntoView(WebElement el);

    /**
     * Scroll the given element with a scroll bar defined by parentCSS so that the web element given by css is in view
     */
    public void scrollIntoView(By scrollContainerLocator, By locator);

    public void scrollIntoView(By scrollContainerLocator, WebElement el);

    /**
     * Returns a Page object with initialized WebElements that is a valid page class for the currently open page in the web driver.
     *
     * @param pageClass - a Class representing the type of page to be initialized.
     */
    public <T extends TopLevelPage> T loadTopLevelPage(Class<T> pageClass);

    public <T extends SubPage> T loadSubPage(Class<T> pageClass);

    public void verifyElementContainsText(By locator, String text, TimeoutType timeout);

    public void verifyElementSelected(By locator, TimeoutType timeout);

    public void verifyElementSelected(WebElement el, TimeoutType timeout);

    public void verifyElementNotSelected(By locator, TimeoutType timeout);

    public void verifyElementNotSelected(WebElement el, TimeoutType timeout);

    public WebElement verifyElementPresented(By locator, TimeoutType timeout);

    public void verifyElementNotPresented(By locator, TimeoutType timeout);

    public void verifyElementWithTextNotPresented(By locator, String text, TimeoutType timeout);

    public WebElement verifyElementVisible(By locator, TimeoutType timeout);

    public void verifyElementInvisible(By locator, TimeoutType timeout);

    public void verifyElementWithTextIsInvisible(By locator, String text, TimeoutType timeout);

    /* Method to simplify general waiting code in Pages and Keywords. Takes a function and waits until the return value is non-null.*/
    public <T, V> V waitOnFunction(Function<T, V> function, T input, String message, TimeoutType timeout);

    /* Method to simplify general waiting code in Pages and Keywords. Takes a predicate and waits until it returns true.*/
    public <T> void waitOnPredicate(Predicate<T> predicate, T input, String message, TimeoutType timeout);

    /* Same, but a helper for predicates that don't require an Input, because they use closure to interact with a containing class. */
    public void waitOnPredicate(Predicate<Object> predicate, String message, TimeoutType timeout);

    /* Same, but refresh the page after each time the predicate is checked. */
    public <T> void waitOnPredicateWithRefresh(Predicate<T> predicate, T input, String message, TimeoutType timeout);

    /* Same, but no input is required. */
    public void waitOnPredicateWithRefresh(Predicate<Object> predicate, String message, TimeoutType timeout);

    public <T> T waitOnExpectedCondition(ExpectedCondition<T> expectedCondition, String message, TimeoutType timeout);

    public WebElement verifyPageRefreshed(WebElement elementFromBeforeRefresh, By locatorAfterRefresh, TimeoutType timeout);

    public WebElement waitUntilClickable(By locator, TimeoutType timeout);

    public WebElement waitUntilClickable(WebElement el, TimeoutType timeout);

    /**
     * @return true if-and-only-if the web element found by the given locator has the CSS class "cssClass"
     */
    public boolean doesElementHaveClass(By locator, String locatorClass);

    public WebElement verifyElementHasClass(By locator, String locatorClass, TimeoutType timeout);

    public WebElement verifyElementDoesNotHaveClass(final By locator, final String locatorClass, TimeoutType timeout);

    //////////////////////////////////////Timeouts//////////////////////////////////////////////
    public TimeoutsConfig getTimeoutsConfig();
}
