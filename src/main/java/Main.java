import configuration.ConfigurationHHBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final String HH = "https://hh.ru";

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", ConfigurationHHBot.GECKO_DRIVER);
        try (Browser browser = new Browser()) {
            new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
        } catch (Exception e) {
            log.error("The browser was closed with an error!", e);
        }
    }
}