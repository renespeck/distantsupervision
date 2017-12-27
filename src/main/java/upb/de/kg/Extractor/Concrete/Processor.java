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

public class Processor extends Thread {

    private ResourcePair resourcePair;
    private List<String> directoryList;
    private int fileCounter;
    private int sentencesFound;

    public Processor(ResourcePair pair, List<String> subList) {

        directoryList = subList;
        resourcePair = pair;
        fileCounter = 0;
        sentencesFound = 0;
    }

    private void processFile(String fileName) throws IOException {
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
                    model.setSource(resourcePair.getSourceResource().getTrimedLabel());
                    //model.setSourceLabel(resourcePair.getSourceResource().);
                    model.setSentence(sentence);

                    sentencesFound++;

                    if (sentencesFound == Config.SentenceLimit) {
                        break;
                    }

                }
            }
        }
    }

    private void processDirectory(String directory) throws IOException {

        File file = new File(directory);
        String[] fileList = file.list();

        for (int i = 0; i < fileList.length; i++) {

            processFile(fileList[i]);
            fileCounter++;

            if ((sentencesFound == Config.SentenceLimit) || (fileCounter == Config.FileLimit)) {
                break;
            }
        }
    }

    public void run() {

        for (String folder : directoryList
                ) {
            try {
                processDirectory(folder);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
