package com.sayem.appium.pagefactory.framework.actions;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.sayem.appium.pagefactory.framework.browser.Browser;
import com.sayem.appium.pagefactory.framework.config.TimeoutType;
import com.sayem.appium.pagefactory.framework.config.TimeoutsConfig;
import com.sayem.appium.pagefactory.framework.exception.*;
import com.sayem.appium.pagefactory.framework.exception.WebDriverException;
import com.sayem.appium.pagefactory.framework.pages.BaseTopLevelPage;
import com.sayem.appium.pagefactory.framework.pages.SubPage;
import com.sayem.appium.pagefactory.framework.pages.TopLevelPage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * Default implementations of Selenium actions that aren't browser-specific.
 */
public abstract class BaseSeleniumActions <B extends Browser> implements SeleniumActions{
    private static final long DEFAULT_POLL_MILLIS = 100;

    protected B browser;
    protected final TimeoutsConfig timeoutsConfig;
    protected static Logger logger = LoggerFactory.getLogger(BaseSeleniumActions.class);

    public BaseSeleniumActions(B browser) {
        this.browser = Preconditions.checkNotNull(browser, "Error: you must supply a non-null Browser to BaseSeleniumActions!");
        this.timeoutsConfig = Preconditions.checkNotNull(browser.getTimeouts(), "Error: you must supply a non-null Timeouts to BaseSeleniumActions!");
    }

    //Convenience method to reduce typing
    protected WebDriver webDriver() {
        return browser.getWebDriver();
    }

/**
 * Return the {@link com.sayem.appium.pagefactory.framework.browser.Browser} object this actions class is tied to.
 */
    @Override
    public B getBrowser() {
        return browser;
    }

    public void setBrowser(Browser browser) {
        this.browser = (B)browser;
    }

    @Override
    public Actions getActionsBuilder() {
        return new Actions(webDriver());
    }

    @Override
    public void acceptAlert(TimeoutType timeout) {
        waitOnExpectedCondition(ExpectedConditions.alertIsPresent(),
                "Waiting for javascript alert to be present before accepting alert.", timeout);
        webDriver().switchTo().alert().accept();
    }

    @Override
    public void dismissAlert(TimeoutType timeout) {
        waitOnExpectedCondition(ExpectedConditions.alertIsPresent(),
                "Waiting for javascript alert to be present before dismissing alert.", timeout);
        webDriver().switchTo().alert().dismiss();
    }

    @Override
    public WebElement clearText(By locator) {
        WebElement el = verifyElementPresented(locator, TimeoutType.DEFAULT);
        try {
            el.clear();
        } catch (Exception e) {
            throw new RuntimeException(format("Error clearing text from element with Locator '%s': %s",
                    locator.toString(), e.getMessage()), e);
        }
        logger.info("Cleared text from element with Locator '{}'", locator);
        return el;
    }

    @Override
    public WebElement clearText(@Nonnull WebElement el) {
        String tag = el.getTagName();
        try {
            el.clear();
        } catch (Exception e) {
            throw new RuntimeException(format("Error clearing text from element <%s>: %s", tag, e.getMessage()), e);
        }
        logger.info("Cleared text from element <{}>", tag);
        return el;
    }

    @Override
    public WebElement clickNoWait(By locator) throws WebDriverException {
        WebElement el = getElement(locator);
        if (!isClickable(el)) {
            throw new com.sayem.appium.pagefactory.framework.exception.WebDriverException("Element is not clickable: " + locator.toString());
        }
        el.click();
        logger.info("Clicked element with locator '{}', no waiting.", locator);
        return el;
    }

    @Override
    public WebElement click(By locator, TimeoutType timeout) {
        WebElement el = waitUntilClickable(locator, timeout);
        el.click();
        logger.info("Clicked element with locator '{}'", locator);
        return el;
    }

    @Override
    public WebElement click(WebElement el, TimeoutType timeout) {
        waitUntilClickable(el, timeout);
        String tag = el.getTagName();
        el.click();
        logger.info("Clicked element <{}>", tag);
        return el;
    }

