import logger.Logs;

import java.util.logging.Level;

public class Main {
    static final String HH = "https://hh.ru";
    private static int countStart = 1;

    private static Browser browser;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        try {
            System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
            browser = new Browser();
            new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
        } catch (Exception e) {
            Logs.infoLog.log(Level.SEVERE, "Error! Browser will restart!", e);
            try {
                stop();
            } finally {
                if (countStart < 10) {
                    countStart++;
                    Logs.infoLog.log(Level.SEVERE, "The browser was closed with an error! Trying to work again. Attempt number: " + countStart);
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