package com.sayem.appium.pagefactory.framework.models;

public class AndroidBuilder {

    public void androidInfo(){
        Android android = new Android();
        android.setAppUrl("http://127.0.0.1:4723/wd/hub");
        android.setPlatformVersion("4.4");
        android.setDeviceName("Moto X");
        android.setAppPath("/Users/ssayem/IdeaProjects/Appium/src/test/resources/someapp.apk");
        android.setAppPackage("com.company.companyandroid");
        android.setAppAcitivity("com.company.android.activities.HomeActivity");
    }
}
