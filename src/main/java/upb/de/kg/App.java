package upb.de.kg;

import upb.de.kg.DBPedia.Concrete.DBPediaFetcher;
import upb.de.kg.DBPedia.Contants.Constants;
import upb.de.kg.DBPedia.Interfaces.DataFetcher;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.Concrete.WikipediaExtractor;
import upb.de.kg.Extractor.Interface.Extractor;
import upb.de.kg.Logger.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) throws IOException, ParserConfigurationException {

        List<ResourcePair> resourcePairList = traverseLinks();
        Extractor extractor = new WikipediaExtractor(resourcePairList);
        extractor.processData();

    }

    public static List<ResourcePair> traverseLinks() throws IOException {
        // FetchData from DBPedia Source
        DataFetcher dataFetcher = new DBPediaFetcher();
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

}
