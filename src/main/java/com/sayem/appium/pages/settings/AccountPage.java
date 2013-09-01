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

    //    Connect to account username and password


    @FindBy(xpath = "//window[1]/tableview[1]/cell[1]/textfield[1]")
    //@FindBy(name = "Username")
    private WebElement userName; // locate by name: Username

    @FindBy(xpath = "//window[1]/tableview[1]/cell[2]/secure[1]")
    private WebElement password; // locate by name: Password

    @FindBy(xpath = "//window[1]/tableview[1]/group[2]/button[1]")
    private WebElement cancelButton; // locate by name: Cancel

    @FindBy(xpath = "//window[1]/tableview[1]/group[2]/button[2]")
    private WebElement submitButton; // locate by name: Submit

    @FindBy(xpath = "//window[1]/toolbar[1]/button[1]")
    private WebElement backButton; // locate by name: Back


    @FindBy(xpath = "//window[2]/toolbar[1]/button[3]")
    private WebElement doneButton; // locate by name: Done

    public void addAnAccount() throws InterruptedException {
        accountPlusSign.click();
        searchForAnAccount.sendKeys("DAGBANK");
        Thread.sleep(1000L);
        popUpAccountSearchName.click();
        Thread.sleep(1000L);
        userName.sendKeys("Money2.bank1");
        doneButton.click();
        password.sendKeys("bank1");
        doneButton.click();
        submitButton.click();

    }
}
