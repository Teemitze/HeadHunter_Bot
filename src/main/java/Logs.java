import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logs {


    public static Logger logs(String nameLog) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Logger logger = Logger.getLogger(nameLog);
        try {
            FileHandler fh = new FileHandler("logs/" + nameLog + "_" + format.format(Calendar.getInstance().getTime()) + ".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}
