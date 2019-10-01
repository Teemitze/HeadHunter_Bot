import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Browser {
    private Logger logger = Logs.logs("browser");

    public String getPage(String profession, int page) {
        String link = Main.HH + "/search/resume?area=1" +
                "&clusters=true" +
                "&exp_period=all_time" +
                "&logic=normal" +
                "&pos=full_text" +
                "&st=resumeSearch" +
                "&text=" + profession +
                "&from=suggest_post" +
                "&page=" + page;
        logger.info(link);
        return link;
    }


    public WebDriver openBrowser() {
        logger.info("find geckoDriver");
        System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
        WebDriver driver = new FirefoxDriver();
        logger.info("opening browser");
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        driver.manage().window().maximize();
        logger.info("browser opened");
        return driver;
    }

    public void authentication(WebDriver driver) {
        logger.info("authentication start");
        WebElement webElement = driver.findElement(By.name("username"));
        webElement.sendKeys(Configuration.LOGIN);
        logger.info("input login");
        webElement = driver.findElement(By.name("password"));
        webElement.sendKeys(Configuration.PASSWORD);
        logger.info("input password");
        webElement.submit();
        logger.info("send login and password");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendOffer(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            logger.info("find button \"Пригласить\"");
            WebElement webElement = driver.findElement(By.className("bloko-button_secondary"));
            webElement.click();
            logger.info("click button \"Пригласить\"");
            logger.info("find combo-box \"Вакансия\"");
            webElement = driver.findElement(By.className("Bloko-CustomSelect-Selected"));
            webElement.click();
            logger.info("click combo-box \"Вакансия\"");
            webElement = driver.findElement(By.className("Bloko-CustomSelect-Search"));
            logger.info("region selection: " + Configuration.REGION);
            webElement.sendKeys(Configuration.REGION);
            webElement.sendKeys(Keys.ENTER);
            logger.info("send region: " + Configuration.REGION);
            logger.info("find button \"Изменить статус и отправить сообщение\"");
            webElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=\"submit\"]")));
            webElement.click();
            logger.info("click button \"Изменить статус и отправить сообщение\"");
            logger.info("offer send!");
        } catch (NoSuchElementException e) {
            logger.info("Do not find button \"Пригласить\"");
        }
    }
}
