package logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logs {

    private static final String DIRECTORY_LOGS = "./logs";

    public static Logger infoLog = logs("info", "info");
    public static Logger invitedLog = logs("invitations", "invitations");

    public static Logger logs(String nameLog, String directory) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Logger logger = Logger.getLogger(nameLog);
        try {
            checkExistLogsDir("info", "invitations");
            FileHandler fh = new FileHandler(DIRECTORY_LOGS + "/" + directory + "/" + nameLog + "_" + format.format(Calendar.getInstance().getTime()) + ".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            throw new RuntimeException(e + "\nNo rights to create log file!");
        } catch (IOException e) {
            throw new RuntimeException(e + "\nCould not find log file!");
        }
        return logger;
    }


    public static void checkExistLogsDir(String infoDir, String invitationsDir) {

        Path pathInfoDir = Paths.get(DIRECTORY_LOGS, infoDir);
        Path pathInvitationsDir = Paths.get(DIRECTORY_LOGS, invitationsDir);

        if (Files.notExists(pathInfoDir) || Files.notExists(pathInvitationsDir)) {
            try {
                Files.createDirectories(pathInfoDir);
                Files.createDirectories(pathInvitationsDir);
            } catch (IOException e) {
                Logs.infoLog.log(Level.SEVERE, "Failed to create directories", e);
                e.printStackTrace();
            }
        }
    }
}
