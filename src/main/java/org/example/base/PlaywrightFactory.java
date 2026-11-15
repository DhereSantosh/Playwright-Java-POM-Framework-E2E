package org.example.base;

import com.microsoft.playwright.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PlaywrightFactory {

    // ThreadLocal containers to ensure cross-thread isolation for parallel test runs
    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    protected Properties prop;
    /**
     * Reads environment profiles straight out of properties file storage
     */
    public Properties initProperties() {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to parse config.properties structure: " + e.getMessage());
        }
        return prop;
    }

    /**
     * Initializes Playwright, launches the specified browser, and instantiates the target view.
     * @param browserName Name of the browser (e.g., chromium, firefox, webkit)
     * @param isHeadless Set true for headless run (CI/CD environments)
     * @return The active localized Page interface
     */

    public Page initBrowser(String browserName, boolean isHeadless, String storageStatePath) {
        System.out.println("Initializing " + browserName + " in headless=" + isHeadless + " execution state.");

        tlPlaywright.set(Playwright.create());

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setSlowMo(isHeadless ? 0 : 50); // Minor intentional slowdown provided exclusively for debugging locally

        switch (browserName.toLowerCase().trim()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium().launch(options));
                break;
            case "firefox":
                tlBrowser.set(getPlaywright().firefox().launch(options));
                break;
            case "webkit":
                tlBrowser.set(getPlaywright().webkit().launch(options));
                break;
            default:
                System.out.println("Unsupported browser target choice: " + browserName + ". Defaulting to Chromium launch.");
                tlBrowser.set(getPlaywright().chromium().launch(options));
                break;
        }

        // Configure Context Options to check if a saved login session is available
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        if (storageStatePath != null && !storageStatePath.isEmpty()) {
            System.out.println("Injecting existing authentication session state from: " + storageStatePath);
            contextOptions.setStorageStatePath(Paths.get(storageStatePath));
        }

        tlContext.set(getBrowser().newContext(contextOptions));
        tlPage.set(getContext().newPage());

        return getPage();
    }
    // Overloaded helper method to support tests that don't need auth state injection
    public Page initBrowser(String browserName, boolean isHeadless) {
        return initBrowser(browserName, isHeadless,null);
    }

    // Explicit Getters to retrieve localized thread context elements safely
    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    /**
     * Captures a runtime failure screenshot encoded directly to Base64
     * to eliminate broken image paths on Jenkins CI/CD servers.

    public static String takeScreenshot() {
        byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
        return java.util.Base64.getEncoder().encodeToString(buffer);
    }*/

    /**
     * Wipes local context structures down cleanly to eliminate continuous pipeline memory leakage.
     */
    public void quitBrowser() {
        if (getPage() != null) getPage().close();
        if (getContext() != null) getContext().close();
        if (getBrowser() != null) getBrowser().close();
        if (getPlaywright() != null) getPlaywright().close();

        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}