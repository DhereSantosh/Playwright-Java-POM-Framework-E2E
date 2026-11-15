package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class DashboardPage {
    private final Page page;

    // Constructor initializes the page instance from the running test
    public DashboardPage(Page page) {
        this.page = page;
    }

    public Locator getAboutDialog() {

        return page.getByRole(AriaRole.DOCUMENT).last();
    }


    public void OrangeHRMAboutPage(){
        page.getByAltText("profile picture").click();
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("About")).click();
    }

    public void closeAboutDialogBox(){
        getAboutDialog().getByRole(AriaRole.BUTTON).click();
    }

    public String getCompanyNameValue() {
        return getAboutDialog().locator("p").nth(1).innerText().trim();
    }

    public String getVersionValue() {
        return getAboutDialog().locator("p").nth(3).innerText().trim();
    }

    public Page ClickHelpButtonAndGetNewTab(){
        // Use page.context().waitForPage to listen for a tab-opening event coming from this browser instance
        Page newTab = page.context().waitForPage(() -> {
            page.getByTitle("Help").click();
        });
        // Ensure the new tab finishes loading its static elements completely before moving on
        newTab.waitForLoadState();
        return newTab;
    }

    public void TimeAtWorkPunchIn(){
        // 1. Locate the parent element by its interactive button role
        Locator parentButton = page.getByRole(AriaRole.BUTTON)
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("i").and(page.locator(".bi-stopwatch"))));
        // 2. Chain down to find the specific child icon element inside that button
        Locator childButton = parentButton.locator("i");

        // 3. Perform your click action
        childButton.click();

        // 4. Punch In
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("In")).click();
    }

    public String ActionMessage(){
       return page.getByText("Successfully Saved").textContent();
    }

    public void MyActions_leaveRequests(){
        Locator leaveRequestsItem  = page.getByText(Pattern.compile(".*Leave Requests to Approve"));
        leaveRequestsItem.click();
    }

    public void MyActions_PendingSelfReview(){
        page.locator("div")
                .filter(new Locator.FilterOptions().setHasText("Pending Self Review"))
                .getByText(Pattern.compile(".*Pending Self Review"))
                .click();
    }

    public void getBackToDashboard(){
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Dashboard")).click();
    }

    public void AssignLeaves(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Assign Leave")).click();
    }

    public void LeaveList(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Leave List")).click();
    }

    public void Timesheets(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Timesheets")).click();
    }

    public void ApplyLeave(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Leave")).click();
    }

    public void MyLeaves(){
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("My Leave")).click();
    }

    public void MyTimesheet(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Timesheet")).click();
    }
}
