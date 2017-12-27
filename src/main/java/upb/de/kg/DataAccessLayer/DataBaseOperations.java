package upb.de.kg.DataAccessLayer;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import upb.de.kg.Configuration.Config;
import upb.de.kg.DataModel.JsonModel;

public class DataBaseOperations {
    private static final String dataBaseServerUrl = "localhost";
    private static final int port = 27017;

    private static MongoDatabase createDataBaseConnection() {
        // Creating a Mongo Client
        MongoClient mongo = new MongoClient(Config.SERVER_NAME, Config.PORT);

        //Creating Credentials
        //MongoCredential credential = MongoCredential.createCredential("","DistantSupervision", null);
        MongoDatabase database = mongo.getDatabase(Config.DATABASENAME);

        return database;
    }

    public static void Insert(JsonModel model) {
        MongoDatabase database = createDataBaseConnection();

        MongoCollection<Document> collection = database.getCollection(Config.COLLECTION_NAME);


        Gson gson = new Gson();
        String json = gson.toJson(model);

        Document document = Document.parse(json);
        collection.insertOne(document);
    }
}
