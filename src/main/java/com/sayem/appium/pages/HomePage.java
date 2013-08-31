package com.sayem.appium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {

    private WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(xpath = "//window[1]/button[1]")
    private WebElement myMoneyButton;

    @FindBy(xpath = "//window[1]/button[3]")
    private WebElement myGoalsButton;

    @FindBy(xpath = "//window[1]/button[6]")
    private WebElement myReadsButton;

    @FindBy(xpath = "//window[1]/button[3]")
    private WebElement myExpertButton;

    @FindBy(name = "Settings")
    private WebElement SettingsButton;


}
