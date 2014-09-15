package com.sayem.appium.pagefactory.framework.pages;

import com.google.common.base.Optional;
import com.sayem.appium.pagefactory.framework.actions.SeleniumActions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Base abstract class for a TopLevelPage. Implements the default pageLoadHook that waits for the page identifier to be present.
 * <p/>
 * Subclasses should call super.pageLoadHook() if they want to wait on the page identifier.
 */
public class BaseTopLevelPage implements TopLevelPage {
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(BaseTopLevelPage.class);

    private static final PageUtils PAGE_UTILS = new PageUtils();
    protected SeleniumActions a;

    public final SeleniumActions getActions() {
        return a;
    }

    public final void setActions(SeleniumActions actions) {
        this.a = actions;
    }

    @Nonnull
    @Override
    public String getWebPagePath() {
        Optional<String> optionalPathFromAnnotation = PAGE_UTILS.getWebPagePathForClass(getClass());
        if (optionalPathFromAnnotation.isPresent()) {
            return optionalPathFromAnnotation.get();
        }
        return "/";
    }

    @Override
    public void pageLoadHook() {
        PAGE_UTILS.defaultPageLoadHook(this, a);
    }


    @Override
    public By getPageIdentifier() {
        return null;
    }

    public final void initSubPages() {
        PAGE_UTILS.initSubPages(this, a);
    }

    @Override
    public final void refreshElements() {
        PageFactory.initElements(getActions().getBrowser().getWebDriver(), this);
        initSubPages();
        pageLoadHook();
    }


    @Override
    public void refreshPage() {
        getActions().getBrowser().refreshPage();
        refreshElements();
    }

    @Override
    public void leavePageHook() {

    }
}
