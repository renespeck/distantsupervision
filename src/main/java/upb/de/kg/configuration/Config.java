package upb.de.kg.configuration;

public class Config {

    //Paths
    public static final String WIKIPEDIA_DUMP_PATH = "C:\\DistantSupervison\\WikipediaDump\\FreshDump";
    public static final String LOG_PATH = "C:\\DistantSupervison\\Logs\\DistantSupervision.Log";


    // Remote Database
    public static final boolean USE_REMOTE_DB = true;
    public static final String COLLECTION_NAME = "DataSet";
    public static final String REMOTE_URI = "mongodb://admin:admin@ds231987.mlab.com:31987/distantsupervision";

    // Local Database
    public static final String SERVER_NAME = "localhost";
    public static final int PORT = 27017;
    public static final String lOCAL_DATABASE_NAME = "DistantSupervision";
    public static final String TRAINING_COLLECTION_NAME = "TrainingDataSet";
    public static final String TEST_COLLECTION_NAME = "TestDataSet";

    //Threads
    public static final int DIRECTORY_THREAD_LIMIT = 12;


    public static final String LABELS_LIMIT = "100";

    //Log
    public static final boolean CONSOLE_OUTPUT = true;
}
