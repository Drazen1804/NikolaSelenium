package LagunaAutomation.Login;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestLagunaWebsite {
    private WebDriver driver;
    private String URL = "https://www.laguna.rs/";
    private WebElement searchTextField;
    private WebElement searchSubmitIcon;

    @BeforeMethod
    public void BeforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @Test
    public void testSearchFunctionality() {
        searchTextField = driver.findElement(By.id("pretraga_rec"));
        searchSubmitIcon = driver.findElement(By.id("pretraga_submit"));

        searchTextField.sendKeys("Bakman");
        searchSubmitIcon.click();

        List<WebElement> writerBooksWebElements = driver.findElements(By.cssSelector("div.podaci>a.naslov"));
        List<String> writerBooksTitles = new ArrayList<>();
        for (WebElement element : writerBooksWebElements) {
            writerBooksTitles.add(element.getText());
        }
        Assert.assertTrue(writerBooksTitles.contains("Pobednici"));
    }

    @Test
    public void testOpenBookDetails() {
        searchTextField = driver.findElement(By.id("pretraga_rec"));
        searchSubmitIcon = driver.findElement(By.id("pretraga_submit"));

        searchTextField.sendKeys("Pobednici");
        searchSubmitIcon.click();

        WebElement searchResultItem = driver.findElement(By.xpath("//div[@class='podaci']//a[@class='naslov']"));
        searchResultItem.click();

        //Validate opened book
        WebElement selectedBookTitle = driver.findElement(By.xpath("//div[@class='row hidden-sm hidden-xs podaci']//h1"));
        WebElement selectedBookAuthor = driver.findElement(By.xpath("//div[@class='row hidden-sm hidden-xs podaci']//h2//a"));
        Assert.assertTrue(selectedBookTitle.getText().equals("Pobednici"));
        Assert.assertTrue(selectedBookAuthor.getText().equals("Fredrik Bakman"));
    }

    @Test
    public void testCheckHeaderMenuList() {
        driver.get(URL);
        List<String> expectedMenuItems = Arrays.asList("Naslovna", "Knjige", "U pripremi", "#Bukmarker", "Top-liste", "Mala Laguna", "Klub čitalaca");
        List<WebElement> webElementListHeaderMenuItems = driver.findElements(By.xpath("//ul[@id='glavni-meni']//li/a"));

        List<String> headerMenuItems = new ArrayList<>();
        for(WebElement element : webElementListHeaderMenuItems) {
            headerMenuItems.add(element.getText());
        }
        Assert.assertTrue(headerMenuItems.containsAll(expectedMenuItems));
    }

    @Test
    public void testAddToShoppingCart() {
        // Select the first book from section 'Top Lista' and add to shopping cart
        WebElement topListeWebElement = driver.findElement(By.xpath("//div[@id='glavni-meni-wrapper']//ul[@id='glavni-meni']//li[5]"));
        topListeWebElement.click();

        WebElement topRatedBook = driver.findElement(By.cssSelector("div#spisak-knjiga-knjige>div.knjiga.col-lg-3.col-md-3.col-sm-4.col-xs-8:nth-child(3)>div.podaci>a.naslov"));
        String topRatedBookTitle = topRatedBook.getText();
        String topRatedBookImageSelector = String.format("img[alt='%s']", topRatedBookTitle);
        WebElement topRatedBookImage = driver.findElement(By.cssSelector(topRatedBookImageSelector));
        topRatedBookImage.click();

        WebElement dodajuKorpuButton = driver.findElement(By.xpath("//div[@class='col-xs-24 col-sm-16 col-md-6 col-lg-6 col-md-push-14 col-lg-push-14']//a[@id='dugme-korpa']"));
        dodajuKorpuButton.click();

        WebElement korpaIcon = driver.findElement(By.id("korpa_broj"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("korpa_broj"), "1"));

        Assert.assertEquals(korpaIcon.getText(), "1");

        korpaIcon.click();
        WebElement bookFromCart = driver.findElement(By.cssSelector("td.knjiga a.naslov"));
        Assert.assertEquals(topRatedBookTitle, bookFromCart.getText());
    }

    @Test
    public void testSocialMediaLinksArePresent() {
        WebElement facebookIcon = driver.findElement(By.xpath("//ul[@class='nav navbar-nav navbar-right hidden-xs']//li//a[contains(@href, 'facebook')]//img"));
        WebElement twitterIcon = driver.findElement(By.xpath("//ul[@class='nav navbar-nav navbar-right hidden-xs']//li//a[contains(@href, 'twitter')]//img"));
        WebElement instagramIcon = driver.findElement(By.xpath("//ul[@class='nav navbar-nav navbar-right hidden-xs']//li//a[contains(@href, 'instagram')]//img"));
        WebElement youtubeIcon = driver.findElement(By.xpath("//ul[@class='nav navbar-nav navbar-right hidden-xs']//li//a[contains(@href, 'youtube')]//img"));
        WebElement tiktokIcon = driver.findElement(By.xpath("//ul[@class='nav navbar-nav navbar-right hidden-xs']//li//a[contains(@href, 'tiktok')]//img"));

        Assert.assertTrue(facebookIcon.isDisplayed());
        Assert.assertTrue(twitterIcon.isDisplayed());
        Assert.assertTrue(instagramIcon.isDisplayed());
        Assert.assertTrue(youtubeIcon.isDisplayed());
        Assert.assertTrue(tiktokIcon.isDisplayed());
    }

    @Test
    public void testAddBookToFavorites() {
        searchTextField = driver.findElement(By.id("pretraga_rec"));
        searchSubmitIcon = driver.findElement(By.id("pretraga_submit"));

        searchTextField.sendKeys("Pobednici");
        searchSubmitIcon.click();

        WebElement searchResultItem = driver.findElement(By.xpath("//div[@class='podaci']//a[@class='naslov']"));
        searchResultItem.click();

        WebElement dodajNaListuZeljaButton = driver.findElement(By.xpath("//div[@class='col-xs-24 col-sm-16 col-md-6 col-lg-6 col-md-push-14 col-lg-push-14']//a[@id='dugme-zelje']"));
        dodajNaListuZeljaButton.click();
        WebElement zeljeIconBroj = driver.findElement(By.xpath("//a[@id='zelje_broj']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[@id='zelje_broj']"), "1"));


        Assert.assertEquals(zeljeIconBroj.getText(), "1");

        zeljeIconBroj.click();
        WebElement listaZeljaFirstBook = driver.findElement(By.xpath("//a[@class='naslov']"));
        Assert.assertEquals(listaZeljaFirstBook.getText(), "Pobednici");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
