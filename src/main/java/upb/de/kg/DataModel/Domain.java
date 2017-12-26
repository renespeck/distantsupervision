package upb.de.kg.DataModel;

public class Domain {
    private String domain;

    public Domain (String domainValue)
    {
        domain = domainValue;
    }

    public String GetValue ()
    {
        return domain;
    }

    public boolean equals(Domain d)
    {
        return domain.equals(d.GetValue());

    }

    @Override
    public String toString() {
        return domain;
    }
}
