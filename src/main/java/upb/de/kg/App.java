package upb.de.kg;

import org.jsoup.nodes.Document;
import upb.de.kg.DBPedia.Concrete.DBPediaFetcher;
import upb.de.kg.DBPedia.Concrete.HtmlParser;
import upb.de.kg.DBPedia.Contants.Constants;
import upb.de.kg.DBPedia.Interfaces.IDataFetcher;
import upb.de.kg.DBPedia.Interfaces.Parser;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;

import java.io.IOException;
import java.util.List;
import java.util.logging.*;


public class App {
    public static void main(String[] args) throws IOException {

        /* Intialize Logger */
        Logger logger = Logger.getLogger("InfoLogging");
        //FileHandler fh = new FileHandler("D:/DistantSupervision.log");
        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);
        SimpleFormatter formatter = new SimpleFormatter();

        // FetchData from DBPedia Source
        IDataFetcher dataFetcher = new DBPediaFetcher();

        for (String label : Constants.DBPediaLabels
                ) {
            logger.info(String.format("Processing Label:%s ", label));
            logger.fine(String.format("Processing Label:%s ", label));
            Relation relation = new Relation(label);
            relation.AddDomainList(dataFetcher.GetDomainList(relation));
            relation.AddRangeList(dataFetcher.GetRangeList(relation));
            List<ResourcePair> resourcePairList = dataFetcher.GetResourcePair(relation);
        }

    }

    public static void FetchWikiPediaText() throws IOException {
        String wikipediaUrl = "https://en.wikipedia.org/wiki/Berlin";
        Parser parser = new HtmlParser(wikipediaUrl);
        Document doc = parser.Parse();
        System.out.println(doc.text());
    }
}
