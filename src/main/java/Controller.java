import logger.Logs;

import java.util.logging.Level;

public class Controller implements Runnable {

    private Browser browser;

    private void start() {
        try {
            System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
            this.browser = new Browser();
            new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
        } catch (Exception e) {
            Logs.infoLog.log(Level.SEVERE, "Exception!", e);
        }
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
        start();
    }
}
