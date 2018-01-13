package upb.de.kg.datamodel;

public class Resource {

    private String resource;
    private String label;
    private String trimLabel;
    private String rdfsClass;
    private String entityLabel;


    public Resource(String resoruce, String label, String rdfsClass) {
        this.resource = resoruce;
        this.label = label;
        trimLabel = label.replace("@en", "");
        this.rdfsClass = rdfsClass;
        this.entityLabel = rdfsClass.replace("http://dbpedia.org/ontology/", "");
    }

    public String getClassLabel() {
        return entityLabel;
    }

    public String getResource() {
        return resource;
    }

    public String getTrimedLabel() {
        return trimLabel;
    }


    public void printResource() {
        System.out.println("Resource: " + resource);
        System.out.println("Label: " + trimLabel);
        System.out.println("Class: " + rdfsClass);
        System.out.println("Entity: " + entityLabel);
    }


}
