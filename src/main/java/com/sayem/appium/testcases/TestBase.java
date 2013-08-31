package com.sayem.appium.testcases;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

public class TestBase {
    public static Logger APPLICATION_LOGS = null;
    public static Properties CONFIG=null;
    public static WebDriver driver=null;
    public static boolean isLoggedIn=false;
}
