package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationHHBot {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationHHBot.class);

    private static final String CONFIG_PATH = "./config.properties";

    public static final String LOGIN_HH = loadProperties().getProperty("login_hh");
    public static final String PASSWORD_HH = loadProperties().getProperty("password_hh");
    public static final String LOGIN_DB = loadProperties().getProperty("login_db");
    public static final String PASSWORD_DB = loadProperties().getProperty("password_db");
    public static final String VACANCY = loadProperties().getProperty("vacancy");
    public static final String PROFESSION = loadProperties().getProperty("profession");
    public static final int START_PAGE = Integer.parseInt(loadProperties().getProperty("startPage"));
    public static final int END_PAGE = Integer.parseInt(loadProperties().getProperty("endPage"));
    public static final int POSITION_VACANCY = Integer.parseInt(loadProperties().getProperty("positionVacancy"));
    public static final String GECKO_DRIVER = loadProperties().getProperty("geckoDriver");
    public static final int MAX_LIMIT_SEND_OFFER = Integer.parseInt(loadProperties().getProperty("maxLimitSendOffer"));

    public static Properties loadProperties() {
        final Properties properties = new Properties();
        try (FileReader reader = new FileReader(CONFIG_PATH)) {
            properties.load(reader);
        } catch (IOException e) {
            log.error("Error in loading configuration", e);
            throw new RuntimeException(e);
        }
        return properties;
    }
}