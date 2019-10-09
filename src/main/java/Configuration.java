import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Configuration {

    public static final String LOGIN = getProperties().getProperty("login");
    public static final String PASSWORD = getProperties().getProperty("password");
    public static final String VACANCY = getProperties().getProperty("vacancy");
    public static final String PROFESSION = getProperties().getProperty("profession");
    public static final int START_PAGE = Integer.parseInt(getProperties().getProperty("startPage"));
    public static final int END_PAGE = Integer.parseInt(getProperties().getProperty("endPage"));
    public static final int POSITION_VACANCY = Integer.parseInt(getProperties().getProperty("positionVacancy"));
    public static final String GECKO_DRIVER = getProperties().getProperty("geckoDriver");
    public static final boolean MAX_LIMIT_SEND_OFFER = Boolean.parseBoolean(getProperties().getProperty("maxLimitVacancySendOffer"));

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            InputStream stream = new FileInputStream(new File("config.properties"));
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            properties = new Properties();
            properties.load(reader);
            stream.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
