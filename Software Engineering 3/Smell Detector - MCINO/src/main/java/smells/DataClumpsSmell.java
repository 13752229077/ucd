package smells;

import com.github.javaparser.Position;
import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

// Data Clumps are a group of parameters that are passed around together (this is a smell which should be refactored by the programmer)
public class DataClumpsSmell extends AbstractCodeSmell {
    private final static String smellName = "Data Clumps";

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();

        //this is a hard smell to detect accurately because of the potential variety in parameters passed in
        //Workaround: Assume that high number of parameters to a method is a sign data clumps are possible
        //  Thus if average number of parameters is high, they can likely be grouped into objects
        // Assumption: Class constructors are allowed to have large number of parameters to create the objects

        double avgNumOfParams = 0;
        int maxNumOfParams = 0;

        //loop through all method metrics to find out number of parameters each takes
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            int numOfParams = entry.getValue().getNumOfParams();
            if(numOfParams > maxNumOfParams)
                maxNumOfParams = numOfParams;

            //add to occurrences if number of parameters is greater than 4
            if(numOfParams > 4){
                Optional<Position> methodStart = entry.getValue().getMethodDeclaration().getName().getBegin(); //method start excl annotations
                methodStart.ifPresent(position -> occurrences.add(new Occurrence(position.line, position.line))); //functional style for adding beginning to occurrences if present
            }

            avgNumOfParams += numOfParams;
        }

        avgNumOfParams = (metrics.getNumOfMethods() > 0) ?  avgNumOfParams/metrics.getNumOfMethods() : 0;

        if(avgNumOfParams > 8 || maxNumOfParams > 12){
            //Data clumps are extremely likely to be a problem. Likeliness of methods having 8 parameters on average and not be interdependent is slim
            severity = 3;
        }
        else if(avgNumOfParams > 6 || maxNumOfParams > 10){
            //A high chance data clumps are a problem
            severity = 2;
        }
        else if(avgNumOfParams > 4 || maxNumOfParams > 8){
            //data clumps are not a problem throughout the entire file given small average but select methods may smell of data clumps
            severity = 1;
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
