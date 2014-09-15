package com.sayem.appium.pagefactory.framework.models;

public class Android {

    private String platformVersion;
    private String deviceName;
    private String appPath;
    private String appPackage;
    private String appAcitivity;
    private String appUrl;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppAcitivity() {
        return appAcitivity;
    }

    public void setAppAcitivity(String appAcitivity) {
        this.appAcitivity = appAcitivity;
    }
}

