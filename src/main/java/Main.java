import org.openqa.selenium.WebDriver;

public class Main {
    public static final String HH = "https://hh.ru";

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
        Browser browser = new Browser();
        WebDriver driver = browser.openBrowser();
        driver.get(HH + "/account/login");
        browser.authentication(driver);
        new HTMLParser().parseUniqueEmployees(driver, browser);
    }
}