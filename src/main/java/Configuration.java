import logger.Logs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public class Configuration {

    private static final Path CONFIG_PATH = Paths.get("./config.properties");

    public static final String LOGIN = loadProperties().getProperty("login");
    public static final String PASSWORD = loadProperties().getProperty("password");
    public static final String VACANCY = loadProperties().getProperty("vacancy");
    public static final String PROFESSION = loadProperties().getProperty("profession");
    public static final int START_PAGE = Integer.parseInt(loadProperties().getProperty("startPage"));
    public static final int END_PAGE = Integer.parseInt(loadProperties().getProperty("endPage"));
    public static final int POSITION_VACANCY = Integer.parseInt(loadProperties().getProperty("positionVacancy"));
    public static final String GECKO_DRIVER = loadProperties().getProperty("geckoDriver");
    public static final int MAX_LIMIT_SEND_OFFER = Integer.parseInt(loadProperties().getProperty("maxLimitSendOffer"));

    public static Properties loadProperties() {
        final Properties properties = new Properties();
        try (final BufferedReader reader = Files.newBufferedReader(CONFIG_PATH)) {
            properties.load(reader);
        } catch (IOException e) {
            Logs.infoLog.log(Level.SEVERE, "Error in loading configuration", e);
            throw new RuntimeException(e);
        }
        return properties;
    }
}
