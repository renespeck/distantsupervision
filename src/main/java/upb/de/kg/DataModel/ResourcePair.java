package upb.de.kg.DataModel;

public class ResourcePair {

    private Resource resourceSrc;
    private Resource resourceTrg;
    private Relation relation;

    public ResourcePair(Resource srcVal, Resource trgVal, Relation relVal)
    {
        resourceSrc = srcVal;
        resourceTrg = trgVal;
        relation = relVal;
    }




}
