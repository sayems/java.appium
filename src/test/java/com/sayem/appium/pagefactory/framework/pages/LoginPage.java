package com.sayem.appium.pagefactory.framework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BaseTopLevelPage{


    @FindBy(id = "accountname")
    WebElement accountName;

    @FindBy(id = "username")
    WebElement userName;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(id = "login_button")
    WebElement loginButton;



    public void login() throws InterruptedException {
        accountName.sendKeys("test");
        userName.sendKeys("sayem");
        password.sendKeys("passw0rd");
        loginButton.click();
        Thread.sleep(4000);
    }

}
