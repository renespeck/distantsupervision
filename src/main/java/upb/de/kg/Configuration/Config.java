package upb.de.kg.Configuration;

public class Config {

    //Paths
    public static final String WIKIPEDIA_DUMP_PATH = "D:\\Distant_Supervision\\WikiPedia_Dump\\FreshDump";
    public static final String LOG_PATH = "D:\\Distant_Supervision\\Logs\\DistantSupervision.Log";


    // Database
    public static final boolean USERREMOTEDB = true;
    public static final String SERVER_NAME = "localhost";
    public static final int PORT = 27017;
    public static final String DATABASENAME = "distantsupervision";
    public static final String COLLECTION_NAME = "Items";
    public static final String REMOTEURI = "mongodb://admin:admin@ds231987.mlab.com:31987/distantsupervision";

    //Threads
    public static final int DirectoyThreadLimit = 12;

    //
    //public static final int SentenceLimit = 10;
    //public static final int FileLimit = 1500;

    public static final String LabelsLimit = "100";

    //Log
    public static final boolean CONSOLEOUTPUT = true;
}
