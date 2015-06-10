package org.sayem.appium.pages;

import org.sayem.appium.config.TimeoutType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Syed on 2/28/2015.
 */

public class HomePage extends BaseTopLevelPage {

    private WebDriver driver;

    @FindBy(css = ".skip-link.skip-account")
    private WebElement account;

    @FindBy(css = ".links > ul > li:nth-of-type(5) > a")
    private WebElement register;

    public RegisterPage register() {
        getActions().click(account, TimeoutType.DEFAULT);
        return PageFactory.initElements(driver, RegisterPage.class);
    }

}