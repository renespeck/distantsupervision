package upb.de.kg.Extractor.Concrete;

import org.apache.jena.ext.com.google.common.collect.Lists;
import upb.de.kg.Configuration.Config;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.Interface.Extractor;
import upb.de.kg.Logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WikipediaExtractor implements Extractor {


    private ResourcePair resourcePair;

    private List<File> getDirectoryList(String basePath) {
        File file = new File(basePath);
        File[] directories = file.listFiles();

        return Arrays.asList(directories);
    }

    public WikipediaExtractor(ResourcePair pair) {
        resourcePair = pair;
    }


    public void processData() {

        List<File> directoryList = getDirectoryList(Config.WIKIPEDIA_DUMP_PATH);

        int threadCount = Config.ParallelThread;

        double directoryDist = Math.ceil((double) directoryList.size() / (double) threadCount);
        int directoryDistrbution = threadCount;

        List<List<File>> subLists = Lists.partition(directoryList, threadCount);

        try {
            Logger.info("Total Partions for processing:" + Integer.toString(subLists.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < subLists.size(); i++) {
            Processor process = new Processor(resourcePair, subLists.get(i));
            process.run();
        }

    }

}
