package upb.de.kg.DataAccessLayer;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.jena.atlas.json.JSON;
import org.bson.Document;
import upb.de.kg.DataModel.JsonModel;

public class DataBaseOperations {
    private static final String dataBaseServerUrl = "localhost";
    private static final int port = 27017;

    private static MongoDatabase createDataBaseConnection() {
        // Creating a Mongo Client
        MongoClient mongo = new MongoClient("localhost", 27017);

        //Creating Credentials
        //MongoCredential credential = MongoCredential.createCredential("","DistantSupervision", null);
        MongoDatabase database = mongo.getDatabase("DistantSupervision");

        return database;
    }

    public static void Insert(JsonModel model) {
        MongoDatabase database = createDataBaseConnection();

        MongoCollection<Document> collection = database.getCollection("Items");


        Gson gson = new Gson();
        String json = gson.toJson(model);

        Document document = Document.parse(json);
        collection.insertOne(document);
    }
}
