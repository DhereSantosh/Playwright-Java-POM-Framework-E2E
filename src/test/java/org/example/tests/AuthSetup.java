package org.example.tests;


import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.example.base.BaseTest;
import org.example.base.PlaywrightFactory;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import com.microsoft.playwright.options.LoadState;

public class AuthSetup extends BaseTest {

    @Test(groups = {"smoke","regression"},description = "Generate global authentication state JSON mapping file for active execution session context sharing")
    public void generateGlobalAppStateJson() {

        LoginPage loginPage = new LoginPage(getPage());

        // Fetch URL and credentials dynamically from config.properties
        loginPage.navigateToLoginPage(prop.getProperty("url"));
        loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));

        // 2. Structural assertion validation to confirm landing state safety
        Assert.assertTrue(loginPage.isDashboardVisible(), "Authentication failed, cannot save application state JSON mapping.");

        getPage().waitForURL("**/dashboard/index");
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
        // 3. Extract and save the captured browser state storage down to a local tracking folder
        PlaywrightFactory.getContext().storageState(
                new BrowserContext.StorageStateOptions()
                        .setPath(Paths.get("src/test/resources/appLoginState.json"))
        );

        System.out.println("Global login authentication states exported successfully to resources workspace directory.");
    }
}
