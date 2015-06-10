package org.sayem.appium.actions;

import com.google.common.collect.Lists;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Static helper methods that don't require a WebDriver instance belong here.
 */
public class WebElementHelpers {
    /**
     * Determine whether the given WebElement has the given Class
     *
     * @param webElement element to check
     * @param className  to search for
     * @return boolean
     */
    public static boolean webElementHasClass(WebElement webElement, String className) {
        return getClasses(webElement).contains(className);
    }

    /**
     * Get the classes of a given WebElement. Split on whitespace in case it's possible to have multiple spaces in the
     * "class" attribute of an HTML element.
     *
     * @param webElement - the WebElement to return a list of classes for.
     * @return - a List&lt;String&gt; of the classes for the given WebElement
     */
    public static List<String> getClasses(WebElement webElement) {
        if (webElement.getAttribute("class") == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(webElement.getAttribute("class").split("\\s+"));
    }
}
