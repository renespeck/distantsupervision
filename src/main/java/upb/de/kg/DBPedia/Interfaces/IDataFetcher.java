package upb.de.kg.DBPedia.Interfaces;

import upb.de.kg.DataModel.Domain;
import upb.de.kg.DataModel.Range;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;

import java.io.IOException;
import java.util.List;

public interface IDataFetcher {
    List<Domain> getDomainList(Relation relation);
    List<Range> getRangeList (Relation relation);
    List<ResourcePair> getResourcePair(Relation relation) throws IOException;
}
