package upb.de.kg.DataAccessLayer;

import upb.de.kg.DataModel.JsonModel;

public interface  IDataBaseOperations {
    void insert(JsonModel model) throws Exception;

    void createDataPartitions(String relation);

    void createLocalCopyofRemoteData ();

}
