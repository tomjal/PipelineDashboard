package io.dashboardhub.pipelinedashboard.secure;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.dashboardhub.pipelinedashboard.PipelinedashboardApplicationTests;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GuestDefinitions extends PipelinedashboardApplicationTests {

    final private String baseUrl = "http://localhost:8082";

    private WebDriver driver = new FirefoxDriver();

    @Given("^I am not logged in$")
    public void I_am_not_logged_in() {
        this.driver.get(this.baseUrl + "/logout");
    }

    @When("^I try to access a secure page$")
    public void I_try_to_access_a_secure_page() {
        this.driver.get(this.baseUrl + "/project");
    }

    @Then("^I get redirected to the login page$")
    public void I_get_redirected_to_the_login_page() {
        String currentUrl = this.driver.getCurrentUrl();

        Assert.assertEquals(this.baseUrl + "/login", currentUrl);

        this.driver.close();
    }
}
