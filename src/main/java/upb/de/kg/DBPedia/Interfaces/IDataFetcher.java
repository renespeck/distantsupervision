package upb.de.kg.DBPedia.Interfaces;

import upb.de.kg.DataModel.Domain;
import upb.de.kg.DataModel.Range;
import upb.de.kg.DataModel.Relation;
import upb.de.kg.DataModel.ResourcePair;

import java.util.List;

public interface IDataFetcher {
    List<Domain> GetDomainList(Relation relation);
    List<Range> GetRangeList (Relation relation);
    List<ResourcePair> GetResourcePair(Relation relation);
}
