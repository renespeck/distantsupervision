package upb.de.kg.dbpedia;

import upb.de.kg.datamodel.Domain;
import upb.de.kg.datamodel.Range;
import upb.de.kg.datamodel.Relation;
import upb.de.kg.datamodel.ResourcePair;

import java.io.IOException;
import java.util.List;

public interface IDataFetcher {
    List<Domain> getDomainList(Relation relation);

    List<Range> getRangeList(Relation relation);

    List<ResourcePair> getResourcePair(Relation relation) throws IOException;
}
