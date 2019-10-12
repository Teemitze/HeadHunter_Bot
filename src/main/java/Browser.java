import logger.Logs;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.RepositoryVacancy;

import java.util.logging.Level;

public class Browser {

    private WebDriver driver;
    private WebDriverWait wait;

    Browser() {
        this.driver = new FirefoxDriver();
        this.driver = openBrowser();
        this.wait = new WebDriverWait(driver, 10);
        authentication();
        new HTMLParser().parseUniqueEmployees(driver, this);
    }

    String getWebPageWithEmployees(int page) {
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
                "&text=" + Configuration.PROFESSION +
                "&age_from=20" +
                "&from=cluster_age" +
                "&showClusters=true" +
                "&page=" + page;

        return link;
    }

    private WebDriver openBrowser() {
        driver.manage().window().maximize();
        return driver;
    }

    private void authentication() {
        try {
            driver.get(Main.HH + "/account/login");
            WebElement loginAndPassword = driver.findElement(By.name("username"));
            loginAndPassword.sendKeys(Configuration.LOGIN);
            loginAndPassword = driver.findElement(By.name("password"));
            loginAndPassword.sendKeys(Configuration.PASSWORD);
            loginAndPassword.submit();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Logs.infoLog.log(Level.SEVERE, "Thread interrupted its work", e);
            throw new RuntimeException(e);
        }
    }

    private void clickButtonOffer(WebDriver driver) throws NoSuchElementException, TimeoutException {
        new RepositoryVacancy().addVacancy(HTMLParser.getUUIDEmployeeFromURL(driver.getCurrentUrl()));
        waitWebElementToBeClickable("bloko-button_secondary").click();
    }

    private void clickComboBoxVacancy() {
        waitWebElementToBeClickable("Bloko-CustomSelect").click();
    }

    private void chooseVacancy() {
        WebElement comboBoxVacancy = waitWebElementToBeClickable("Bloko-CustomSelect-Search");
        comboBoxVacancy.sendKeys(Configuration.VACANCY);
        for (int i = 0; i < Configuration.POSITION_VACANCY; i++) {
            comboBoxVacancy.sendKeys(Keys.DOWN);
        }
        comboBoxVacancy.sendKeys(Keys.ENTER);
    }

    private void clickSendMessage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=\"submit\"]"))).click();
    }

    void sendOffer(WebDriver driver) throws NoSuchElementException {
        clickButtonOffer(driver);
        clickComboBoxVacancy();
        chooseVacancy();
        clickSendMessage();
        if (maxLimitSendOffer(driver)) {
            Logs.infoLog.warning("The vacancy limit has been reached! Create a new vacancy!");
            driver.quit();
            System.exit(0);
        }
    }

    private boolean maxLimitSendOffer(WebDriver driver) {
        try {
            driver.findElement(By.className("bloko-notification__plate"));
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    private WebElement waitWebElementToBeClickable(String element) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.className(element)));
    }

    static boolean maxLimitResumeView(WebDriver driver) {
        try {
            driver.findElement(By.className("attention_bad"));
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }
}