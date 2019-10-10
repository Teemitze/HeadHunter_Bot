import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.RepositoryVacancy;

public class Browser {
    public String getWebPageWithEmployees(String profession, int page) {
        String link = Main.HH + "/search/resume?area=1&clusters=true" +
                "&exp_period=all_time" +
                "&gender=male" +
                "&relocation=living" +
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

    public void clickButtonOffer(WebDriver driver) throws NoSuchElementException, TimeoutException {
        new RepositoryVacancy().addVacancy(HTMLParser.getUUIDEmployeeFromURL(driver.getCurrentUrl()));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("bloko-button_secondary")));
        webElement.click();
    }

    public void clickComboBoxVacancy(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("Bloko-CustomSelect")));
        webElement.click();
    }

    public void chooseVacancy(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("Bloko-CustomSelect-Search")));
        for (int i = 0; i < Configuration.POSITION_VACANCY; i++) {
            webElement.sendKeys(Keys.DOWN);
        }
        webElement.sendKeys(Configuration.VACANCY);
        webElement.sendKeys(Keys.ENTER);
    }

    public void clickSendMessage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=\"submit\"]")));
        webElement.click();
    }

    public void sendOffer(WebDriver driver) throws NoSuchElementException {
        clickButtonOffer(driver);
        clickComboBoxVacancy(driver);
        chooseVacancy(driver);
        clickSendMessage(driver);
        if (Configuration.MAX_LIMIT_SEND_OFFER) {
            maxLimitSendOffer(driver);
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
