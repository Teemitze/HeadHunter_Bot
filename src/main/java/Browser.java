import logger.Logs;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Level;

public class Browser {

    private WebDriver driver;
    private WebDriverWait wait;

    Browser() {
        this.driver = new FirefoxDriver();
        this.driver = openBrowser();
        this.wait = new WebDriverWait(driver, 10);
        authentication();
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
                "&items_on_page=100" +
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
        clickComboBoxVacancy();
        chooseVacancy();
        clickSendMessage();
        if (maxLimitSendOffer(driver)) {
            Logs.infoLog.warning("The vacancy limit has been reached! Create a new vacancy!");
            driver.quit();
            System.exit(0);
        }
        pause(500);
    }

    private boolean maxLimitSendOffer(WebDriver driver) {
        pause(200);
        final String MAX_LIMIT_SEND_OFFER_MESSAGE = "Превышено максимальное количество приглашений на данную вакансию. Создайте новую вакансию для отправки приглашений.";
        try {
            return driver.findElement(By.className("bloko-notification__plate")).getText().equals(MAX_LIMIT_SEND_OFFER_MESSAGE);
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    private WebElement waitWebElementToBeClickable(String element) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.className(element)));
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    WebDriver getDriver() {
        return driver;
    }
}