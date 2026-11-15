package org.example.tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.example.base.BaseTest;
import org.example.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.testng.Assert.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardTest extends BaseTest {
    //enabled = false,
    @Test(groups = {"smoke"}, description = "Verify user can directly navigate to dashboard sections skipping the authentication phase entirely")
    public void testDirectDashboardAccessMode() {
        // Step straight onto the internal site layout, bypassing the main Login screen forms completely
        getPage().navigate(prop.getProperty("url"));

        assertThat(getPage()).hasURL(Pattern.compile(".*/dashboard/index$"));
        System.out.println("Landing page URL state validated successfully: " + getPage().url());

    }

    @Test(groups = {"functional", "regression"},description = "Verify Quick Launch Options")
    public void testQuickLaunchOptions(){

        DashboardPage dashboardPage = new DashboardPage(getPage());
        // Step straight onto the internal site layout, bypassing the main Login screen forms completely
        getPage().navigate(prop.getProperty("url"));

        dashboardPage.AssignLeaves();
        assertTrue(getPage().url().contains("assignLeave"), "Assign Leave Page Not Opened.");
        dashboardPage.getBackToDashboard();

        dashboardPage.LeaveList();
        assertTrue(getPage().url().contains("viewLeaveList"),"Leave List Page Not Opened.");
        dashboardPage.getBackToDashboard();

        dashboardPage.Timesheets();
        assertTrue(getPage().url().contains("viewEmployeeTimesheet"),"Timesheet Page Not Opened.");
        dashboardPage.getBackToDashboard();

        dashboardPage.ApplyLeave();
        assertTrue(getPage().url().contains("applyLeave"),"Apply Leaves Page Not Opened.");
        dashboardPage.getBackToDashboard();

        dashboardPage.MyLeaves();
        assertTrue(getPage().url().contains("viewMyLeaveList"),"My Leaves Page Not Opened.");
        dashboardPage.getBackToDashboard();

        dashboardPage.MyTimesheet();
        assertTrue(getPage().url().contains("viewMyTimesheet"),"My Timesheet Page Not Opened.");
        dashboardPage.getBackToDashboard();
    }

    @Test(groups = {"functional", "regression"}, description = "Verify the OrangeHRM About Dialogbox")
    public void testAboutDialogBox(){

        DashboardPage dashboardPage = new DashboardPage(getPage());

        getPage().navigate(prop.getProperty("url"));

        dashboardPage.OrangeHRMAboutPage();
        assertThat(dashboardPage.getAboutDialog()).isVisible();
        assertThat(dashboardPage.getAboutDialog().getByRole(AriaRole.HEADING, new Locator.GetByRoleOptions().setName("About"))).isVisible();

        // 2. Fetch value parameters using clean index locations
        String companyNameText = dashboardPage.getCompanyNameValue();
        String appVersionText  = dashboardPage.getVersionValue();

        System.out.println("Company Name: " + companyNameText);
        System.out.println("App Version: " + appVersionText);

        assertEquals(companyNameText, "OrangeHRM", "Mismatch detected inside Company Name field block.");
        assertTrue(appVersionText.contains("OrangeHRM OS"), "Application platform version mismatch.");

        dashboardPage.closeAboutDialogBox();
    }

    @Test(groups = {"functional"}, description = "Verify the Time At Work")
    public void testTimeAtWork(){
        DashboardPage dashboardPage = new DashboardPage(getPage());
        getPage().navigate(prop.getProperty("url"));

        dashboardPage.TimeAtWorkPunchIn();
        assertEquals(dashboardPage.ActionMessage(),"Successfully Saved1");
    }

    @Test(groups = {"functional"}, description = "Verify the MyActions")
    public void testMyActions(){
        DashboardPage dashboardPage = new DashboardPage(getPage());
        getPage().navigate(prop.getProperty("url"));

        dashboardPage.MyActions_leaveRequests();
        dashboardPage.getBackToDashboard();

        dashboardPage.MyActions_PendingSelfReview();
        dashboardPage.getBackToDashboard();
    }
}
