package com.ixigo.travelbooking.driver;



import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {

private DriverFactory() {
}


public static WebDriver createDriver(String browser) {
	
	String gridUrl=System.getProperty("grid.url","http://192.168.4.190:4444/");

try {
switch (browser.toLowerCase()) {
case "chrome":
ChromeOptions chromeOptions = new ChromeOptions();
chromeOptions.addArguments("--window-size=1920,1080");
return new RemoteWebDriver(new URL(gridUrl), chromeOptions);

case "firefox":
FirefoxOptions firefoxOptions = new FirefoxOptions();
firefoxOptions.addArguments("--width=1920");
firefoxOptions.addArguments("--height=1080");
return new RemoteWebDriver(new URL(gridUrl), firefoxOptions);

case "edge":
EdgeOptions edgeOptions = new EdgeOptions();
edgeOptions.addArguments("--window-size=1920,1080");
return new RemoteWebDriver(new URL(gridUrl), edgeOptions);

default:
throw new IllegalArgumentException("Unsupported browser: " + browser);
}
} catch (MalformedURLException e) {
throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
}
}
}

