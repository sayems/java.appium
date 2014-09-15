package com.sayem.appium.pagefactory.framework.models;

public class UserBuilder {

    public User userInfo() {

        User user = new User();
        user.setAccountName("someaccount");
        user.setUserName("someusername");
        user.setPassword("somepassword");
        return user;

    }
}