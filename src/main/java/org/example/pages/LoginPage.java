package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    private final Page page;

    // Constructor initializes the page instance from the running test
    public LoginPage(Page page) {
        this.page = page;
    }

    /**
     * Navigates to the login screen and verifies structural heading components are present
     */
    public void navigateToLoginPage(String url) {
        page.navigate(url);
        // Asserts that the heading "Login" is visible before typing
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Login"))).isVisible();
    }

    /**
     * Orchestrates the primary authentication action workflow
     */
    public void doLogin(String username, String password) {
        page.getByPlaceholder("Username").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Login")).click();
    }

    /**
     * Validation method to verify the user landed successfully on the dashboard page
     */
    public boolean isDashboardVisible() {
        // Playwright auto-waits for the dashboard header component to appear
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Dashboard"))).isVisible();
        return page.url().contains("dashboard");
    }
}