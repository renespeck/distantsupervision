package upb.de.kg.Extractor.Concrete;

import upb.de.kg.Configuration.Config;
import upb.de.kg.DataAccessLayer.DataBaseOperations;
import upb.de.kg.DataModel.JsonModel;
import upb.de.kg.DataModel.ResourcePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import static upb.de.kg.Logger.Logger.error;
import static upb.de.kg.Logger.Logger.info;

public class Processor extends Thread {

    private ResourcePair resourcePair;
    private List<File> directoryList;
    private int fileCounter;
    private int sentencesFound;

    public Processor(ResourcePair pair, List<File> subList) {

        directoryList = subList;
        resourcePair = pair;
        fileCounter = 0;
        sentencesFound = 0;
    }

    private void processFile(String fileName) throws Exception {
        BufferedReader br = null;

        br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {

            if (line.contains("<doc"))
                continue;

            BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);

            iterator.setText(line);
            int start = iterator.first();
            for (int end = iterator.next();
                 end != BreakIterator.DONE;
                 start = end, end = iterator.next()) {
                String sentence = line.substring(start, end);

                if (sentence.contains(resourcePair.getSourceResource().getTrimedLabel())
                        && sentence.contains(resourcePair.getTargetResource().getTrimedLabel())) {

                    JsonModel model = new JsonModel();
                    model.setPredicate(resourcePair.getRelation().getRelationLabel());
                    model.setSource(resourcePair.getSourceResource().getTrimedLabel());
                    model.setSourceLabel(resourcePair.getSourceResource().getClassLabel());
                    model.setSourcePosition(sentence.indexOf(resourcePair.getSourceResource().getTrimedLabel()));
                    model.setSentence(sentence);
                    model.setTarget(resourcePair.getTargetResource().getTrimedLabel());
                    model.setTargetLabel(resourcePair.getTargetResource().getClassLabel());
                    model.setTargetPosition(sentence.indexOf(resourcePair.getTargetResource().getTrimedLabel()));

                    sentencesFound++;

                    DataBaseOperations.Insert(model);
                    System.out.println(sentence);

                    if (sentencesFound == Config.SentenceLimit) {
                        break;
                    }

                }
            }
        }
    }

    private void processDirectory(File directory) throws Exception {

        File[] fileList = directory.listFiles();

        for (File file : fileList) {

            processFile(file.getCanonicalPath());
            fileCounter++;

            if ((sentencesFound == Config.SentenceLimit) || (fileCounter == Config.FileLimit)) {
                break;
            }
        }
    }

    public void run() {

        for (File folder : directoryList
                ) {
            try {
                info("Processing folder: " + folder);
                processDirectory(folder);

            } catch (Exception e) {
                try {
                    error(e.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
