import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestShopping {
    private WebDriver driver;
    private String URL = "https://www.laguna.rs/";
    private WebElement searchTextField;
    private WebElement searchSubmitIcon;

    @BeforeTest
    public void BeforeTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testSearchFunctionality() {
        driver.get(URL);

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
        driver.get(URL);
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
        driver.get(URL);

        WebElement topListeWebElement = driver.findElement(By.cssSelector("#glavni-meni-wrapper a[href*='top-liste']"));
        topListeWebElement.click();

        WebElement topRatedBook = driver.findElement(By.cssSelector("div#spisak-knjiga-knjige>div.knjiga.col-lg-3.col-md-3.col-sm-4.col-xs-8:nth-child(3)>div.podaci>a.naslov"));
        String topRatedBookTitle = topRatedBook.getText();
        String topRatedBookImageSelector = String.format("img[alt='%s']", topRatedBookTitle);
        WebElement topRatedBookImage = driver.findElement(By.cssSelector(topRatedBookImageSelector));
        topRatedBookImage.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        WebElement dodajuKorpuButton = driver.findElement(By.cssSelector("div#cena>a#dugme-korpa"));
        dodajuKorpuButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        WebElement korpaIcon = driver.findElement(By.id("korpa_broj"));
        Assert.assertEquals("1", korpaIcon.getText());

        korpaIcon.click();
        WebElement bookFromCart = driver.findElement(By.cssSelector("td.knjiga a.naslov"));
        Assert.assertEquals(topRatedBookTitle, bookFromCart.getText());
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

}
