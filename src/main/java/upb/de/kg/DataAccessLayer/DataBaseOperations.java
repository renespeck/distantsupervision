package upb.de.kg.DataAccessLayer;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import upb.de.kg.Configuration.Config;
import upb.de.kg.DataModel.JsonModel;
import upb.de.kg.Logger.Logger;

public class DataBaseOperations {

    private MongoDatabase dataBase;

    private MongoDatabase getDataBaseConnection() {
        if (dataBase == null) {
            MongoClient mongo = new MongoClient(Config.SERVER_NAME, Config.PORT);
            dataBase = mongo.getDatabase(Config.DATABASENAME);
        }
        return dataBase;
    }

    public void Insert(JsonModel model) throws Exception {
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
}
