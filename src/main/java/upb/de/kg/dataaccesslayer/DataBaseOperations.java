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

    public void createDataPartitions(String relation) {
        try {
            MongoDatabase database = getDataBaseConnection();
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
}
