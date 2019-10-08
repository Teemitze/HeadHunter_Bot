import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.RepositoryVacancy;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Browser {
    public String getWebPageWithEmployees(String profession, int page) {
        String link = Main.HH + "/search/resume?area=1&clusters=true" +
                "&exp_period=all_time" +
                "&gender=male" +
                "&label=only_with_gender" +
                "&label=only_with_age" +
                "&logic=normal" +
                "&no_magic=false" +
                "&order_by=relevance" +
                "&pos=full_text" +
                "&text=" + profession +
                "&age_from=20" +
                "&from=cluster_age" +
                "&showClusters=true" +
                "&page=" + page;
        return link;
    }

    public WebDriver openBrowser() {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    public void authentication(WebDriver driver) {
        WebElement webElement = driver.findElement(By.name("username"));
        webElement.sendKeys(Configuration.LOGIN);
        webElement = driver.findElement(By.name("password"));
        webElement.sendKeys(Configuration.PASSWORD);
        webElement.submit();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickButtonOffer(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("bloko-button_secondary")));
        new RepositoryVacancy().addVacancy(HTMLParser.getUUIDEmployeeFromURL(driver.getCurrentUrl()));
        webElement.click();
    }

    public void clickComboBoxVacancy(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("Bloko-CustomSelect-Selected")));
        webElement.click();
    }

    public void chooseVacancy(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("Bloko-CustomSelect-Search")));
        webElement.sendKeys(Configuration.VACANCY);
        webElement.sendKeys(Keys.ENTER);
    }

    public void clickSendMessage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=\"submit\"]")));
        webElement.click();
    }

    public void sendOffer(WebDriver driver) {
        try {
            clickButtonOffer(driver);
            clickComboBoxVacancy(driver);
            chooseVacancy(driver);
            clickSendMessage(driver);
            if (Configuration.MAX_LIMIT_SEND_OFFER) {
                maxLimitSendOffer(driver);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    private void maxLimitSendOffer(WebDriver driver) {
        try {
            if (driver.findElements(By.className("bloko-notification__content Bloko-Notification-Content")).size() != 0) {
                System.exit(0);
            }
        } catch (NoSuchElementException ignored) {
        }
    }

    public static void maxLimitResumeView(WebDriver driver) {
        try {
            if (driver.findElements(By.className("attention_bad")).size() != 0) {
                System.exit(0);
            }
        } catch (NoSuchElementException ignored) {
        }
    }
}
