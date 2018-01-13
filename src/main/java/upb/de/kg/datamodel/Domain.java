package upb.de.kg.datamodel;

public class Domain {
    private String domain;

    public Domain(String domainValue) {
        domain = domainValue;
    }

    public String getValue() {
        return domain;
    }

    public boolean equals(Domain d) {
        return domain.equals(d.getValue());

    }

    @Override
    public String toString() {
        return domain;
    }
}
