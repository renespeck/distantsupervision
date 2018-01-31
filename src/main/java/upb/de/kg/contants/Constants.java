package upb.de.kg.contants;

import java.util.Arrays;
import java.util.List;

// These constants hold DBPedia's predicate which we are interested to make SPARQL query to fetch sample from the DBPedia.
public class Constants {

    public static List<String> DBPediaPredicates = Arrays.asList(
            "dbo:spouse",
            "dbo:child",
            "dbo:parent",
            "dbo:employer",
            "dbo:ceo",
            "dbo:president",
            "dbo:residence",
            "dbo:livingPlace",
            "dbo:subregion",
            "dbo:locatedInArea",
            "dbo:closeTo",
            "dbo:deathPlace",
            "dbo:birthPlace",
            "dbo:placeOfBurial"
    );
}


