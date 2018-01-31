package upb.de.kg.wikiextractor;

import org.apache.jena.ext.com.google.common.collect.Lists;
import upb.de.kg.configuration.Config;
import upb.de.kg.datamodel.ResourcePair;
import upb.de.kg.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WikipediaExtractor implements IExtractor {

    private List<ResourcePair> resourcePairList;
    private String wikipediaDumpPath;

    private List<File> getDirectoryList(String basePath) {
        File file = new File(basePath);
        File[] directories = file.listFiles();

        return Arrays.asList(directories);
    }

    public WikipediaExtractor(List<ResourcePair> labelsList, String wikipediaPath) {
        this.wikipediaDumpPath = wikipediaPath;
        resourcePairList = labelsList;
    }

    public void processData() throws IOException {

        List<File> directoryList = getDirectoryList(Config.WIKIPEDIA_DUMP_PATH);
        List<List<File>> subLists = Lists.partition(directoryList, Config.DIRECTORY_THREAD_LIMIT);

        ExecutorService executorService = Executors.newFixedThreadPool(subLists.size());
        try {
            Logger.info("Total partions for processing:" + Integer.toString(subLists.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < subLists.size(); i++) {
            Logger.info("Thread:" + i + " Created");
            executorService.submit(new DataProcessor(resourcePairList, subLists.get(i)));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
