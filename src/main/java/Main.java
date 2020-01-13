import configuration.ConfigurationHHBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ConnectionFactory;
import repository.RepositoryVacancy;

import java.sql.Connection;

public class Main {
    static final String HH = "https://hh.ru";

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", ConfigurationHHBot.CHROME_DRIVER);
        try (Browser browser = new Browser()) {
            new HTMLParser().parseUniqueEmployees(browser.getDriver(), browser);
        } catch (Exception e) {
            log.error("The browser was closed with an error!", e);
            throw new RuntimeException(e);
        } finally {
            final Connection connection = new ConnectionFactory().getConnection();
            new RepositoryVacancy(connection).addVacancy(HTMLParser.invitedEmployees);
        }
    }
}