package upb.de.kg.DBPedia.Interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface Parser {
    Document parse() throws IOException;
}
