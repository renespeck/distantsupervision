package upb.de.kg.datamodel;

public class Range {
    private String range;

    public Range(String rangeValue) {
        range = rangeValue;
    }

    public String getValue() {
        return range;
    }

    public boolean equals(Range r) {
        return range.equals(r.getValue());
    }

    @Override
    public String toString() {
        return range;
    }

}
