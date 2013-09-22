package com.sayem.appium.pages.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PasscodePage {

    private WebDriver driver;

    public PasscodePage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(xpath = "//window[1]/toolbar[1]/button[2]")
    private WebElement accountPlusSign;
    public void passcode(){

    }

    public void changePasscode(){

    }
}
