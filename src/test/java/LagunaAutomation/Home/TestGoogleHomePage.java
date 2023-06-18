package LagunaAutomation.Home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGoogleHomePage {
    private WebDriver driver;
    private String URL = "https://www.google.rs/";

    @Test
    public void test() {
        driver = new ChromeDriver();
        driver.get(URL);
        Assert.assertTrue(driver.getTitle().equals("Google"));
    }

}
