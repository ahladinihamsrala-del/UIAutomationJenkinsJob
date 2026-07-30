package com.ixigo.travelbooking.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SessionCookieManager {

    private static final String COOKIE_FILE = "session-cookies.ser";

    public static void saveCookies(WebDriver driver) throws IOException {
        Set<Cookie> cookies = driver.manage().getCookies();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COOKIE_FILE))) {
            oos.writeObject(new ArrayList<>(cookies));
        }
        System.out.println("Saved " + cookies.size() + " cookies to " + COOKIE_FILE);
    }

    public static boolean cookiesExist() {
        return new File(COOKIE_FILE).exists();
    }

    @SuppressWarnings("unchecked")
    public static void loadCookies(WebDriver driver, String siteUrl) throws IOException, ClassNotFoundException {
        if (!cookiesExist()) {
            throw new FileNotFoundException(
                COOKIE_FILE + " not found. Run SaveSessionCookies manually first to generate it.");
        }

        driver.get(siteUrl); // must be on the domain before cookies can be added

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COOKIE_FILE))) {
            List<Cookie> cookies = (List<Cookie>) ois.readObject();
            for (Cookie cookie : cookies) {
                try {
                    driver.manage().addCookie(cookie);
                } catch (Exception e) {
                    System.out.println("Skipped cookie (domain mismatch likely): " + cookie.getName());
                }
            }
        }

        driver.navigate().refresh(); // reload so the site recognizes the session
        System.out.println("Session cookies loaded and page refreshed.");
    }
}