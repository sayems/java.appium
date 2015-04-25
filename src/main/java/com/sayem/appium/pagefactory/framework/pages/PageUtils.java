package com.sayem.appium.pagefactory.framework.pages;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sayem.appium.pagefactory.framework.actions.SeleniumActions;
import com.sayem.appium.pagefactory.framework.config.TimeoutType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Helpers for interacting with Pages and TopLevelPages
 */
public class PageUtils {
    private static final Logger logger = LoggerFactory.getLogger(PageUtils.class);

    /**
     * Use reflection to recursively get all fields annotated with {@link SubPageField} on a given class.
     *
     * @param type
     * @return - List of Fields that are annotated with {@link com.sayem.appium.pagefactory.framework.pages.SubPageField}
     * and are of type {@link com.sayem.appium.pagefactory.framework.pages.SubPage},
     * recursively including fields from super classes.
     */
    public static List<Field> getAllSubpageFields(Class<?> type) {
        List<Field> subpageFields = Lists.newArrayList();
        for (Field field : type.getDeclaredFields()) {
            if (field.getAnnotation(SubPageField.class) != null) {
                if (SubPage.class.isAssignableFrom(field.getType())) {
                    subpageFields.add(field);
                } else {
                    logger.warn("Class {} has a field annotated with @SubPageField that isn't a SubPage type", type.getSimpleName());
                }
            }
        }

        if (type.getSuperclass() != null) {
            subpageFields.addAll(getAllSubpageFields(type.getSuperclass()));
        }

        return subpageFields;
    }

    /**
     * Return an Optional&lt;String&gt; representing the path to a web page for a TopLevelPage class.
     * The path is extracted using reflection from the {@link WebPagePath} annotation, if present.
     *
     * @param pageClass
     * @return - the Optional&lt;String&gt; that is present with a value if the given Page class was annotated with
     * {@link com.sayem.appium.pagefactory.framework.pages.WebPagePath}
     */
    public Optional<String> getWebPagePathForClass(Class<? extends TopLevelPage> pageClass) {
        WebPagePath annotation = pageClass.getAnnotation(WebPagePath.class);
        if (annotation == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(annotation.path());
    }

    /**
     * Default implementation of pageLoadHook().
     * <p>
     * Just verify the page identifier Locator is present on the DOM.
     *
     * @param page
     * @param a
     */
    public void defaultPageLoadHook(Page page, SeleniumActions a) {
        By pageIdentifier = page.getPageIdentifier();
        if (pageIdentifier != null) {
            a.verifyElementPresented(pageIdentifier, TimeoutType.PAGE_LOAD_TIMEOUT);
        }
    }

    public void initSubPages(Page page, SeleniumActions a) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(page);
        List<Field> fields = getAllSubpageFields(page.getClass());
        for (Field field : fields) {
            Class type = field.getType();
            if (!SubPage.class.isAssignableFrom(type)) {
                logger.warn("Invalid @SubPageField in class '%s'. Must be a subclass of SubPage.");
                continue;
            }

            SubPage subPage = PageFactory.initElements(a.getBrowser().getWebDriver(), (Class<? extends SubPage>) field.getType());
            subPage.setActions(a);
            subPage.setParent(page);
            subPage.pageLoadHook();
            subPage.initSubPages();

            //Set the subpage field
            try {
                field.setAccessible(true);
                field.set(page, subPage);
            } catch (IllegalAccessException ex) {
                logger.error("Error instantiating SubPage field: " + field, ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public void initSubPagesWithoutPageLoadHooks(Page page, SeleniumActions a) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(page);
        List<Field> fields = getAllSubpageFields(page.getClass());
        for (Field field : fields) {
            Class type = field.getType();
            if (!SubPage.class.isAssignableFrom(type)) {
                logger.warn("Invalid @SubPageField in class '%s'. Must be a subclass of SubPage.");
                continue;
            }

            SubPage subPage = PageFactory.initElements(a.getBrowser().getWebDriver(), (Class<? extends SubPage>) field.getType());
            subPage.setActions(a);
            subPage.setParent(page);
            initSubPagesWithoutPageLoadHooks(subPage, a);

            //Set the subpage field
            try {
                field.setAccessible(true);
                field.set(page, subPage);
            } catch (IllegalAccessException ex) {
                logger.error("Error instantiating SubPage field: " + field, ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public void runPageLoadHooksForSubPages(Page page, SeleniumActions a) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(page);
        List<Field> fields = getAllSubpageFields(page.getClass());
        for (Field field : fields) {
            Class type = field.getType();
            if (!SubPage.class.isAssignableFrom(type)) {
                logger.warn("Invalid @SubPageField in class '%s'. Must be a subclass of SubPage.");
                continue;
            }

            //Get the subpage field
            SubPage subPage;
            try {
                field.setAccessible(true);
                subPage = (SubPage) field.get(page);
            } catch (IllegalAccessException ex) {
                logger.error("Error instantiating SubPage field: " + field, ex);
                throw new RuntimeException(ex);
            }
            if (subPage != null) {
                subPage.pageLoadHook();
                runPageLoadHooksForSubPages(subPage, a);
            }
        }
    }

    public <T extends Page> T loadPageFromURL(URI absoluteURL, Class<T> pageClass, WebDriver driver, SeleniumActions actions) {
        Preconditions.checkNotNull(absoluteURL, "Error: URI provided cannot be null in PageUtils#loadPageFromURL");
        Preconditions.checkNotNull(pageClass);
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(actions);
        Preconditions.checkArgument(absoluteURL.isAbsolute(), "Error: must provide an absolute URL to PageUtils#loadPageFromURL");
        driver.get(absoluteURL.toString());
        return loadCurrentPage(pageClass, driver, actions);
    }

    public <T extends Page> T loadCurrentPage(Class<T> pageClass, WebDriver driver, SeleniumActions actions) {
        T page = PageFactory.initElements(driver, pageClass);
        page.setActions(actions);
        page.initSubPages();
        page.pageLoadHook();
        return page;
    }

    public <T extends Page> T loadCurrentPageWithoutPageLoadHook(Class<T> pageClass, WebDriver driver, SeleniumActions actions) {
        T page = PageFactory.initElements(driver, pageClass);
        page.setActions(actions);
        initSubPagesWithoutPageLoadHooks(page, actions);
        return page;
    }
}
