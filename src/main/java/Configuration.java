import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

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
            log.error("Error in loading configuration", e);
            throw new RuntimeException(e);
        }
        return properties;
    }
}
