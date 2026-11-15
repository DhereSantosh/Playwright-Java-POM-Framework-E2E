package org.example;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Page page = browser.newPage();
            page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            System.out.println("Site Title : " + page.title());

            //assertThat(page.getByAltText("Login")).isVisible();
            assertThat(page.getByRole(AriaRole.HEADING,
                    new Page.GetByRoleOptions().setName("Login"))).isVisible();

            page.getByPlaceholder("Username").fill("Admin");
            page.getByPlaceholder("Password").fill("admin123");
            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Login")).click();

        }
    }
}*/
