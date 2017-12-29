package upb.de.kg.Extractor.Concrete;

import upb.de.kg.DataAccessLayer.DataBaseOperations;
import upb.de.kg.DataModel.JsonModel;
import upb.de.kg.DataModel.ResourcePair;
import upb.de.kg.Logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

public class DataProcessor implements Runnable {

    private List<ResourcePair> resourcePairList;
    private List<File> directoryList;
    private static int fileCounter = 0;
    private DataBaseOperations dbBaseOperations;

    public DataProcessor(List<ResourcePair> labelsList, List<File> subList) {

        directoryList = subList;
        resourcePairList = labelsList;
        dbBaseOperations = new DataBaseOperations();
        //fileCounter = 0;
    }

    private String getSubSentence(String sentence, int srcIndex, int srcLength, int trgIndex, int trgLength) {
        if (srcIndex == trgIndex)
            return "";

        if (srcIndex < trgIndex)
            return sentence.substring(srcIndex + srcLength, trgIndex);
        else
            return sentence.substring(trgIndex + trgLength, srcIndex);
    }

    private void processSentence(String sentence, ResourcePair resourcePair) throws Exception {

        int srcStartIndex = sentence.indexOf(resourcePair.getSourceResource().getTrimedLabel());
        int trgStartIndex = sentence.indexOf(resourcePair.getTargetResource().getTrimedLabel());
        try {

            if (sentence.contains(resourcePair.getSourceResource().getTrimedLabel())
                    && sentence.contains(resourcePair.getTargetResource().getTrimedLabel())) {

                JsonModel model = new JsonModel();


                model.setPredicate(resourcePair.getRelation().getRelationLabel());
                model.setSource(resourcePair.getSourceResource().getTrimedLabel());
                model.setSourceLabel(resourcePair.getSourceResource().getClassLabel());
                model.setSourcePosition(srcStartIndex);

                model.setSentence(sentence);

                model.setTarget(resourcePair.getTargetResource().getTrimedLabel());
                model.setTargetLabel(resourcePair.getTargetResource().getClassLabel());
                model.setTargetPosition(trgStartIndex);

                int srcLength = resourcePair.getSourceResource().getTrimedLabel().length();
                int trgLength = resourcePair.getTargetResource().getTrimedLabel().length();

                String subSentence = getSubSentence(sentence, srcStartIndex, srcLength, trgStartIndex, trgLength);

                model.setSentencePortion(subSentence);

                dbBaseOperations.Insert(model);
                System.out.println(sentence);
            }
        } catch (Exception ex) {
            Logger.info("Sentence:" + sentence);
            Logger.info("StartIndex:" + srcStartIndex);
            Logger.info("EndIndex:" + trgStartIndex);
            Logger.error(ex.toString());
        }
    }

    private void processFile(String fileName, String directoryName) throws Exception {

        Logger.info(String.format("%s Processing File: %s in Directory: %s", fileCounter, fileName, directoryName));

        BufferedReader br = null;
        br = new BufferedReader(new FileReader(fileName));

        // ReadLine
        String line;
        while ((line = br.readLine()) != null) {

            if (line.contains("<doc"))
                continue;

            // Break line into multiple sentences
            BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);

            iterator.setText(line);
            int start = iterator.first();
            for (int end = iterator.next();
                 end != BreakIterator.DONE;
                 start = end, end = iterator.next()) {
                String sentence = line.substring(start, end);

                for (ResourcePair resourcePair : resourcePairList
                        ) {
                    processSentence(sentence, resourcePair);
                }

            }
        }
    }

    private void processDirectory(File directory) throws Exception {

        File[] fileList = directory.listFiles();
        for (File file : fileList) {

            processFile(file.getCanonicalPath(), directory.getName());
            fileCounter++;
        }
        Logger.info(String.format("Folder:%s Processing Finished", directory.getName()));
    }

    public void run() {

        for (File folder : directoryList
                ) {
            try {
                Logger.info("Processing folder: " + folder);
                processDirectory(folder);

            } catch (Exception e) {

                try {
                    Logger.error(e.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            Logger.info("List Processing Finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
