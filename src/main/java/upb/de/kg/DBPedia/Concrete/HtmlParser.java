package upb.de.kg.DBPedia.Concrete;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import upb.de.kg.DBPedia.Interfaces.Parser;

import java.io.IOException;

public class HtmlParser implements Parser {

    private String url;

    public HtmlParser(String urlValue) {
        url = urlValue;
    }

    public Document Parse() throws IOException {
        return Jsoup.connect(url).get();
    }
}
