package upb.de.kg.Extractor.Concrete;

import org.apache.jena.ext.com.google.common.collect.Lists;
import upb.de.kg.Configuration.Config;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Extractor.Interface.Extractor;
import upb.de.kg.Logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WikipediaExtractor implements Extractor {


    private List<ResourcePair> resourcePairList;


    private List<File> getDirectoryList(String basePath) {
        File file = new File(basePath);
        File[] directories = file.listFiles();

        return Arrays.asList(directories);
    }

    public WikipediaExtractor(List<ResourcePair> labelsList) {
        resourcePairList = labelsList;
    }


    public void processData() {

        List<File> directoryList = getDirectoryList(Config.WIKIPEDIA_DUMP_PATH);
        List<List<File>> subLists = Lists.partition(directoryList, Config.DirectoyThreadLimit);

        ExecutorService executorService = Executors.newFixedThreadPool(subLists.size());
        List<Callable> processorList = new ArrayList<Callable>();
        try {
            Logger.info("Total partions for processing:" + Integer.toString(subLists.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < subLists.size(); i++) {
            executorService.submit(new DataProcessor(resourcePairList, subLists.get(i)));
        }

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
