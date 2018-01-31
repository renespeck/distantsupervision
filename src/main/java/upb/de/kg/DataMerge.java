package upb.de.kg;

import upb.de.kg.contants.Constants;
import upb.de.kg.dataaccesslayer.DataBaseOperations;
import upb.de.kg.dataaccesslayer.IDataBaseOperations;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DataMerge {

    public static void main(String[] args) throws IOException, ParserConfigurationException {
        for (String label : Constants.DBPediaPredicates
                ) {
            IDataBaseOperations dataBaseOperations = new DataBaseOperations();
            String cleanLabel = label.replace("dbo:", "");
            dataBaseOperations.createDataPartitions(cleanLabel);
        }


    }
}
