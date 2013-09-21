package com.sayem.appium.pages.random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    @FindBy(xpath = "//window[1]/scrollview[1]/textfield[1]")
    private WebElement emailAddress;

    @FindBy(xpath = "//window[1]/scrollview[1]/secure[1]")
    private WebElement password;

    @FindBy(xpath = "//window[1]/scrollview[1]/button[5]")
    private WebElement submitButton;

    @FindBy(xpath = "//window[2]/toolbar[1]/button[3]")
    private WebElement doneButton;

    @FindBy(xpath = "//window[1]/button[9]")
    private WebElement passcodeOne;

    @FindBy(xpath = "//window[1]/button[10]")
    private WebElement passcodeTwo;

    @FindBy(xpath = "//window[1]/button[11]")
    private WebElement passcodeThree;

    @FindBy(xpath = "//window[1]/button[12]")
    private WebElement passcodeFour;

    public HomePage loginIn(String emailAddress, String password) throws InterruptedException {
        Thread.sleep(3000L);
        this.emailAddress.sendKeys(emailAddress);
        this.doneButton.click();
        this.password.sendKeys(password);
        this.doneButton.click();
        this.submitButton.click();
        return PageFactory.initElements(driver, HomePage.class);
    }

    private void passcodeMethod() {
        try {
            passcodeOne.click();
            passcodeTwo.click();
            passcodeThree.click();
            passcodeFour.click();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
