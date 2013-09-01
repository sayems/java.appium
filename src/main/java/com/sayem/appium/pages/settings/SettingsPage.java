package com.sayem.appium.pages.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SettingsPage {


    private WebDriver driver;

    public SettingsPage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(name = "PASSCODE")
    private WebElement passcodeButton;

    @FindBy(name = "ACCOUNTS")
    private WebElement accountsButton;

    @FindBy(name = "HELP & SUPPORT")
    private WebElement helpAndSupportButton;

    @FindBy(name = "FEEDBACK")
    private WebElement feedbackButton;

    @FindBy(name = "LOG OUT")
    private WebElement  logoutButton;

    @FindBy(xpath = "//window[3]/alert[1]/button[2]")
    private WebElement alertBox;

    public void passcode(){
        passcodeButton.click();
    }

    public AccountPage accounts(){
        accountsButton.click();
        return PageFactory.initElements(driver, AccountPage.class);
    }

    public void helpAndSupport(){
        helpAndSupportButton.click();
    }

    public void feedback(){
        feedbackButton.click();
    }

    public void logout(){
        logoutButton.click();
        alertBox.click();

    }
}
