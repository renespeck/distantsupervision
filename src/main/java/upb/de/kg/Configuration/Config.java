package upb.de.kg.Configuration;

public class Config {

    //Paths
    public static final String WIKIPEDIA_DUMP_PATH = "D:\\Croups\\";
    public static final String LOG_PATH = "D:\\Distant_Supervision\\Logs\\DistantSupervision.Log";

    // Database
    public static final String SERVER_NAME = "localhost";
    public static final int PORT = 27017;
    public static final String DATABASENAME = "DistantSupervision";
    public static final String COLLECTION_NAME = "Items";

    //Threads
    public static final int ParallelThread = 10;

    //
    public static final int SentenceLimit = 10;
    public static final int FileLimit = 10;

    public static final String LabelsLimit = "20";
}
