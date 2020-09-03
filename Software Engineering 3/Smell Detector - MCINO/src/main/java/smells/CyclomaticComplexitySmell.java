package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import visitors.CyclomaticComplexityVisitor;

import java.util.ArrayList;
import java.util.Map;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    private final static String smellName = "Cyclomatic Complexity";

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();
        //the visitor does the counting for us
        CyclomaticComplexityVisitor ccv;
        float avgCC = 0;

        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            ccv = new CyclomaticComplexityVisitor(entry.getValue().getMethodDeclaration());
            avgCC += ccv.getComplexity();
            if(ccv.getComplexity() > 10){
                occurrences.add(new Occurrence(entry.getValue().getStartLine(), entry.getValue().getEndLine()));
            }
        }
        avgCC = metrics.getNumOfMethods() > 0 ? avgCC/metrics.getNumOfMethods() : 0;

        if(avgCC > 30) //high
            severity = 3;
        else if(avgCC > 20) //medium
            severity = 2;
        else if(avgCC > 10 || !occurrences.isEmpty()) //low
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
