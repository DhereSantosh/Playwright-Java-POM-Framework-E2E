package org.example.base;

import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import java.util.Properties;


public class BaseTest {

    protected PlaywrightFactory playwrightFactory;

    protected Properties prop; // Accessible to all child test classes
    // Helper method so child test classes always get their own thread's isolated Page
    protected Page getPage() {
        return PlaywrightFactory.getPage();
    }

    /**
     * Set up hooks execution running dynamically before every isolated test profile executes.
     * Takes configuration options straight out of the testng.xml definition layout.
     */
    @Parameters({"browser", "headless", "storageState"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("") String browserName,
            @Optional("") String headlessMode,
            @Optional("") String storageStatePath)  {

        // Instantiate the primary framework factory engine
        playwrightFactory = new PlaywrightFactory();
        prop = playwrightFactory.initProperties(); // Load properties at test start
        // Fallback to config.properties if parameters are not explicitly passed via testng.xml
        String targetBrowser = browserName.isEmpty() ? prop.getProperty("browser", "chromium") : browserName;
        String targetHeadless = headlessMode.isEmpty() ? prop.getProperty("headless", "false") : headlessMode;
        boolean isHeadless = Boolean.parseBoolean(targetHeadless);

        // If a storage state path is configured in testng.xml, inject it; otherwise inject null
        if (storageStatePath != null && !storageStatePath.isEmpty()) {
            //page = playwrightFactory.initBrowser(targetBrowser, isHeadless, storageStatePath.trim());
            playwrightFactory.initBrowser(targetBrowser, isHeadless, storageStatePath.trim());
        } else {
            //page = playwrightFactory.initBrowser(targetBrowser, isHeadless);
            playwrightFactory.initBrowser(targetBrowser, isHeadless);
        }

    }
    /**
     * Tear down lifecycle cleaning loops execution running reliably right after test method terminations.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        if (getPage() != null && result.getStatus() == ITestResult.FAILURE) {
            captureFailureScreenshot(result.getName() + "_Failure_Snapshot");
        }

        playwrightFactory.quitBrowser();
    }

    /**
     * Allure-native byte screenshot attachment hook.
     * Allure grabs the returned byte array and embeds it cleanly inline inside your step reports.
     */
    @Attachment(value = "{screenshotName}", type = "image/png")
    public byte[] captureFailureScreenshot(String screenshotName) {
        System.out.println("Test failure encountered! Capturing Allure page attachment: " + screenshotName);
        return getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }
}