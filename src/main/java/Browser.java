import configuration.ConfigurationHHBot;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Browser implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(Browser.class);

    private WebDriver driver;
    private WebDriverWait wait;

    Browser() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        this.driver = new ChromeDriver(chromeOptions);
        this.driver = openBrowser();
        this.wait = new WebDriverWait(driver, 120);
        authentication();
    }

    public static String getWebPageWithEmployees(int page) {
        String link = Main.HH + "/search/resume?" +
                "area=1&clusters=true" +
                "&driver_license_types=B" +
                "&exp_period=all_time" +
                "&label=only_with_gender" +
                "&logic=normal&no_magic=false" +
                "&order_by=relevance" +
                "&pos=full_text&text=" + ConfigurationHHBot.PROFESSION +
                "&gender=male&from=cluster_gender&showClusters=true" +
                "&items_on_page=100" +
                "&page=" + page;

        return link;
    }

    private WebDriver openBrowser() {
        driver.manage().window().maximize();
        return driver;
    }

    private void authentication() {
        driver.get(Main.HH + "/account/login");
        pause(2500);
        WebElement loginAndPassword = driver.findElement(By.name("username"));
        loginAndPassword.sendKeys(ConfigurationHHBot.LOGIN_HH);
        loginAndPassword = driver.findElement(By.name("password"));
        loginAndPassword.sendKeys(ConfigurationHHBot.PASSWORD_HH);
        loginAndPassword.submit();
        pause(2500);
    }

    private void clickComboBoxVacancy() {
        waitWebElementToBeClickable("Bloko-CustomSelect").click();
    }

    private void chooseVacancy() {
        WebElement comboBoxVacancy = waitWebElementToBeClickable("Bloko-CustomSelect-Search");
        comboBoxVacancy.sendKeys(ConfigurationHHBot.VACANCY);
        for (int i = 0; i < ConfigurationHHBot.POSITION_VACANCY; i++) {
            comboBoxVacancy.sendKeys(Keys.DOWN);
        }
        comboBoxVacancy.sendKeys(Keys.ENTER);
    }

    private void clickSendMessage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=\"submit\"]"))).click();
    }

    void sendOffer(WebDriver driver) throws NoSuchElementException, TimeoutException {
        clickComboBoxVacancy();
        chooseVacancy();
        clickSendMessage();
        if (maxLimitSendOffer(driver)) {
            closeBrowser(driver, "The vacancy limit has been reached! Create a new vacancy!");
        }
        pause(500);
    }

    public static void closeBrowser(WebDriver driver, String message) {
        log.warn(message);
        driver.quit();
        System.exit(0);
    }

    private boolean maxLimitSendOffer(WebDriver driver) {
        pause(500);
        final String MAX_LIMIT_SEND_OFFER_MESSAGE = "Превышено максимальное количество приглашений на данную вакансию. Создайте новую вакансию для отправки приглашений.";
        try {
            return driver.findElement(By.className("bloko-notification__plate")).getText().equals(MAX_LIMIT_SEND_OFFER_MESSAGE);
        } catch (NoSuchElementException | TimeoutException ignored) {
            return false;
        }
    }

    private WebElement waitWebElementToBeClickable(String element) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.className(element)));
    }

    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    WebDriver getDriver() {
        return driver;
    }

    @Override
    public void close() {
        this.getDriver().quit();
    }
}