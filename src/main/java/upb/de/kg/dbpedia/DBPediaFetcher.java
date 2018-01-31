package upb.de.kg.dbpedia;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import upb.de.kg.configuration.Config;
import upb.de.kg.datamodel.Domain;
import upb.de.kg.datamodel.Range;
import upb.de.kg.datamodel.Relation;
import upb.de.kg.datamodel.ResourcePair;
import upb.de.kg.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
This class contains all methods which are required to make query to DBPedia.
*/

public class DBPediaFetcher implements IDataFetcher {

    private final String OntologyPREFIX = "PREFIX dbo: <http://dbpedia.org/ontology/> ";
    private final String RDFSPREFIX = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
    private final String RDFPREFIX = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";


    private ResultSet executeQuery(String exeQuery) {
        Query query = QueryFactory.create(exeQuery);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

        // Execute.
        ResultSet rs = qexec.execSelect();
        return rs;
    }

    /**
     * This method fetchs list of domains from DBpedia of give relation/predicate.
     * @param relation
     * @return
     */
    public List<Domain> getDomainList(Relation relation) {
        String domainQuery = String.format("%s%s SELECT Distinct ?domain WHERE { %s rdfs:domain ?domain .}", RDFSPREFIX, OntologyPREFIX, relation.toString());

        List<Domain> domainList = new ArrayList<Domain>();

        ResultSet resultSet = executeQuery(domainQuery);
        while (resultSet.hasNext()) {
            QuerySolution soln = resultSet.nextSolution();
            RDFNode x = soln.get("domain");
            String domainStr = x.toString();
            Domain domain = new Domain(domainStr);
            if (!domainList.contains(domain)) {
                domainList.add(domain);
            }
        }
        return domainList;
    }

    /**
     * This method fetchs list of range from DBPedia of given relation/predicate.
     * @param relation
     * @return
     */
    public List<Range> getRangeList(Relation relation) {
        String rangeQuery = String.format("%s%sSELECT Distinct ?range WHERE { %s rdfs:range ?range .}", RDFSPREFIX, OntologyPREFIX, relation.toString());

        ResultSet resultSet = executeQuery(rangeQuery);
        List<Range> rangeList = new ArrayList<Range>();

        while (resultSet.hasNext()) {
            QuerySolution soln = resultSet.nextSolution();
            RDFNode x = soln.get("range");
            String rangeStr = x.toString();
            Range range = new Range(rangeStr);
            if (!rangeList.contains(range)) {
                rangeList.add(range);
            }
        }
        return rangeList;
    }

    /**
     * This method returns ResourcePair of give predicate from DBPedia
     * @param relation
     * @return
     * @throws IOException
     */
    public List<ResourcePair> getResourcePair(Relation relation) throws IOException {

        Logger.info("Query -----------------------------------");
        String labelQuery = String.format("%s%s%s SELECT ?x ?xlabel ?y ?ylabel " +
                        "WHERE {?x %s ?y." +
                        "?x rdfs:label ?xlabel." +
                        "?y rdfs:label ?ylabel. " +
                        "?x rdf:type <%s>. " +
                        "?y rdf:type <%s>. " +
                        "FILTER (langMatches( lang(?xlabel), \"en\" ) ) " +
                        "FILTER (langMatches( lang(?ylabel), \"en\" ) )}" +
                        "LIMIT %s"
                , RDFSPREFIX, OntologyPREFIX, RDFPREFIX, relation.toString(), relation.getDomain(), relation.getRange(), Config.LABELS_LIMIT);


        Logger.info(labelQuery);

        ResultSet resultSet = executeQuery(labelQuery);
        List<ResourcePair> resourceList = new ArrayList<ResourcePair>();

        int count = 0;
        while (resultSet.hasNext()) {
            count++;
            QuerySolution soln = resultSet.nextSolution();

            Resource resourceSrc = soln.getResource("x");
            Resource resourceTarget = soln.getResource("y");
            Literal srcLabel = soln.getLiteral("xlabel");
            Literal trgLabel = soln.getLiteral("ylabel");

            Logger.info(Integer.toString(count) + "-----------------------------------");
            Logger.info("ResourceSource:" + srcLabel);
            Logger.info("ResourceTarget:" + trgLabel);

            upb.de.kg.datamodel.Resource resSrc = new upb.de.kg.datamodel.Resource(resourceSrc.toString(), srcLabel.toString(), relation.getDomain().toString());
            upb.de.kg.datamodel.Resource trgSrc = new upb.de.kg.datamodel.Resource(resourceTarget.toString(), trgLabel.toString(), relation.getRange().toString());

            ResourcePair resourcePair = new ResourcePair(resSrc, trgSrc, relation);
            resourceList.add(resourcePair);
        }
        return resourceList;

    }


}
