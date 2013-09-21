package com.sayem.appium.testcases;

import com.sayem.appium.base.TestBase;
import com.sayem.appium.pages.LandingPage;
import com.sayem.appium.pages.SignupPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

public class TestSignup extends TestBase {

    @BeforeMethod
    public void setUp() throws Exception {
        initDriver();
    }

    @Test
    protected void testUIComputation() throws Exception {
        Random rand = new Random();
        int random = rand.nextInt((2147483647 - 1) + 1);
        char c = (char)(rand.nextInt(26) + 'a');
        String emailAddress = "signup"+c+random+c+"@ilearnvest.com";

        LandingPage landingPage = PageFactory.initElements(driver, LandingPage.class);
        SignupPage signupPage = landingPage.signUpAndLogin();
        signupPage.signUp(emailAddress, "user1234", "Syed", "30");
    }

    @AfterMethod
    public void tearDown(){
        quitDriver();
    }
}
