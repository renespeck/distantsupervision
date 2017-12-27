package upb.de.kg;

import org.jsoup.nodes.Document;
import upb.de.kg.DBPedia.Concrete.DBPediaFetcher;
import upb.de.kg.DBPedia.Concrete.HtmlParser;
import upb.de.kg.DBPedia.Contants.Constants;
import upb.de.kg.DBPedia.Interfaces.IDataFetcher;
import upb.de.kg.DBPedia.Interfaces.Parser;
import upb.de.kg.DataAccessLayer.DataBaseOperations;
import upb.de.kg.DataModel.JsonModel;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.Concrete.WikipediaExtractor;
import upb.de.kg.Extractor.Interface.Extractor;
import upb.de.kg.Logger.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


public class App {
    public static void main(String[] args) throws IOException, ParserConfigurationException {

        traverseLinks();

    }

    public static void insertDatabase() throws IOException {
        for (int i = 0; i < 2; i++) {

            JsonModel jsonModel = new JsonModel();
            jsonModel.setSource("Barck Obama" + Integer.toString(i));
            jsonModel.setSourceLabel("Person");
            jsonModel.setSourcePosition(0);
            jsonModel.setSentence("Barack Obama was the president of the USA.");
            jsonModel.setSentencePortion("was the president of");
            jsonModel.setTargetLabel("Location");
            jsonModel.setTargetPosition(28);


            try {
                DataBaseOperations.Insert(jsonModel);
            } catch (Exception e) {
                Logger.error(e.toString());
            }
        }
    }

    public static void traverseLinks() throws IOException {
        // FetchData from DBPedia Source
        IDataFetcher dataFetcher = new DBPediaFetcher();

        for (String label : Constants.DBPediaLabels
                ) {
            Logger.info(String.format("Processing Label:%s ", label));
            Relation relation = new Relation(label);
            relation.addDomainList(dataFetcher.getDomainList(relation));
            relation.addRangeList(dataFetcher.getRangeList(relation));

            //relation.printDetails();

            List<ResourcePair> resourcePairList = dataFetcher.getResourcePair(relation);

            for (ResourcePair pair : resourcePairList
                    ) {
                Extractor extractor = new WikipediaExtractor(pair);
                extractor.processData();
                pair.printDetails();
            }
        }
    }

    public static void fetchWikiPediaText() throws IOException {
        String wikipediaUrl = "https://en.wikipedia.org/wiki/Berlin";
        Parser parser = new HtmlParser(wikipediaUrl);
        Document doc = parser.parse();
        System.out.println(doc.text());
    }
}
