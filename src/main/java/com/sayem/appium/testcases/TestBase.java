package com.sayem.appium.testcases;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.util.Properties;

public class TestBase {
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
}
