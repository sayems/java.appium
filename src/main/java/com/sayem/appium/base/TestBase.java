package com.sayem.appium.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public abstract class TestBase {

	public static Logger APPLICATION_LOGS = null;
	public static Properties CONFIG=null;
	public static WebDriver driver=null;
	public static boolean isLoggedIn=false;

	
	public void initConfigurations(){
		if(CONFIG==null){
		// Logging
		APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
		// config.prop
		 CONFIG = new Properties();
		try {
			FileInputStream fs = new FileInputStream("Appium/src/main/java/com/sayem/config/config.properties");
			CONFIG.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
	public void initDriver() throws MalformedURLException {
        // set up appium
        File app = new File("/Users/ssayem/Dropbox/Appium/LearnVest/LearnVest.app");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");
        capabilities.setCapability(CapabilityType.VERSION, "6.0");
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
        capabilities.setCapability("app", app.getAbsolutePath());
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}
	
	public void quitDriver(){
		driver.quit();
		driver=null;
	}
}