    @Override
    public WebElement clickAndVerifyPresent(By locatorToClick, By locatorToVerifyPresent, TimeoutType timeout) {
        click(locatorToClick, timeout);
        logger.info("After click, waiting for '{}' to be present.", locatorToVerifyPresent);
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage =
                format("Failure in clickAndVerifyPresent: element '%s' never became present after %d seconds!",
                        locatorToVerifyPresent, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locatorToVerifyPresent));
    }

    @Override
    public WebElement clickAndVerifyPresent(WebElement el, By locatorToVerifyPresent, TimeoutType timeout) {
        click(el, timeout);
        logger.info("After click, waiting for '{}' to be present.", locatorToVerifyPresent);
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage =
                format("Failure in clickAndVerifyPresent: element '%s' never became present after %d seconds!",
                        locatorToVerifyPresent, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locatorToVerifyPresent));
    }

    @Override
    public WebElement clickAndVerifyVisible(By locatorToClick, By locatorToVerifyVisible, TimeoutType timeout) {
        click(locatorToClick, timeout);
        logger.info("After click, waiting for '{}' to be visible.", locatorToVerifyVisible);
        return verifyElementVisible(locatorToVerifyVisible, timeout);
    }

    @Override
    public WebElement clickAndVerifyVisible(WebElement el, By locatorToVerifyVisible, TimeoutType timeout) {
        click(el, timeout);
        logger.info("After click, waiting for '{}' to be visible.", locatorToVerifyVisible);
        return verifyElementVisible(locatorToVerifyVisible, timeout);
    }

    @Override
    public <T extends TopLevelPage> T clickAndLoadTopLevelPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout) {
        click(locatorToClick, TimeoutType.DEFAULT);
        return loadTopLevelPage(pageClass);
    }

    @Override
    public <T extends TopLevelPage> T clickAndLoadTopLevelPage(WebElement el, Class<T> pageClass, TimeoutType timeout) {
        click(el, TimeoutType.DEFAULT);
        return loadTopLevelPage(pageClass);
    }

    @Override
    public <T extends SubPage> T clickAndLoadSubPage(By locatorToClick, Class<T> pageClass, TimeoutType timeout) {
        click(locatorToClick, TimeoutType.DEFAULT);
        return loadSubPage(pageClass);
    }

    @Override
    public <T extends SubPage> T clickAndLoadSubPage(WebElement el, Class<T> pageClass, TimeoutType timeout) {
        click(el, TimeoutType.DEFAULT);
        return loadSubPage(pageClass);
    }

    @Override
    public void clickAndVerifyNotPresent(By locatorToClick, By locatorToVerifyNotPresent, TimeoutType timeout) {
        click(locatorToClick, timeout);
        logger.info("After click, waiting for '{}' to NOT be present.", locatorToVerifyNotPresent);
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in clickAndVerifyNotPresent: element '%s' never became removed from the DOM after %d seconds!",
                locatorToVerifyNotPresent, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.not(
                ExpectedConditions.presenceOfAllElementsLocatedBy(locatorToVerifyNotPresent)));
    }

    @Override
    public void clickAndVerifyNotPresent(WebElement el, By locatorToVerifyNotPresent, TimeoutType timeout) {
        click(el, timeout);
        logger.info("After click, waiting for '{}' to NOT be present.", locatorToVerifyNotPresent);
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in clickAndVerifyNotPresent: element '%s' never became removed from the DOM after %d seconds!",
                locatorToVerifyNotPresent, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.not(
                ExpectedConditions.presenceOfAllElementsLocatedBy(locatorToVerifyNotPresent)));
    }

    @Override
    public void clickAndVerifyNotVisible(By locatorToClick, By locatorToVerifyNotVisible, TimeoutType timeout) {
        click(locatorToClick, timeout);
        logger.info("After click, waiting for '{}' to NOT be visible.", locatorToVerifyNotVisible);
        verifyElementInvisible(locatorToVerifyNotVisible, timeout);
    }

    @Override
    public void clickAndVerifyNotVisible(WebElement el, By locatorToVerifyNotVisible, TimeoutType timeout) {
        click(el, timeout);
        logger.info("After click, waiting for '{}' to NOT be visible.", locatorToVerifyNotVisible);
        verifyElementInvisible(locatorToVerifyNotVisible, timeout);
    }

    @Override
    public void clickAndSelectFromList(By locatorToClick, By popoverLocator) {
        invokeMenuItemAndSelect(getElement(locatorToClick), popoverLocator);
    }

    @Override
    public void clickAndSelectFromList(WebElement clickable, By popoverLocator) {
        invokeMenuItemAndSelect(clickable, popoverLocator);
    }
    @Override
    public Object executeJavascript(String script) {
        logger.trace("Executing javascript: '{}'", script);
        try {
            return ((JavascriptExecutor) webDriver()).executeScript(script);
        } catch (Exception e) {
            throw new RuntimeException(format("Exception executing Javascript '%s':", script), e);
        }
    }
    @Override
    public void waitForJavascriptSymbolToBeDefined(final String symbol, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getPageLoadTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds, DEFAULT_POLL_MILLIS); //Check every 100ms
        wait.ignoring(StaleElementReferenceException.class);
        try {
            wait.until(new ExpectedCondition<Object>() {
                @Nullable
                @Override
                public Object apply(@Nullable WebDriver input) {
                    Object jsResult = executeJavascript(format("return (typeof %s != 'undefined') && (%s != null)", symbol, symbol));
                    logger.trace("javascript result: " + jsResult);
                    return jsResult;
                }
            });
        } catch (TimeoutException e) {
            throw new RuntimeException(
                    format("Timeout waiting for javascript symbol '%s' to be defined with %d seconds timeout used", symbol, waitSeconds), e);
        }
        logger.info("Success verifying javascript symbol '{}' is defined!", symbol);
    }

    @Override
    public void waitForJavascriptSymbolToHaveValue(final String symbol, final String value, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getPageLoadTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds, DEFAULT_POLL_MILLIS); //Check every 100ms
        wait.ignoring(StaleElementReferenceException.class);
        try {
            wait.until(new ExpectedCondition<Object>() {
                @Nullable
                @Override
                public Object apply(@Nullable WebDriver input) {
                    Object jsResult = executeJavascript(format("return (%s) === (%s)", symbol, value));
                    logger.trace("javascript result: " + jsResult);
                    return jsResult;
                }
            });
        } catch (TimeoutException e) {
            throw new RuntimeException(
                    format("Timeout waiting for javascript symbol '%s' to have value '%s' with %d seconds timeout used", symbol, value, waitSeconds), e);
        }
        logger.info("Success verifying javascript symbol '{}' has value '{}'!", symbol, value);
    }


    /**
     * According to Selenium Javadoc, this is the correct way to check for existence of an element.
     */
    @Override
    public boolean exists(By locator) {
        List<WebElement> elements = findElements(locator, null);
        return elements.size() > 0;
    }

    @Override
    public boolean exists(By locator, WebElement parentEl) {
        List<WebElement> elements = findElements(locator, parentEl);
        return elements.size() > 0;
    }

    @Override
    public WebElement findElementContainingText(By locator, String text) {
        List<WebElement> matches = findElements(locator, null);
        for (WebElement el : matches) {
            try {
                if (el.getText().contains(text)) {
                    logger.info("SUCCESS: Found web element containing text '{}' with locator '{}'", text, locator);
                    return el;
                }
            } catch (Exception e) { //Don't fail just because one web element was stale. Continue searching for the text.
                logger.debug("Exception while searching for web elements containing text '{}' with locator '{}'", text, locator);
                logger.debug(Throwables.getStackTraceAsString(e));
            }
        }
        return null;
    }

    @Override
    public WebElement findVisibleElementContainingText(By locator, String text) {
        List<WebElement> matches = findElements(locator, null);
        for (WebElement el : matches) {
            try {
                if (el.getText().contains(text) && el.isDisplayed()) {
                    logger.info("SUCCESS: Found visible web element containing text '{}' with locator '{}'", text, locator);
                    return el;
                }
            } catch (Exception e) { //Don't fail just because one web element was stale. Continue searching for the text.
                logger.debug("Exception while searching for web elements containing text '{}' with locator '{}'", text, locator);
                logger.debug(Throwables.getStackTraceAsString(e));
            }
        }
        return null;
    }

    @Override
    public WebElement findVisibleElementContainingTextWithWait(final By locator, final String text, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.ignoring(StaleElementReferenceException.class);

        return wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(@Nullable WebDriver input) {
                WebElement el = findVisibleElementContainingText(locator, text);
                if (el == null) {
                    GeneralUtils.waitOneSecond();
                }
                return el;
            }
        });
    }

    @Override
    public WebElement findElementWithRefresh(final By locator, TimeoutType timeout) {
        return findElementContainingTextWithRefresh(locator, "", timeout);
    }

    @Override
    public WebElement findElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getPollingWithRefreshTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.ignoring(StaleElementReferenceException.class);

        logger.info("Waiting for element containing text '{}' defined by locator '{}', timeout of {} seconds", new Object[]{text, locator, waitSeconds});
        try {
            WebElement found = wait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(@Nullable WebDriver input) {
                    long start = new Date().getTime();
                    while ((new Date().getTime() - start) / 1000 < timeoutsConfig.getPauseBetweenRefreshSeconds()) {
                        WebElement el = findElementContainingText(locator, text);
                        if (el != null) {
                            return el;
                        }
                        GeneralUtils.waitMillis(timeoutsConfig.getPauseBetweenTriesMillis());
                    }
                    getBrowser().refreshPage();
                    return null;
                }
            });
            logger.info("Success finding element containing text '{}' defined by locator '{}'!", text, locator);
            return found;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting to find text '{}' in an element matching locator '{}'", text, locator);
            throw new TimeoutException(
                    format("Timeout waiting to find text '%s' in an element matching locator '%s'", text, locator));
        }
    }

    @Override
    public WebElement findVisibleElementWithRefresh(final By locator, TimeoutType timeout) {
        return findVisibleElementContainingTextWithRefresh(locator, "", timeout);
    }

    @Override
    public WebElement findVisibleElementContainingTextWithRefresh(final By locator, final String text, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getPollingWithRefreshTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.ignoring(StaleElementReferenceException.class);

        logger.info("Waiting for element containing text '{}' defined by locator '{}', timeout of {} seconds", new Object[]{text, locator, waitSeconds});
        try {
            WebElement found = wait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(@Nullable WebDriver input) {
                    long start = new Date().getTime();
                    while ((new Date().getTime() - start) / 1000 < timeoutsConfig.getPauseBetweenRefreshSeconds()) {
                        WebElement el = findVisibleElementContainingText(locator, text);
                        if (el != null) {
                            return el;
                        }
                        GeneralUtils.waitMillis(timeoutsConfig.getPauseBetweenTriesMillis());
                    }
                    getBrowser().refreshPage();
                    return null;
                }
            });
            logger.info("Success finding element containing text '{}' defined by locator '{}'!", text, locator);
            return found;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting to find text '{}' in an element matching locator '{}'", text, locator);
            throw new TimeoutException(
                    format("Timeout waiting to find text '%s' in an element matching locator '%s'", text, locator));
        }
    }

    @Override
    public WebElement findElementContainingTextWithWait(final By locator, final String text, TimeoutType timeout) {
        final int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String msg = format("Failure in findElementContainingTextWithWait: never found text '%s' in element " +
                                  "with locator '%s' with timeout of %d seconds", text, locator, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.ignoring(StaleElementReferenceException.class)
            .withMessage(msg);

        return wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(@Nullable WebDriver input) {
                WebElement el = findElementContainingText(locator, text);
                if (el == null) {
                    GeneralUtils.waitOneSecond();
                }
                return el;
            }
        });
    }

    @Override
    @Nullable
    public WebElement getChildElement(By locator, WebElement parentEl) {
        List<WebElement> elements = findElements(locator, parentEl);
        if (elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    @Nullable
    public WebElement getElement(By locator) {
        List<WebElement> elements = findElements(locator, null);
        if (elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    @Nonnull
    public List<WebElement> getChildElements(By locator, WebElement parentEl) {
        return findElements(locator, parentEl);
    }

    @Override
    @Nonnull
    public List<WebElement> getElements(By locator) {
        return findElements(locator, null);
    }

    @Override
    @Nonnull
    public WebElement getElementWithWait(By locator) {
        return getChildElementWithWait(locator, null);
    }


    @Override
    @Nonnull
    public WebElement getChildElementWithWait(By locator, WebElement parentEl) {
        try {
            WebElement el = findElement(locator, parentEl);
            logger.trace("Successfully found web element by locator '{}'", locator);
            return el;
        } catch (NoSuchElementException e) {
            long implicitWait = browser.getImplicitWaitTimeoutMillis();
            throw new RuntimeException(
                    format("Timeout using implicit wait of %d ms waiting to find web element with locator '%s' ", implicitWait, locator));
        }
    }

    @Override
    @Nonnull
    public WebElement getParentElement(WebElement el) {
        return el.findElement(By.xpath(".."));
    }

    @Override
    @Nonnull
    public WebElement inputText(By locator, String text) {
        logger.info("Inputting text '{}' into element with locator '{}'", text, locator);
        WebElement el = getElementWithWait(locator);
        try {
            el.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException(format("Error inputting text '%s' into element with locator '%s': %s", text, locator, e.getMessage()), e);
        }
        return el;
    }

    @Override
    @Nonnull
    public WebElement inputText(@Nonnull WebElement el, String text) {
        logger.info("Inputting text '{}' into web element <{}>", text, el.getTagName());
        try {
            el.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException(format("Error inputting text '%s' into element <%s>: %s", text, el.getTagName(), e.getMessage()), e);
        }
        return el;
    }

    @Override
    public WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException {
        return inputTextAndSelectFromList(inputField, value, popoverLocator, 0);         // default is no retries
    }

    @Override
    public WebElement inputTextAndSelectFromList(WebElement inputField, String value, By popoverLocator,
                                                 int withRetryCount) throws SeleniumActionsException {
        return enterTextAndSelectFromList(inputField, value, popoverLocator, withRetryCount, false);
    }

    @Override
    public WebElement inputTextSlowly(By locator, String text) {
        WebElement el = getElementWithWait(locator);
        logger.info("Inputting text '{}' into web element with locator '{}'", text, locator);
        return inputTextSlowly(el, text);
    }

    @Override
    public WebElement inputTextSlowly(@Nonnull WebElement el, String text) {
        logger.info("Inputting text {} slowly into web element {}", text, el.getTagName());
        for (Character c : text.toCharArray()) {
            el.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(timeoutsConfig.getPauseBetweenKeysMillis());
            } catch (InterruptedException e) {
                // don't care
            }
        }
        return el;
    }

    @Override
    public WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator) throws SeleniumActionsException {
        return inputTextSlowlyAndSelectFromList(inputField, value, popoverLocator, 0);      // default is no retries
    }

    @Override
    public WebElement inputTextSlowlyAndSelectFromList(WebElement inputField, String value, By popoverLocator,
                                                       int withRetryCount) throws SeleniumActionsException {
        return enterTextAndSelectFromList(inputField, value, popoverLocator, withRetryCount, true);
    }

    @Override
    public void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, String text, By popoverLocator,
                                                            String requiredPopupText) {
        enterTextForAutoCompleteAndSelectFirstMatch(inputLocator, 0, text, popoverLocator, requiredPopupText);
    }

    @Override
    public void enterTextForAutoCompleteAndSelectFirstMatch(By inputLocator, int minChars, String text, By popoverLocator,
                                                            String requiredPopupText) {
        if (minChars > text.length()) {
            throw new RuntimeException(format("Minimum characters to enter (%d) is greater than the length of the input text '%s'!", minChars, text));
        }
        scrollIntoView(inputLocator);
        if (minChars > 0) {
            inputText(inputLocator, text.substring(0, minChars));
        }
        for (int i = minChars; i < text.length(); i++) {
            String oneChar = String.valueOf(text.charAt(i));
            inputText(inputLocator, oneChar);

            // If the last char is being entered, wait 5 full seconds for the expected popup. Otherwise, wait 1 second.
            TimeoutType timeout = (i == text.length() - 1) ? TimeoutType.FIVE_SECONDS : TimeoutType.ONE_SECOND;
            try {
                WebElement matchingPopup = findElementContainingTextWithWait(popoverLocator, requiredPopupText, timeout);
                if (matchingPopup != null) {
                    try {
                        getActionsBuilder().moveToElement(matchingPopup)
                                .pause(500) // Sometimes javascript needs a moment to register that it's being hovered.
                                .click()
                                .perform();
                        logger.info("Success - clicked popup for autocomplete text \"{}\"", text);
                        return;
                    } catch (Exception e) {
                        logger.debug("Exception clicking popup from autocomplete.", e);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new RuntimeException(format("No popup defined by Locator  '%s' found with required text '%s'", popoverLocator, requiredPopupText));
    }

    @Override
    public void inputTinyMceText(String text) {
        waitForTinyMceToBeReady();
        ((JavascriptExecutor) webDriver()).executeScript(format("tinyMCE.activeEditor.setContent(\"%s\")", text));
    }

    @Override
    public void waitForTinyMceToBeReady() {
        waitForJavascriptSymbolToBeDefined("tinyMCE", TimeoutType.DEFAULT);
        waitForJavascriptSymbolToBeDefined("tinyMCE.activeEditor", TimeoutType.DEFAULT);
        waitForJavascriptSymbolToHaveValue("tinyMCE.activeEditor.initialized", "true", TimeoutType.DEFAULT);
    }

    @Override
    public boolean isClickable(By locator) {
        WebElement el = getElement(locator);
        if (el == null) {
            return false;
        }
        return isClickable(el);
    }

    /**
     * Conditions according to selenium Javadoc for an element to be clickable
     */
    @Override
    public boolean isClickable(WebElement el) {
        if (el == null) {
            return false;
        }
        try {
            if (!el.isDisplayed()) { //If not visible, element isn't clickable
                return false;
            }
            if (el.getSize().getHeight() <= 0 || el.getSize().getWidth() <= 0) { // If width or height is 0, element is not clickable
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isVisible(By locator) {
        WebElement el = getElement(locator);
        return isVisible(el);
    }

    @Override
    public boolean isVisible(WebElement el) {
        if (el == null) {
            return false;
        }
        return el.isDisplayed() && el.getSize().getHeight() > 0 && el.getSize().getWidth() > 0;
    }

    @Override
    public void scrollIntoView(By locator) {
        WebElement el = verifyElementPresented(locator, TimeoutType.DEFAULT);
        scrollIntoView(el);
    }

    @Override
    public void scrollIntoView(WebElement el) {
        int scrollHeight = webDriver().manage().window().getSize().getHeight();
        int y = Math.max(0, el.getLocation().getY() - scrollHeight / 2); //Subtract half the window height so its in the middle of the viewable area.
        executeJavascript(format("window.scrollTo(%d, %d)", 0, y));
    }

    @Override
    public void scrollIntoView(By scrollContainerLocator, By locator) {
        WebElement parent = verifyElementPresented(scrollContainerLocator, TimeoutType.DEFAULT);
        WebElement el = verifyElementPresented(locator, TimeoutType.DEFAULT);
        int currentScrollTop = ((Long) executeJavascript(format("return $('%s').scrollTop()", scrollContainerLocator))).intValue();
        int y = el.getLocation().getY();
        int parentY = parent.getLocation().getY();
        int scrollTo = Math.max(0, y - parentY + currentScrollTop);
        executeJavascript(format("$('%s').scrollTop(%d)", scrollContainerLocator, scrollTo));
    }

    @Override
    public void scrollIntoView(By scrollContainerLocator, WebElement el) {
        WebElement parent = verifyElementPresented(scrollContainerLocator, TimeoutType.DEFAULT);
        int currentScrollTop = ((Long) executeJavascript(format("return $('%s').scrollTop()", scrollContainerLocator))).intValue();
        int y = el.getLocation().getY();
        int parentY = parent.getLocation().getY();
        int scrollTo = Math.max(0, y - parentY + currentScrollTop);
        executeJavascript(format("$('%s').scrollTop(%d)", scrollContainerLocator, scrollTo));
    }

    @Override
    public <T extends TopLevelPage> T loadTopLevelPage(Class<T> pageClass) {
        return (T) browser.loadTopLevelPage(pageClass);
    }

    @Override
    public <T extends SubPage> T loadSubPage(Class<T> pageClass) {
        return (T)browser.loadSubPage(pageClass);
    }

    @Override
    public void verifyElementContainsText(final By locator, final String text, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in verifyElementContainsText: an element with Locator '%s' was never found containing text '%s'!",
                locator, text);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        logger.info("SUCCESS: Verified element with Locator '{}' contains text '{}'", locator, text);
    }

    @Override
    public WebElement verifyElementPresented(By locator, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage =
                format("Failure in verifyElementPresented: element '%s' never became presented after %d seconds!",
                        locator.toString(), waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage).ignoring(StaleElementReferenceException.class);
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        logger.trace("SUCCESS: Verified element with Locator '{}' is present", locator.toString());
        return el;
    }

    @Override
    public void verifyElementNotPresented(By locator, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in verifyElementNotPresented: element '%s' never became not presented after %d seconds!",
                locator, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        logger.trace("SUCCESS: Verified element with locator '{}' is NOT present", locator);
    }

    @Override
    public void verifyElementWithTextNotPresented(By locator, String text, TimeoutType timeout) {
        try {
            findElementContainingTextWithWait(locator, text, timeout);
            throw new RuntimeException(
                    format("Error in verifyElementWithTextNotPresented: found element with locator '%s' containing text '%s'!", locator, text));
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void verifyElementSelected(By locator, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in verifyElementSelected: Element '%s' never became selected after %d seconds!",
                locator, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.elementToBeSelected(locator));
    }

    @Override
    public void verifyElementSelected(WebElement el, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in verifyElementSelected: Element '%s' never became selected after %d seconds!",
                el.getTagName(), waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage);
        wait.until(ExpectedConditions.elementToBeSelected(el));
        logger.info("SUCCESS: Verified element <{}> is selected", el.getTagName());
    }

    @Override
    public void verifyElementNotSelected(By locator, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        final String errorMessage = format("Failure in verifyElementNotSelected: Element '%s' never became deselected after %d seconds!",
                locator, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.elementSelectionStateToBe(locator, false));
        logger.info("SUCCESS: Verified element with locator '{}' is NOT selected", locator);
    }

    @Override
    public void verifyElementNotSelected(WebElement el, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.until(ExpectedConditions.elementSelectionStateToBe(el, false));
        logger.info("SUCCESS: Verified element <{}> is NOT selected", el.getTagName());
    }

    @Override
    public WebElement verifyElementVisible(final By locator, TimeoutType timeout) {
        final String errorMessage = format("Error in verifyElementVisible: element with locator '%s' never became visible", locator);
        return waitOnExpectedCondition(ExpectedConditions.visibilityOfElementLocated(locator), errorMessage, timeout);
    }

    @Override
    public void verifyElementInvisible(By locator, TimeoutType timeout) {
        waitOnExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(locator),
                format("Failure in verifyElementInvisible waiting for element with locator '%s' to be invisible", locator), timeout);
    }

    @Override
    public void verifyElementWithTextIsInvisible(By locator, String text, TimeoutType timeout) {
        try {
            WebElement visibleEl = findVisibleElementContainingTextWithWait(locator, text, timeout);
            throw new RuntimeException(
                    format("Error in verifyElementWithTextIsInvisible: found element by locator '%s' containing text '%s'", locator, text));
        } catch (Exception e) {
            return; // OK - we didn't find a visible element containing the given text
        }
    }

    @Override
    public WebElement verifyPageRefreshed(WebElement elementFromBeforeRefresh, By locatorAfterRefresh, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getPageRefreshTimeoutSeconds(), timeout);
        logger.info("Waiting for locator '{}' to be present after page refreshes, using timeout of {} seconds", locatorAfterRefresh, waitSeconds);
        waitOnExpectedConditionForSeconds(ExpectedConditions.stalenessOf(elementFromBeforeRefresh),
                "Timeout waiting for web element to become stale (waiting for page to reload).",
                waitSeconds);
        logger.info("Verified web element became stale (page is reloading).");
        WebElement el = verifyElementPresented(locatorAfterRefresh, TimeoutType.DEFAULT);
        logger.info("Successfully verified page refreshed by finding web element with locator '{}'.", locatorAfterRefresh);

        return el;
    }

    @Override
    public <T, V> V waitOnFunction(Function<T, V> function, T input, String message, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getMediumTimeoutSeconds(), timeout);
        FluentWait<T> fluentWait = new FluentWait<T>(input)
                .withTimeout(waitSeconds, TimeUnit.SECONDS)
                .pollingEvery(DEFAULT_POLL_MILLIS, TimeUnit.MILLISECONDS)
                .withMessage(message)
                .ignoring(NotFoundException.class)
                .ignoring(StaleElementReferenceException.class);
        return fluentWait.until(function);
    }

    @Override
    public <T> void waitOnPredicate(Predicate<T> predicate, T input, String message, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getMediumTimeoutSeconds(), timeout);
        FluentWait<T> fluentWait = new FluentWait<T>(input)
                .withTimeout(waitSeconds, TimeUnit.SECONDS)
                .pollingEvery(DEFAULT_POLL_MILLIS, TimeUnit.MILLISECONDS)
                .withMessage(message)
                .ignoring(NotFoundException.class)
                .ignoring(StaleElementReferenceException.class);
        fluentWait.until(predicate);
    }

    @Override
    public void waitOnPredicate(Predicate predicate, String message, TimeoutType timeout) {
        waitOnPredicate(predicate, new Object(), message, timeout);
    }

    @Override
    public <T> void waitOnPredicateWithRefresh(final Predicate<T> predicate, final T input, String message, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getMediumTimeoutSeconds(), timeout);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds, DEFAULT_POLL_MILLIS);
        wait.withMessage(message)
            .ignoring(StaleElementReferenceException.class);

        logger.info("Waiting on expected condition, using timeout of {} seconds", waitSeconds);
        wait.until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(@Nullable WebDriver webDriver) {
                if (predicate.apply(input)) {
                    return true;
                }
                browser.refreshPage(BaseTopLevelPage.class);
                return false;
            }
        });
    }

    @Override
    public void waitOnPredicateWithRefresh(final Predicate predicate, String message, TimeoutType timeout) {
        waitOnPredicateWithRefresh(predicate, new Object(), message, timeout);
    }

    @Override
    public <T> T waitOnExpectedCondition(ExpectedCondition<T> expectedCondition, String message, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getWebElementPresenceTimeoutSeconds(), timeout); //Default of web element presence timeout
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds, DEFAULT_POLL_MILLIS);
        wait.withMessage(message)
            .ignoring(StaleElementReferenceException.class);
        logger.info("Waiting on expected condition, using timeout of {} seconds", waitSeconds);
        return wait.until(expectedCondition);
    }

    private <T> T waitOnExpectedConditionForSeconds(ExpectedCondition<T> expectedCondition, String message, int timeout) {
        WebDriverWait wait = new WebDriverWait(webDriver(), timeout, DEFAULT_POLL_MILLIS);
        wait.withMessage(message)
            .ignoring(StaleElementReferenceException.class);
        logger.info("Waiting on expected condition, using timeout of {} seconds", timeout);
        return wait.until(expectedCondition);
    }

    @Override
    public WebElement waitUntilClickable(By locator, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        final String errorMessage = format("Element '%s' never became clickable after '%d' seconds", locator, waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(errorMessage)
            .ignoring(StaleElementReferenceException.class);
        logger.info("Waiting for locator element '{}' to be clickable, using timeout of {} seconds", locator, waitSeconds);
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        return el;
    }

    @Override
    public WebElement waitUntilClickable(final WebElement el, TimeoutType timeout) {
        int waitSeconds = getTimeout(timeoutsConfig.getClickTimeoutSeconds(), timeout);
        final String message = format("Element never became clickable after '%d' seconds", waitSeconds);
        WebDriverWait wait = new WebDriverWait(webDriver(), waitSeconds);
        wait.withMessage(message)
            .ignoring(StaleElementReferenceException.class);
        wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                if (isClickable(el)) {
                    return el;
                }
                return null;
            }
        });
        return el;
    }

    //////////////////////////////////////Timeouts//////////////////////////////////////////////
    @Override
    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    protected int getTimeout(int defaultTimeout, TimeoutType timeout) {
        return timeout == TimeoutType.DEFAULT ? defaultTimeout : timeoutsConfig.getTimeoutInSeconds(timeout);
    }

    //////////////////////////////////// helpers ////////////////////////////////////////////////

    /**
     * Convenient helper to Find Elements from either top page element or from parent element.
     *
     * @param locator      - Locator defining the input element
     * @param parentEl - Parent web element. If this is provided the search will be from the parent element. If null, search will be from top element
     * @return - List of elements
     */
    protected List<WebElement> findElements(By locator, WebElement parentEl) {
        if (parentEl == null) {
            return webDriver().findElements(locator);
        } else {
            return parentEl.findElements(locator);
        }
    }

    protected WebElement findElement(By locator, WebElement parentEl) {
        if (parentEl == null) {
            return webDriver().findElement(locator);
        } else {
            return parentEl.findElement(locator);
        }
    }

    protected WebElement enterTextAndSelectFromList(WebElement inputField, String value, By popoverLocator,
                                                    int withRetryCount, boolean slowly) throws SeleniumActionsException {
        boolean done = false;
        int initialCount = withRetryCount;

        do {
            try {
                enterTextAndSelectFromList(inputField, value, popoverLocator, slowly);
                done = true;
            } catch (Exception ex) {
                logger.error("Caught an exception " + ex.getMessage());
            }
            withRetryCount--;
        } while (!done && withRetryCount > 0);

        // Need to subtract 1, so that we have 0 retries if we succeeded on the first try.
        int numberOfUsedRetries = initialCount - withRetryCount - 1;
        if (numberOfUsedRetries > 0) {
            logger.warn(done ?
                    format("Entered text successfully and selected locator '%s' from list after %d retries", popoverLocator, numberOfUsedRetries) :
                    format("Failed to enter text and select locator '%s' from list.", popoverLocator));
        }
        if (!done) {
            throw new SeleniumActionsException(format("Failed to inputTextAndSelectFromList after %d retries", numberOfUsedRetries));
        }
        return inputField;
    }

    protected void enterTextAndSelectFromList(WebElement inputField, String value, By popoverLocator, boolean slowly) {
        clearText(inputField);
        if (slowly) {
            inputTextSlowly(inputField, value);
        } else {
            inputText(inputField, value);
        }
        verifyElementPresented(popoverLocator, TimeoutType.DEFAULT);
        click(popoverLocator, TimeoutType.DEFAULT);
    }

    protected void invokeMenuItemAndSelect(WebElement clickable, By popoverLocator) {
        Preconditions.checkNotNull(clickable, "Input WebElement cannot be null");
        waitUntilClickable(clickable, TimeoutType.DEFAULT);
        click(clickable, TimeoutType.DEFAULT);
        verifyElementPresented(popoverLocator, TimeoutType.DEFAULT);
        waitUntilClickable(popoverLocator, TimeoutType.DEFAULT);
        click(popoverLocator, TimeoutType.DEFAULT);
    }

    //**********~~~~~~~~~~~~~ Verify Class Actions ~~~~~~~~~~~~~~~*************
    public boolean doesElementHaveClass(By locator, String locatorClass) {
        WebElement el = verifyElementPresented(locator, TimeoutType.DEFAULT);
        return WebElementHelpers.webElementHasClass(el, locatorClass);
    }

    public WebElement verifyElementHasClass(final By locator, final String locatorClass, TimeoutType timeout) {
        return waitOnFunction(new Function<SeleniumActions, WebElement>() {
                                  @Nullable
                                  @Override
                                  public WebElement apply(SeleniumActions input) {
                                      WebElement el = input.verifyElementPresented(locator, TimeoutType.DEFAULT);
                                      if (WebElementHelpers.webElementHasClass(el, locatorClass)) {
                                          return el;
                                      }
                                      return null;
                                  }
                              }, this,
                format("Waiting for element that matches locator '%s' to have class '%s'", locator, locatorClass),
                timeout);
    }

    public WebElement verifyElementDoesNotHaveClass(final By locator, final String locatorClass, TimeoutType timeout) {
        return waitOnFunction(new Function<SeleniumActions, WebElement>() {
                                  @Nullable
                                  @Override
                                  public WebElement apply(SeleniumActions input) {
                                      WebElement el = input.verifyElementPresented(locator, TimeoutType.DEFAULT);
                                      if (!WebElementHelpers.webElementHasClass(el, locatorClass)) {
                                          return el;
                                      }
                                      return null;
                                  }
                              }, this,
                format("Waiting for element that matches locator '%s' to NOT have class '%s'", locator, locatorClass),
                timeout);
    }

}
