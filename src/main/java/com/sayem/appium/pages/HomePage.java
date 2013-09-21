package com.sayem.appium.pages;

import com.sayem.appium.pages.myexpert.MyExpertPage;
import com.sayem.appium.pages.mygoal.MyGoalPage;
import com.sayem.appium.pages.mymoney.MyMoneyPage;
import com.sayem.appium.pages.myreads.MyReadsPage;
import com.sayem.appium.pages.settings.SettingsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public MyMoneyPage goToMyMoneyPage(){
        myMoneyButton.click();
        return PageFactory.initElements(driver, MyMoneyPage.class);
    }

    public MyGoalPage goToMyGoalPage(){
        myReadsButton.click();
        return PageFactory.initElements(driver, MyGoalPage.class);
    }

    public MyReadsPage goToMyReadsPage(){
        myReadsButton.click();
        return PageFactory.initElements(driver, MyReadsPage.class);
    }

    public MyExpertPage goToMyExpertPage(){
        myExpertButton.click();
        return PageFactory.initElements(driver, MyExpertPage.class);
    }

    public SettingsPage goToSettingsPage(){
        SettingsButton.click();
        return PageFactory.initElements(driver, SettingsPage.class);
    }
}
