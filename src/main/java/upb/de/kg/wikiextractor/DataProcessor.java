package upb.de.kg.wikiextractor;

import upb.de.kg.dataaccesslayer.DataBaseOperations;
import upb.de.kg.datamodel.JsonModel;
import upb.de.kg.datamodel.ResourcePair;
import upb.de.kg.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 class DataProcessor implements Runnable {

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

    private boolean isContain(String source, String subItem) {

        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    private void processSentence(String sentence, ResourcePair resourcePair) throws Exception {
        try {

            if (true == isContain(sentence, resourcePair.getSourceResource().getTrimedLabel())
                    && true == isContain(sentence, resourcePair.getTargetResource().getTrimedLabel())) {

                JsonModel model = new JsonModel();

                int srcStartIndex = sentence.indexOf(resourcePair.getSourceResource().getTrimedLabel());
                int trgStartIndex = sentence.indexOf(resourcePair.getTargetResource().getTrimedLabel());

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

                dbBaseOperations.insert(model);
                System.out.println(sentence);
            }
        } catch (Exception ex) {
            Logger.info("Sentence:" + sentence);
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
