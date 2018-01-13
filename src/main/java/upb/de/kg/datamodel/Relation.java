package upb.de.kg.datamodel;

import java.util.List;

public class Relation {

    private String relation;
    private List<Domain> domainList;
    private List<Range> rangeList;


    public Relation(String relationValue) {
        relation = relationValue;
    }

    public void addRangeList(List<Range> rList) {
        rangeList = rList;
    }

    public void addDomainList(List<Domain> rList) {
        domainList = rList;
    }

    public void printAllDomainList() {
        System.out.println("Domain List:");
        for (Domain d : domainList) {
            System.out.println(d.getValue());
        }
    }

    public void printAllRangeList() {
        System.out.println("Range List:");
        for (Range r : rangeList) {
            System.out.println(r.getValue());
        }
    }

    public void printDetails() {
        System.out.println("Relation:" + relation);
        printAllDomainList();
        printAllRangeList();
    }

    @Override
    public String toString() {
        return relation;
    }

    public String getRelationLabel() {
        return relation.replace("dbo:", "");
    }

    public Domain getDomain() {
        return domainList.get(0);
    }

    public Range getRange() {
        return rangeList.get(0);
    }
}
