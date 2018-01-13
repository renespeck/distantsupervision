package upb.de.kg;

import upb.de.kg.DBPedia.DBPediaFetcher;
import upb.de.kg.Contants.Constants;
import upb.de.kg.DBPedia.IDataFetcher;
import upb.de.kg.DataAccessLayer.DataBaseOperations;
import upb.de.kg.DataAccessLayer.IDataBaseOperations;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.WikipediaExtractor;
import upb.de.kg.Extractor.IExtractor;
import upb.de.kg.Logger.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class App {
    public static void main(String[] args) throws IOException, ParserConfigurationException {

        List<ResourcePair> resourcePairList = fetchLabelsFromDBPedia();
        IExtractor extractor = new WikipediaExtractor(resourcePairList);
        extractor.processData();
        createDataPartions();

    }

    public static List<ResourcePair> fetchLabelsFromDBPedia() throws IOException {
        // FetchData from DBPedia Source
        IDataFetcher dataFetcher = new DBPediaFetcher();
        List<ResourcePair> resourcePairList = new ArrayList<ResourcePair>();

        for (String label : Constants.DBPediaPredicates
                ) {
            Logger.info(String.format("Processing Label:%s ", label));
            Relation relation = new Relation(label);
            relation.addDomainList(dataFetcher.getDomainList(relation));
            relation.addRangeList(dataFetcher.getRangeList(relation));

            resourcePairList.addAll(dataFetcher.getResourcePair(relation));
        }
        return resourcePairList;
    }

    public static void createDataPartions() throws IOException {
        for (String label : Constants.DBPediaPredicates
                ){
            IDataBaseOperations dataBaseOperations = new DataBaseOperations();
            String cleanLabel = label.replace("dbo:","");
            dataBaseOperations.createDataPartitions (cleanLabel);
        }
    }

    public static void copyDataFromRemoteToLocalDB() throws IOException{
        IDataBaseOperations dataBaseOperations = new DataBaseOperations();
        dataBaseOperations.createLocalCopyofRemoteData();
    }

}
