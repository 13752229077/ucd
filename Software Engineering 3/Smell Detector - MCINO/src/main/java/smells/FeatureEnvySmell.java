package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import visitors.FeatureEnvyVisitor;

import java.util.ArrayList;
import java.util.Map;

public class FeatureEnvySmell extends AbstractCodeSmell{
    private final static String smellName = "Feature Envy";

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();
        int numOfProblematicMethods = 0;

        //use a feature envy visitor on method metrics to find number of methods with potential feature envy
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            FeatureEnvyVisitor v = new FeatureEnvyVisitor(entry.getValue().getMethodDeclaration());

            //check if method is problematic based on its field accesses
            if(v.getNumOfFieldAccesses() > 8 && (v.getNumOfFieldAccesses()*1.0 / entry.getValue().getNumOfStatements() > 0.75)){
                numOfProblematicMethods++;
                occurrences.add(new Occurrence(entry.getValue().getStartLine(), entry.getValue().getEndLine()));
            }
        }

        //if more than 4 methods perform computations on outside data or average number of problematic methods is greater than 50%
        if(numOfProblematicMethods > 4 || numOfProblematicMethods*1.0/metrics.getNumOfMethods() > 0.5){
            severity = 3;
        }
        //if more than 2 methods perform computations on outside data or average number of problematic methods is greater than 30%
        else if(numOfProblematicMethods > 2 || numOfProblematicMethods*1.0/metrics.getNumOfMethods() > 0.3){
            severity = 2;
        }
        //if at least 1 method performs too much computatations on outside data
        else if(numOfProblematicMethods > 0){
            severity = 1;
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
