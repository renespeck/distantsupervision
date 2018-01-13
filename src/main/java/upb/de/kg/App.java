package upb.de.kg;

import upb.de.kg.dbpedia.DBPediaFetcher;
import upb.de.kg.contants.Constants;
import upb.de.kg.dbpedia.IDataFetcher;
import upb.de.kg.dataaccesslayer.DataBaseOperations;
import upb.de.kg.dataaccesslayer.IDataBaseOperations;
import upb.de.kg.datamodel.Relation;
import upb.de.kg.datamodel.ResourcePair;
import upb.de.kg.wikiextractor.WikipediaExtractor;
import upb.de.kg.wikiextractor.IExtractor;
import upb.de.kg.logger.Logger;

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
        // FetchData from dbpedia Source
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
