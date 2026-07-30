package com.ixigo.travelbooking.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SaveSessionCookies {

    public static void main(String[] args) throws Exception {
        ChromeOptions options = new ChromeOptions(); // NOT headless — you need to see and interact
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.ixigo.com/");

        System.out.println("=================================================");
        System.out.println("Log in manually now (enter mobile number + OTP).");
        System.out.println("Once fully logged in, come back here and press Enter.");
        System.out.println("=================================================");
        System.in.read();

        SessionCookieManager.saveCookies(driver);

        driver.quit();
    }
}