import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final String HH = "https://hh.ru";
    private static int countStart = 1;

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static Browser browser;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        log.info("Bot started");
        try {
            System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
            browser = new Browser();
            new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
        } catch (Exception e) {
            log.error("Error! Browser will restart!", e);
            try {
                stop();
            } finally {
                if (countStart < 10) {
                    countStart++;
                    log.error("The browser was closed with an error! Trying to work again. Attempt number: " + countStart);
                    start();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private static void stop() {
        browser.getDriver().quit();
    }
}