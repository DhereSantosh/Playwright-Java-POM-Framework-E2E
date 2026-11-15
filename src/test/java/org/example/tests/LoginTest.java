package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke"},description = "Verify that a user can successfully log into the OrangeHRM system using valid credentials")
    public void testOrangeHRMLoginWorkflow() {
        // 1. Instantiate the target Page Object modeling component, passing the protected 'page' instance from BaseTest
        LoginPage loginPage = new LoginPage(getPage());

        // 2. Navigate out to the target instance site location
        loginPage.navigateToLoginPage("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        System.out.println("Landing Site Title verified as: " + getPage().title());

        // 3. Execute the login operation steps smoothly with standard tracking credentials
        loginPage.doLogin("Admin", "admin123");

        // 4. Assert structural validation criteria using TestNG assertions
        boolean loginSuccess = loginPage.isDashboardVisible();
        Assert.assertTrue(loginSuccess, "Login check failed: Dashboard components were not detected successfully within runtime window.");
        System.out.println("Execution finished cleanly. Final landing page URL state: " + getPage().url());
    }
}