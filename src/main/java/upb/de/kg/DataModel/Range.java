package upb.de.kg.DataModel;

public class Range {
    private String range;

    public Range(String rangeValue) {
        range = rangeValue;
    }

    public String GetValue() {
        return range;
    }

    public boolean equals(Range r) {
        return range.equals(r.GetValue());
    }

    @Override
    public String toString() {
        return range;
    }

}
