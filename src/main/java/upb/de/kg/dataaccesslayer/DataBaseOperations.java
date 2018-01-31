package upb.de.kg.dataaccesslayer;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import upb.de.kg.configuration.Config;
import upb.de.kg.datamodel.JsonModel;
import upb.de.kg.logger.Logger;

import java.util.ArrayList;
import java.util.List;

//This Class contains all method which are required to communicate with Mongo DB. Configuration related to Database is defined
// in the Config.java file

public class DataBaseOperations implements IDataBaseOperations {

    private MongoDatabase dataBase;

    private MongoDatabase getDataBaseConnection() {

        if (dataBase == null) {

            if (Config.USE_REMOTE_DB) {
                dataBase = getRemoteDataBaseConnection();
            } else {
                dataBase = getLocalDataBaseConnection();
            }
        }
        return dataBase;
    }

    private MongoDatabase getLocalDataBaseConnection(){
        MongoClient mongo = new MongoClient(Config.SERVER_NAME, Config.PORT);
        return  mongo.getDatabase(Config.lOCAL_DATABASE_NAME);
    }

    private MongoDatabase getRemoteDataBaseConnection(){
        MongoClientURI uri = new MongoClientURI(Config.REMOTE_URI);
        MongoClient client = new MongoClient(uri);
       return client.getDatabase(uri.getDatabase());
    }


    private MongoDatabase getRemoteDataBaseConnectionSecondary(){
        MongoClientURI uri = new MongoClientURI("mongodb://admin:admin@ds113738.mlab.com:13738/relationextraction");
        MongoClient client = new MongoClient(uri);
        return client.getDatabase(uri.getDatabase());
    }

    /**
     *This mehtod takes one model which contains relevant data to training data and insert into Mongo DB.
     * @param model
     * @throws Exception
     */

    public void insert(JsonModel model) throws Exception {
        try {
            MongoDatabase database = getDataBaseConnection();

            MongoCollection<Document> collection = database.getCollection(Config.COLLECTION_NAME);

            Gson gson = new Gson();
            String json = gson.toJson(model);

            Document document = Document.parse(json);
            collection.insertOne(document);
        } catch (Exception ex) {
            Logger.error(ex.toString());
            throw ex;
        }
    }

    /**
     * This mehtod splits DataSet into two equal parts, One is training data and other is test data based on
     * the give predicate.
     * @param relation
     */
    public void createDataPartitions(String relation) {
        try {
            MongoDatabase database = getRemoteDataBaseConnectionSecondary();
            MongoCollection<Document> collection = database.getCollection(Config.COLLECTION_NAME);
            Bson filter = Filters.eq("predicate", relation);

            List<Document> documentsList = collection.find(filter).into(new ArrayList<Document>());

            int distribute = 0;
            for (Document document :
                    documentsList) {

                if (distribute % 2 == 0) {
                    MongoCollection<Document> trainingCollection = database.getCollection(Config.TRAINING_COLLECTION_NAME);
                    trainingCollection.insertOne(document);
                }
                else{
                    MongoCollection<Document> testCollection = database.getCollection(Config.TEST_COLLECTION_NAME);
                    testCollection.insertOne(document);
                }
                distribute++;
            }
        }
        catch (Exception ex) {
        }
    }

    //Only Use this method when you need to copy remote data into local database
    public void createLocalCopyofRemoteData ()
    {
    MongoCollection<Document> remoteCollection = getRemoteDataBaseConnection().getCollection("DataSet");
    MongoCollection<Document> localCollection = getLocalDataBaseConnection().getCollection("DataSet");

    List<Document> documentList = remoteCollection.find().into(new ArrayList<Document>());
    localCollection.insertMany(documentList);
    }

    public void copyDataFromDifferentSources ()
    {
    MongoCollection<Document> remoteSrcCollectionFirst = getRemoteDataBaseConnection().getCollection("DataSet");
    MongoCollection<Document> remoteSrcCollectionSecond = getRemoteDataBaseConnection().getCollection("DataSet-SecondIteration");

    MongoCollection<Document> targetedCollection = getRemoteDataBaseConnectionSecondary().getCollection("DataSet");


    List<Document> documentListFirst = remoteSrcCollectionFirst.find().into(new ArrayList<Document>());
    List<Document> documentListSecond = remoteSrcCollectionSecond.find().into(new ArrayList<Document>());

    targetedCollection.insertMany(documentListFirst);
    targetedCollection.insertMany(documentListSecond);
    }
}
