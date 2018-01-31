package upb.de.kg.dataaccesslayer;

import upb.de.kg.datamodel.JsonModel;

public interface  IDataBaseOperations {
    void insert(JsonModel model) throws Exception;

    void createDataPartitions(String relation);

    void createLocalCopyofRemoteData ();

    void copyDataFromDifferentSources ();

}
