package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;

import java.util.ArrayList;
import java.util.Map;

//detects overly long methods and judges severity based off of the average method length
public class LongMethodsSmell extends AbstractCodeSmell {
    private final static String smellName = "Long Methods";

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();
        float avgMethodLength = 0;

        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            int methodLength = entry.getValue().getNumOfLines();
            if(methodLength > 40){
                occurrences.add(new Occurrence(entry.getValue().getStartLine(), entry.getValue().getEndLine()));
            }

            avgMethodLength += methodLength;
        }
        avgMethodLength = (metrics.getNumOfMethods() > 0) ? avgMethodLength/metrics.getNumOfMethods() : 0;

        if(avgMethodLength > 80)
            severity = 3;
        else if(avgMethodLength > 60)
            severity = 2;
        else if(!occurrences.isEmpty())
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
