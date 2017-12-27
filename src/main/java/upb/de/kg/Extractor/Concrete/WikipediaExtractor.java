package upb.de.kg.Extractor.Concrete;

import org.apache.jena.ext.com.google.common.collect.Lists;
import upb.de.kg.Configuration.Config;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.Interface.Extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class WikipediaExtractor implements Extractor {


    private ResourcePair resourcePair;

    private List<String> getDirectoryList(String basePath) {
        File file = new File(basePath);
        String[] directories = file.list();

        return Arrays.asList(directories);
    }


    public WikipediaExtractor(ResourcePair pair) {
        resourcePair = pair;
    }


    public void processData(ResourcePair pair) {

        List<String> directoryList = getDirectoryList(Config.WIKIPEDIA_DUMP_PATH);

        int threadCount = Config.ParallelThread;

        double directoryDist = Math.ceil((double) directoryList.size() / (double) threadCount);
        int directoryDistrbution = threadCount;

        List<List<String>> subLists = Lists.partition(directoryList, threadCount);

        for (int i = 0; i < subLists.size(); i++) {


        }

    }


}
