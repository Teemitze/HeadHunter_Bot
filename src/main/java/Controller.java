import logger.Logs;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.util.logging.Level;

public class Controller implements Runnable {

    private Browser browser;

    private void start() {
        System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
        this.browser = new Browser();
        new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
    }

    private void stop() {
        this.browser.getDriver().quit();
    }

    void reboot() {
        stop();
        start();
    }

    @Override
    public void run() {
        try {
            start();
        } catch (NoSuchElementException | TimeoutException e) {
            Logs.infoLog.log(Level.SEVERE, "Error, element not found, the browser will restart!", e);
            reboot();
        } catch (WebDriverException e) {
            Logs.infoLog.log(Level.SEVERE, "The browser was closed!");
            System.exit(0);
        }
    }
}
