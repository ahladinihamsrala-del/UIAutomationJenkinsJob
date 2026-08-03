package com.ixigo.travelbooking.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.Dimension;

import io.github.bonigarcia.wdm.WebDriverManager;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {

        // -Dheadless=true (Jenkins) or -Dheadless=false (local debugging)
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        switch (browser.toLowerCase()) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");//req for Jenkins execution
                    chromeOptions.addArguments("--disable-dev-shm-usage");//req for Jenkins execution
                    chromeOptions.addArguments("--headless=new");//req for Jenkins execution
                    chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
                    chromeOptions.addArguments("--force-device-scale-factor=1");
                }
                WebDriverManager.chromedriver().setup();
               ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
                
                chromeDriver.manage().window().setSize(new Dimension(1920,1080));//1920,1080//2560,1440
                System.out.println("ACTUAL WINDOW SIZE: " + chromeDriver.manage().window().getSize());
                chromeDriver.manage().window().maximize();
                return chromeDriver;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--disable-gpu");
                }
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}