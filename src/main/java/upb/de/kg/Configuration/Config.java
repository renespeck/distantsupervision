package upb.de.kg.Configuration;

public class Config {

    //Paths
    public static final String WIKIPEDIA_DUMP_PATH = "C:\\DistantSupervison\\WikipediaDump\\FreshDump";
    public static final String LOG_PATH = "C:\\DistantSupervison\\Logs\\DistantSupervision.Log";


    // Database
    public static final boolean USE_REMOTE_DB = true;
    public static final String SERVER_NAME = "localhost";
    public static final int PORT = 27017;
    public static final String DATABASE_NAME = "distantsupervision";
    public static final String COLLECTION_NAME = "DataSet";
    public static final String REMOTE_URI = "mongodb://admin:admin@ds231987.mlab.com:31987/distantsupervision";

    //Threads
    public static final int DIRECTORY_THREAD_LIMIT = 12;

    //
    //public static final int SentenceLimit = 10;
    //public static final int FileLimit = 1500;

    public static final String LABELS_LIMIT = "100";

    //Log
    public static final boolean CONSOLE_OUTPUT = true;
}
