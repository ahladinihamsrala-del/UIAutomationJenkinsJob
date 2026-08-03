package com.ixigo.travelbooking.driver;
public final class BrowserContext {

private static final ThreadLocal<String> BROWSER = new ThreadLocal<>();


private BrowserContext() {
}

public static void setBrowser(String browser) {
BROWSER.set(browser);
}

public static String getBrowser() {
String browser = BROWSER.get();
if (browser == null || browser.isBlank()) {
	 // fallback to system property for single-browser runs
    browser = System.getProperty("browser", "chrome");
}
return browser;
}



public static void clear() {
BROWSER.remove();

}
}
