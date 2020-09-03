package smells;

import metrics.FileMetrics;

import java.util.ArrayList;

//Detects overly long classes
public class LongClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Long Class";

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();
        int mainClassLength = metrics.getClassLengths().get(0);

        if(mainClassLength > 1200)
            severity = 3;
        else if(mainClassLength > 1000)
            severity = 2;
        else if(mainClassLength > 800)
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
