package upb.de.kg.DBPedia.Concrete;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import upb.de.kg.DataModel.Domain;
import upb.de.kg.DataModel.Range;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.DBPedia.Interfaces.IDataFetcher;
import upb.de.kg.DataModel.Relation;

import java.util.ArrayList;
import java.util.List;

public class DBPediaFetcher implements IDataFetcher {

    private static final int LIMIT = 10;
    private static final String OntologyPREFIX = "PREFIX dbo: <http://dbpedia.org/ontology/> ";
    private static final String RDFSPREFIX = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";

    /// Execute Query on the DBPedia Source
    private  ResultSet ExecuteQuery(String exeQuery) {
        Query query = QueryFactory.create(exeQuery);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

        // Execute.
        ResultSet rs = qexec.execSelect();
        return rs;
    }

    public List<Domain> GetDomainList(Relation relation) {
        String domainQuery = String.format("%s%s SELECT Distinct ?domain WHERE { %s rdfs:domain ?domain .}", RDFSPREFIX, OntologyPREFIX, relation.toString());

        List<Domain> domainList = new ArrayList<Domain>();

        ResultSet resultSet = ExecuteQuery(domainQuery);
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

    public List<Range> GetRangeList (Relation relation) {
        String rangeQuery = String.format("%s%sSELECT Distinct ?range WHERE { %s rdfs:range ?range .}", RDFSPREFIX, OntologyPREFIX, relation.toString());

        ResultSet resultSet = ExecuteQuery(rangeQuery);
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

    public List<ResourcePair> GetResourcePair(Relation relation) {
        //String rangeQuery = String.format("%s%s SELECT ?source ?target WHERE {?source dbo:spouse ?target} LIMIT %d", RDFSPREFIX, OntologyPREFIX, LIMIT);

        String labelQuery = String.format("%s%s SELECT ?source ?xlabel ?target ?ylabel " +
                        "WHERE {?x %s ?y.?x rdfs:label ?xlabel.?y rdfs:label ?ylabel. " +
                        "FILTER (langMatches( lang(?srclabel), \"en\" ) ) " +
                        "FILTER (langMatches( lang(?trglabel), \"en\" ) )}"
                , RDFSPREFIX, OntologyPREFIX, relation.toString());


        ResultSet resultSet = ExecuteQuery(labelQuery);
        List<ResourcePair> resourceList = new ArrayList<ResourcePair>();

        while (resultSet.hasNext()) {
            QuerySolution soln = resultSet.nextSolution();

            Resource resourceSrc = soln.getResource("source");
            Resource resourceTarget = soln.getResource("target");
            Literal srcLabel = soln.getLiteral("srclabel");
            Literal trgLabel = soln.getLiteral("trglabel");

            upb.de.kg.DataModel.Resource resSrc = new upb.de.kg.DataModel.Resource(resourceSrc.toString(), relation, srcLabel.toString());
            upb.de.kg.DataModel.Resource trgSrc = new upb.de.kg.DataModel.Resource(resourceSrc.toString(), relation, trgLabel.toString());

            ResourcePair resourcePair = new ResourcePair(resSrc, trgSrc, relation);
        }
        return resourceList;

    }


}
