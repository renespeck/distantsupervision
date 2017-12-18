package upb.de.kg.DataModel;

import java.util.List;

public class Relation {

    private String relation;
    private List<Domain> domainList;
    private List<Range>  rangeList;


    public Relation (String relationValue)
    {
        relation = relationValue;
    }

    public void AddRangeList (List<Range> rList)
    {
        rangeList = rList;
    }

    public void AddDomainList (List<Domain> rList)
    {
        domainList = rList;
    }

    @Override
    public String toString() {
        return relation;
    }
}
