package upb.de.kg.logger;

import upb.de.kg.configuration.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

enum Level {
    INFO, WRANNING, ERROR
}

/**
 * This Program supports logging and logging settings are defined in the config file.
 */
public class Logger {

    private static File logFile;

    static {
        try {
            intializeLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void intializeLogger() throws IOException {
        logFile = new File(Config.LOG_PATH);
        logFile.createNewFile();

        info("logger Initialized");
    }

    private static void writeonFile(Level level, String msg) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(level.name() + ":")
                .append(msg)
                .append(timeStamp);


        String fullMessage = strBuilder.toString();

        FileWriter writer = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(msg);
        bw.newLine();
        bw.close();

        if (Config.CONSOLE_OUTPUT)
            System.out.println(msg);
    }

    /**
     * This method takes log string and stores it as info in the file.
     * @param msg
     * @throws IOException
     */
    public static void info(String msg) throws IOException {
        writeonFile(Level.INFO, msg);
    }

    /**
     * This method takes log string and stores it as error in the log file.
     * @param msg
     * @throws IOException
     */
    public static void error(String msg) throws IOException {
        writeonFile(Level.ERROR, msg);
    }

    /**
     * This method takes log string and stores it as warning in the log file.
     * @param msg
     * @throws IOException
     */
    public static void warning(String msg) throws IOException {
        writeonFile(Level.WRANNING, msg);
    }


}
