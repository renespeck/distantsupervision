package upb.de.kg.Logger;

import upb.de.kg.Configuration.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

enum Level {
    INFO, WRANNING, ERROR
}

public class Logger {

    private static File logFile;
    private static boolean CONSOLEOUTPUT = true;

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

        info("Logger Initialized");
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

        if (CONSOLEOUTPUT)
            System.out.println(msg);
    }

    public static void info(String msg) throws IOException {
        writeonFile(Level.INFO, msg);
    }

    public static void error(String msg) throws IOException {
        writeonFile(Level.ERROR, msg);
    }

    public static void warning(String msg) throws IOException {
        writeonFile(Level.WRANNING, msg);
    }


}
