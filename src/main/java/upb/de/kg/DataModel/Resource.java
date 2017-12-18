package upb.de.kg.DataModel;

public class Resource {

    private String resource;
    private String label;
    private Relation relation;
    private Domain domain;
    private Range range;

    public Resource (String resoruceValue, Domain domainVal, Range rangeVal, Relation relationVal)
    {
        resource = resoruceValue;
        domain = domainVal;
        range = rangeVal;
        relation = relationVal;
    }

    public Resource (String resoruceVal, Relation relationVal, String labelVal)
    {
        resource = resoruceVal;
        relation = relationVal;
        label = labelVal;
    }


}
