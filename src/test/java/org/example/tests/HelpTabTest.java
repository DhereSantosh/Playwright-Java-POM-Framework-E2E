package org.example.tests;

import com.microsoft.playwright.Page;
import org.example.base.BaseTest;
import org.example.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HelpTabTest extends BaseTest {

    @Test(groups = {"regression"},description = "Verify clicking the '?' button safely spawns a fresh help documentation tab context")
    public void testHelpButtonOpensNewTab() {

        getPage().navigate(prop.getProperty("url"));

        DashboardPage dashboardPage = new DashboardPage(getPage());

        Page helpTab = dashboardPage.ClickHelpButtonAndGetNewTab();

        // 4. Validate actions inside the newly opened tab directly!
        System.out.println("Original Tab Title: " + getPage().title());
        System.out.println("Newly Opened Tab Title: " + helpTab.title());
        System.out.println("Newly Opened Tab URL: " + helpTab.url());

        Assert.assertTrue(helpTab.url().contains("starterhelp"),"Failed to confirm help tab routing address.");

        // 5. Clean up by closing the secondary child tab context when finished
        helpTab.close();
    }
}
