package com.sayem.appium.pagefactory.framework.actions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.sayem.appium.pagefactory.framework.browser.Browser;
import com.sayem.appium.pagefactory.framework.config.TimeoutType;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.IWebDriverException;
import com.sayem.appium.pagefactory.framework.exception.SeleniumActionsException;
import com.sayem.appium.pagefactory.framework.pages.SubPage;
import com.sayem.appium.pagefactory.framework.pages.TopLevelPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
     * Wait for a javascript confirmation dialog to be present, then accept it.
     */
    void acceptAlert(TimeoutType timeout);

    /**
     * Clear text from an input element.
     *
     * @param locator - locator defining the input element
     * @return - the input element
     */
    WebElement clearText(By locator);

    WebElement clearText(WebElement el);

    /**
     * Click the web element defined by the given CSS, with proper waiting until the element is clickable.
     */
    WebElement click(By locator, TimeoutType timeout);

    /**
     * Click the given web element, with proper waiting until the element is clickable.
     */
    WebElement click(WebElement el, TimeoutType timeout);

    /**
     * Click a button or link, then load a SubPage that will be added to the DOM after the click.
     *
     * @param locatorToClick - locator of the element to be clicked
     * @param pageClass      - class of the {@link SubPage} object to be returned.
     * @param timeout        - timeout object indicating how long to wait, or just TIMEOUT.DEFAULT to use the default
     * @return - the fully initialized {@link SubPage} object
     */
    <T extends SubPage> T clickAndLoadSubPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout);

    <T extends SubPage> T clickAndLoadSubPage(WebElement el, Class<T> pageClass, TimeoutType timeout);

    /**
     * Click a button or link that redirects the browser to a new Page, then load the new Page and return it.
     *
     * @param locatorToClick - locator of the element to be clicked
     * @param pageClass      - Class of the {@link TopLevelPage} object to be returned.
     * @param timeout        - timeout object indicating how long to wait, or just TIMEOUT.DEFAULT to use the default
     * @return - the fully initialized {@link TopLevelPage} object
     */
    <T extends TopLevelPage> T clickAndLoadTopLevelPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout);

    <T extends TopLevelPage> T clickAndLoadTopLevelPage(WebElement el, Class<T> pageClass, TimeoutType timeout);

    /**
     * Click a web element defined by CSS cssToClick, then click a popup that is required to be displayed after clicking.
     *
     * @param locatorToClick - locator for the element to be clicked
     * @param popoverLocator - locator for the popover element that must be present after clicking
     */
    void clickAndSelectFromList(By locatorToClick, By popoverLocator);

    void clickAndSelectFromList(WebElement clickable, By popoverLocator);

    /**
     * Click a web element, then verify another element is NOT present on the DOM (so also not visible).
     *
     * @param locatorToClick            the locator to click
     * @param locatorToVerifyNotPresent the locator to verify not present after clicking.
     * @param timeout                   the timeout type
     */
    void clickAndVerifyNotPresent(By locatorToClick, By locatorToVerifyNotPresent, TimeoutType timeout);

    void clickAndVerifyNotPresent(WebElement elToClick, By locatorToVerifyNotPresent, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is NOT present on the DOM (so also not visible).
     */
    void clickAndVerifyNotVisible(By locatorToClick, By locatorToVerifyNotVisible, TimeoutType timeout);

    void clickAndVerifyNotVisible(WebElement elToClick, By locatorToVerifyNotVisible, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is present on the DOM (not necessarily visible).
     *
     * @return - the WebElement we verified was present
     */
    WebElement clickAndVerifyPresent(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout);

    WebElement clickAndVerifyPresent(WebElement elToClick, By locatorToVerifyPresent, TimeoutType timeout);

    /**
     * Click a web element (if it's unselected), then verify it is selected (checked for a checkbox, etc.)
     *
     * @return - the WebElement we verified was selected
     */
    WebElement clickAndVerifySelected(By locatorToClick, TimeoutType timeout);

    WebElement clickAndVerifySelected(WebElement elToClick, TimeoutType timeout);

    /**
     * Click a web element, then verify it is selected (checked for a checkbox, etc.)
     *
     * @return - the WebElement we verified was selected
     */
    WebElement clickAndVerifyNotSelected(By locatorToClick, TimeoutType timeout);

    WebElement clickAndVerifyNotSelected(WebElement elToClick, TimeoutType timeout);

    /**
     * Click a web element, then verify another element is present on the DOM (not necessarily visible).
     *
     * @return - the WebElement we verified was present
     */
    WebElement clickAndVerifyVisible(By locatorToClick, By locatorToVerifyVisible, TimeoutType timeout);

    WebElement clickAndVerifyVisible(WebElement elToClick, By locatorToVerifyVisible, TimeoutType timeout);

    /**
     * Click without polling for the element to be clickable or waiting until it's ready.
     * Uses the implicit wait timeout built-in to Selenium.
     *
     * @throws IWebDriverException - if the element isn't clickable when this method is called.
     */
    WebElement clickNoWait(By locator) throws IWebDriverException;

    /**
     * Wait for a javascript confirmation dialog to be present, then dismiss it.
     */
    void dismissAlert(TimeoutType timeout);

    /**
     * @return true if-and-only-if the web element found by the given locator has the CSS class "cssClass"
     */
    boolean doesElementHaveClass(By locator, String locatorClass);

    /**
     * Enter the given text into the input defined by inputCSS, one character at a time.
     * At each step, verify the previous popup was removed from the DOM, and find the new popup.
     * Then see if there is a popup containing the required text on the page. If so, click it and return.
     *
     * @param inputLocator      - locator for the input element
     * @param text              - text you are entering into the input element
     * @param popoverLocator    - locator for the popup element containing required text, or list element if there multiple
     * @param requiredPopupText - text required to be present in popup element defined by popupItemCss
     */
    void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, String text, By popoverLocator,
                                                     String requiredPopupText);

    /**
     * Same as above, but enter minChars characters before checking for the popup to exist.
     */
    void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, int minChars, String text, By popoverLocator,
                                                     String requiredPopupText);

    /**
     * Execute the given javascript synchronously and return the result.
     *
     * @return - see Selenium docs for what can be returned here, probably just Integer, Boolean, Double, or null
     */
    Object executeJavascript(String script);

    /**
     * Immediately return true or false as to whether a web element exists on the page.
     */
    boolean exists(By locator);

    boolean exists(By locator, WebElement parentEl);

    /**
     * Find the first element located by 'parentLocator' that has at least 1 child element located by the relative locator 'childLocator'
     *
     * @param parentLocator - locator to find parent elements
     * @param childLocator  - relative locator to find child elements inside a parent element
     * @return - parent element that have at least 1 child element located by 'childLocator', or null if there are none.
     */
    @Nullable
    WebElement findElementContainingChild(final By parentLocator, final By childLocator);

    /**
     * Search for a WebElement located by 'locator' that has a child element located in its sub-tree
     * located by 'childLocator'.
     * <p>
     * Poll repeatedly until a timeout occurs, but do not refresh the page.
     *
     * @param parentLocator - parent locator
     * @param childLocator  - a locator relative to the parent to find the child element
     * @return - the parent element located by the 'locator' param
     */
    @Nonnull
    WebElement findElementContainingChildWithWait(final By parentLocator, final By childLocator, TimeoutType timeout);

    @Nullable
    WebElement findElementContainingText(By locator, String text);

    /**
     * @param caseSensitive whether to require that the element contains this exact text, case-sensitively, or not.
     * @return
     */
    @Nullable
    WebElement findElementContainingText(By locator, String text, boolean caseSensitive);

    /**
     * @throws TimeoutException if no such element is found before timeout is reached.
     */
    @Nonnull
    WebElement findElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout);

    /**
     * @param caseSensitive whether to require that the element contains this exact text, case-sensitively, or not.
     * @throws TimeoutException if no such element is found before timeout is reached.
     */
    @Nonnull
    WebElement findElementContainingTextWithRefresh(final By locator, final String text, boolean caseSensitive, TimeoutType timeout);

    /**
     * @param caseSensitive whether to require that the element contains this exact text, case-sensitively, or not.
     * @throws TimeoutException if no such element is found before timeout is reached.
     */
    @Nonnull
    WebElement findElementContainingTextWithWait(final By locator, final String text, boolean caseSensitive, TimeoutType timeout);

    /**
     * @throws TimeoutException if no such element is found before timeout is reached.
     */
    @Nonnull
    WebElement findElementContainingTextWithWait(By locator, String text, TimeoutType timeout);

    /**
     * @throws TimeoutException if no such element is found before timeout is reached.
     */
    @Nonnull
    WebElement findElementWithRefresh(By locator, TimeoutType timeout);

    /**
     * Find elements located by 'parentLocator' that have child elements located by the relative locator 'childLocator'
     *
     * @param parentLocator - locator to find parent elements
     * @param childLocator  - relative locator to find child elements inside a parent element
     * @return - parent elements that have at least 1 child element located by 'childLocator'
     */
    @Nonnull
    List<WebElement> findElementsContainingChild(final By parentLocator, final By childLocator);

    /**
     * Waits until finding at least one element located by "childLocator" inside an element located by "parentLocator"
     *
     * @throws TimeoutException if no such parent-child pairs are found before timeout is reached.
     */
    @Nonnull
    List<WebElement> findElementsContainingChildWithWait(final By parentLocator, final By childLocator, TimeoutType timeout);

    @Nullable
    WebElement findVisibleElement(By locator);

    @Nullable
    WebElement findVisibleElementContainingText(By locator, String text);

    @Nullable
    WebElement findVisibleElementContainingText(By locator, String text, boolean caseSensitive);

    /**
     * @return a visible element matching the By given
     * @throws TimeoutException if a matching visible element isn't found after the timeout.
     */
    @Nonnull
    WebElement findVisibleElementWithRefresh(By locator, TimeoutType timeout);

    /**
     * @param text the text that much be present in the element. You can pass in null or the empty string to ignore this value.
     * @return a visible element matching the By given
     * @throws TimeoutException if a matching visible element (containing the given text) isn't found after the timeout.
     */
    @Nonnull
    WebElement findVisibleElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout);

    /**
     * @return a visible element matching the By given
     * @throws TimeoutException if a matching visible element isn't found after the timeout.
     */
    @Nonnull
    WebElement findVisibleElementWithWait(final By locator, TimeoutType timeout);

    /**
     * @param text the text that much be present in the element. You can pass in null or the empty string to ignore this value.
     * @return a visible element matching the By given
     * @throws TimeoutException if a matching visible element (containing the given text) isn't found after the timeout.
     */
    @Nonnull
    WebElement findVisibleElementContainingTextWithWait(final By locator, final String text, TimeoutType timeout);

    @Nonnull
    List<WebElement> findVisibleElements(By locator);

    @Nonnull
    List<WebElement> findVisibleElementsContainingText(By locator, String text);

    @Nonnull
    List<WebElement> findVisibleElementsContainingText(By locator, String text, boolean caseSensitive);

    /**
     * Get a {@link org.openqa.selenium.interactions.Actions} object--used to build sequences of actions like clicking + dragging
     */
    Actions getActionsBuilder();

    <B extends Browser> B getBrowser();

    void setBrowser(Browser browser);

    @Nullable
    WebElement getChildElement(By locator, WebElement parentEl);

    @Nonnull
    WebElement getChildElementWithWait(By locator, WebElement parentEl);

    @Nonnull
    List<WebElement> getChildElements(By locator, WebElement parentEl);

    /**
     * Get the current URL that the browser has open.
     * I'm not sure what Appium WebDrivers return for mobile testing (?)
     */
    String getCurrentURL();

    /**
     * Immediately try to return a WebElement without any implicit or explicit waiting.
     *
     * @return - the WebElement, or null if not present.
     */

    @Nullable
    WebElement getElement(By locator);

    /**
     * Get a WebElement using the implicit wait configured for the Selenium WebDriver.
     *
     * @return the WebElement when found. Null is never returned.
     * @throws RuntimeException - if the web element isn't present after waiting.
     */

    @Nonnull
    WebElement getElementWithWait(By locator);

    List<WebElement> getElements(By locator);

    WebElement getParentElement(WebElement el);

    //////////////////////////////////////Timeouts//////////////////////////////////////////////
    TimeoutsConfig getTimeoutsConfig();

    String getWebPageReadyState() throws Exception;

    WebElement inputText(By locator, String text);

    WebElement inputText(@Nonnull WebElement el, String text);

    WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException;

    WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator, int withRetryCount) throws SeleniumActionsException;

    WebElement inputTextSlowly(By locator, String text);

    WebElement inputTextSlowly(WebElement el, String text);

    WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException;

    WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator, int withRetryCount) throws SeleniumActionsException;

    /**
     * Enter text into the active tiny MCE editor.
     */
    void inputTinyMceText(String text);

    /**
     * Return immediately with an answer as to whether an element is clickable.
     *
     * @return - true if the element is present and clickable, false otherwise.
     */
    boolean isClickable(By locator);

    boolean isClickable(WebElement el);

    boolean isSelected(By locator);

    boolean isSelected(WebElement css);

    /**
     * Return immediately with an answer as to whether an element is visible.
     *
     * @return - true if the element is present and visible, false otherwise.
     * See Selenium's docs for the definition of visible, it has to be on the page, scrolled into view,
     * have a height and width greater than 0, etc.
     */
    boolean isVisible(By locator);

    boolean isVisible(WebElement css);

    <T extends SubPage> T loadSubPage(Class<T> pageClass);

    /**
     * Returns a Page object with initialized WebElements that is a valid page class for the currently open page in the web driver.
     *
     * @param pageClass - a Class representing the type of page to be initialized.
     */
    <T extends TopLevelPage> T loadTopLevelPage(Class<T> pageClass);

    /**
     * Scroll so that the element is in the middle of the page.
     */
    void scrollIntoView(By locator);

    void scrollIntoView(WebElement el);

    /**
     * Scroll the given element with a scroll bar defined by parentCSS so that the web element given by css is in view
     */
    void scrollIntoView(By scrollContainerLocator, By locator);

    void scrollIntoView(By scrollContainerLocator, WebElement el);

    void verifyElementContainsText(By locator, String text, TimeoutType timeout);

    WebElement verifyElementDoesNotHaveClass(final By locator, final String locatorClass, TimeoutType timeout);

    WebElement verifyElementHasClass(By locator, String locatorClass, TimeoutType timeout);

    void verifyElementInvisible(By locator, TimeoutType timeout);

    void verifyElementNotPresented(By locator, TimeoutType timeout);

    WebElement verifyElementNotSelected(By locator, TimeoutType timeout);

    WebElement verifyElementNotSelected(WebElement el, TimeoutType timeout);

    WebElement verifyElementPresented(By locator, TimeoutType timeout);

    /**
     * Verify the given WebElement becomes stale (removed from the DOM).
     *
     * @param element - element we expect to be removed from the DOM
     * @param timeout - timeout type, defaults to the webElementPresenceTimeout (typically 5 seconds).
     */
    void verifyElementRemoved(WebElement element, TimeoutType timeout);

    WebElement verifyElementSelected(By locator, TimeoutType timeout);

    WebElement verifyElementSelected(WebElement el, TimeoutType timeout);

    /**
     * Verify that the element found with the given By is visible, then returns this element.
     * <p>
     * This method assumes that the given By uniquely defines an element. It may act unexpectedly if
     * there are multiple elements on the page matching the By, some visible and some invisible.
     *
     * @return - the visible, matching web element
     * @throws TimeoutException if the element is not found
     * @see #verifyAnyElementVisible
     */
    WebElement verifyElementVisible(By locator, TimeoutType timeout);

    /**
     * Verifies that there exists any element on the page that matches the given By, and is visible.
     * Returns the first such element found.
     * <p>
     * This differs from "verifyElementVisible", because that method uses the Selenium "ExpectedConditions.visibilityOfElementLocatedBy()", which
     * requires that the *first* element matching the given By is visible.
     *
     * @return - the found WebElement
     * @see #verifyElementVisible
     */
    WebElement verifyAnyElementVisible(By locator, TimeoutType timeout);

    void verifyElementWithTextIsInvisible(By locator, String text, TimeoutType timeout);

    void verifyElementWithTextNotPresented(By locator, String text, TimeoutType timeout);

    WebElement verifyPageRefreshed(WebElement elementFromBeforeRefresh, By locatorAfterRefresh, TimeoutType timeout);

    /**
     * Wait for the given symbol to be defined AND non-null in javascript
     */
    void waitForJavascriptSymbolToBeDefined(String symbol, TimeoutType timeout);

    void waitForJavascriptSymbolToHaveValue(String symbol, String value, TimeoutType timeout);

    /**
     * Wait for the HTML of a page to be stable, by verifying the length of the HTML doesn't change for a second.
     *
     * @see com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad#getLengthCheckingWait(org.openqa.selenium.WebDriver)
     * <p>
     * This will probably will only be useful for ordinary WebBrowsers (not mobile) at the moment.
     */
    void waitForPageToBeStable(TimeoutType timeout);

    /**
     * Wait for tinyMCE.activeEditor.initialized to be true, see Tiny MCE documentation online for why.
     */
    void waitForTinyMceToBeReady();

    void waitForWebPageReadyStateToBeComplete();

    <T> T waitOnExpectedCondition(ExpectedCondition<T> expectedCondition, String message, TimeoutType timeout);

    /**
     * Method to simplify general waiting code in Pages and Keywords. Takes a function and waits until the return value is non-null.
     */
    <T, V> V waitOnFunction(Function<T, V> function, T input, String message, TimeoutType timeout);

    /**
     * Wait on a Predicate on a TopLevelPage class until it returns true.
     * Each iteration, the page is refreshed, then the new TopLevelPage object is passed in to the Predicate.apply() method.
     *
     * @return - the last TopLevelPage object after the Predicate returned true.
     */
    <T extends TopLevelPage> T waitOnPagePredicateWithRefresh(Predicate<T> predicate, Class<T> pageClass, String message, TimeoutType timeout);

    /* Method to simplify general waiting code in Pages and Keywords. Takes a predicate and waits until it returns true.*/
    <T> void waitOnPredicate(Predicate<T> predicate, T input, String message, TimeoutType timeout);

    /* Same, but a helper for predicates that don't require an Input, because they use closure to interact with a containing class. */
    void waitOnPredicate(Predicate<Object> predicate, String message, TimeoutType timeout);

    /* Same, but refresh the page after each time the predicate is checked. */
    <T> void waitOnPredicateWithRefresh(Predicate<T> predicate, T input, String message, TimeoutType timeout);

    /* Same, but no input is required. */
    void waitOnPredicateWithRefresh(Predicate<Object> predicate, String message, TimeoutType timeout);

    WebElement waitUntilClickable(By locator, TimeoutType timeout);

    WebElement waitUntilClickable(WebElement el, TimeoutType timeout);
}
