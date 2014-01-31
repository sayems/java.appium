package com.sayem.appium.examples;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public class AndroidTest {

    WebDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException{

        //Set up desired capabilities and pass the Android app-activity and app-package to Appium

        //File app = new File("/Users/sayem/appium-apps/AndroidTest.app");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","Android");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(CapabilityType.VERSION, "4.3");
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
        //capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("app-package", "com.android.calculator2"); // This is package name of your app (you can get it from apk info app)
        capabilities.setCapability("app-activity", "com.android.calculator2.Calculator"); // This is Launcher activity of your app (you can get it from apk info app)
        //Create RemoteWebDriver instance and connect to the Appium server.
        //It will launch the Calculator App in Android Device using the configurations specified in Desired Capabilities
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }
    @Test
    public void testCal(){
        //locate the Text on the calculator by using By.name()
        WebElement two=driver.findElement(By.name("2"));
        two.click();
        WebElement plus=driver.findElement(By.name("+"));
        plus.click();
        WebElement four=driver.findElement(By.name("4"));
        four.click();
        WebElement equalTo=driver.findElement(By.name("="));
        equalTo.click();
        //locate the edit box of the calculator by using By.tagName()
        WebElement results=driver.findElement(By.tagName("EditText"));
        //Check the calculated value on the edit box
        assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
    }

    @AfterClass
    public void teardown(){
        //close the app
        driver.quit();
    }
}