package com.sayem.appium.pages.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountPage {

    private WebDriver driver;

    public AccountPage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(xpath = "//window[1]/toolbar[1]/button[2]")
    private WebElement accountPlusSign;

    @FindBy(xpath = "//window[1]/textfield[1]")
    private WebElement searchForAnAccount;

    @FindBy(xpath = "//window[1]/tableview[1]/cell[1]/text[1]")
    private WebElement popUpAccountSearchName;
}
