package upb.de.kg.datamodel;

public class ResourcePair {

    private Resource resourceSrc;
    private Resource resourceTrg;
    private Relation relation;

    public ResourcePair(Resource srcVal, Resource trgVal, Relation relVal) {
        resourceSrc = srcVal;
        resourceTrg = trgVal;
        relation = relVal;
    }

    public void printDetails() {
        System.out.println("Resource Pair:");
        System.out.println("Source:");
        resourceSrc.printResource();

        System.out.println("Target:");
        resourceTrg.printResource();
    }

    public Relation getRelation() {
        return relation;
    }

    public Resource getSourceResource() {
        return resourceSrc;
    }

    public Resource getTargetResource() {
        return resourceTrg;
    }
}
