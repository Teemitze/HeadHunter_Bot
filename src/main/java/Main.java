import logger.Logs;

import java.util.logging.Level;

public class Main {
    static final String HH = "https://hh.ru";

    public static void main(String[] args) {
        try {
            System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
            new Browser();
        } catch (Exception e) {
            Logs.infoLog.log(Level.SEVERE, "Exception!", e);
        }
    }
}