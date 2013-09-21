package com.sayem.appium.pages.myexpert;

import com.sayem.appium.pages.random.SignupPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MyExpertPage {

    private WebDriver driver;

    public MyExpertPage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(xpath = "//window[1]/button[1]")
    private WebElement signUpLoginButton;

    public SignupPage signUpAndLogin(){
        signUpLoginButton.click();
        return PageFactory.initElements(driver, SignupPage.class);

    }
}
