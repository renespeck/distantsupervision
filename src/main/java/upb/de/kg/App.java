package upb.de.kg;

import upb.de.kg.configuration.Config;
import upb.de.kg.contants.Constants;
import upb.de.kg.dataaccesslayer.DataBaseOperations;
import upb.de.kg.dataaccesslayer.IDataBaseOperations;
import upb.de.kg.datamodel.Relation;
import upb.de.kg.datamodel.ResourcePair;
import upb.de.kg.dbpedia.DBPediaFetcher;
import upb.de.kg.dbpedia.IDataFetcher;
import upb.de.kg.logger.Logger;
import upb.de.kg.wikiextractor.IExtractor;
import upb.de.kg.wikiextractor.WikipediaExtractor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic purpose of this program is to fetch data from DBPedia using SPARQL Queries for given predicates then try to find
 * relevant sentences from the wikipedia dump. Predicates are handpicked and these labels are defined inside constants/Constants.java.
 * App.java is entry point for this program. There are following three methods in it.
 * fetchLabelsFromDBPedia
 * copyDataFromRemoteToLocalDB
 */

public class App {
    public static void main(String[] args) throws IOException, ParserConfigurationException {

        List<ResourcePair> resourcePairList = fetchLabelsFromDBPedia(Constants.DBPediaPredicates);
        extractDataFromWikipedia(resourcePairList, Config.WIKIPEDIA_DUMP_PATH);
        createDataPartions(Constants.DBPediaPredicates);

    }

    /**
     *@param dbpediaPredicatesList -- List of predicates against which pair should be fetched
     *@return pairs
     */
    public static List<ResourcePair> fetchLabelsFromDBPedia(List<String> dbpediaPredicatesList) throws IOException {
        // FetchData from dbpedia Source
        IDataFetcher dataFetcher = new DBPediaFetcher();
        List<ResourcePair> resourcePairList = new ArrayList<ResourcePair>();

        for (String label : dbpediaPredicatesList
                ) {
            Logger.info(String.format("Processing Label:%s ", label));
            Relation relation = new Relation(label);
            relation.addDomainList(dataFetcher.getDomainList(relation));
            relation.addRangeList(dataFetcher.getRangeList(relation));

            resourcePairList.addAll(dataFetcher.getResourcePair(relation));
        }
        return resourcePairList;
    }

    /**
     * This method will
     *@param resourcePairList -- labels pair for which data should be fetched
     *@return void
     */

    public static void extractDataFromWikipedia(List<ResourcePair> resourcePairList, String wikiPediaDumpPath) throws IOException {
        IExtractor extractor = new WikipediaExtractor(resourcePairList, wikiPediaDumpPath);
        extractor.processData();
    }

    /**
     * This method will
     *@param dbpediaPredicatesList -- labels pair for which data partioned
     *@return void
     */
    public static void createDataPartions(List<String> dbpediaPredicatesList) throws IOException {
        for (String label : dbpediaPredicatesList
                ) {
            IDataBaseOperations dataBaseOperations = new DataBaseOperations();
            String cleanLabel = label.replace("dbo:", "");
            dataBaseOperations.createDataPartitions(cleanLabel);
        }
    }


    public static void copyDataFromRemoteToLocalDB() throws IOException {
        IDataBaseOperations dataBaseOperations = new DataBaseOperations();
        dataBaseOperations.createLocalCopyofRemoteData();
    }

}
