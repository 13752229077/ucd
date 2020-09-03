package results;

import java.text.DecimalFormat;
import java.util.HashMap;

/* Class that represents the entire analysis as a whole, storing averages of occurrences and severities */
public class OverallResult {

    private HashMap<String, Double> avgSmellSeverities; //average per smell
    private HashMap<String, Double> avgSmellOccurrences; //average per file

    public OverallResult(HashMap<String, SmellResult> smellResults){
        avgSmellOccurrences = new HashMap<>();
        avgSmellSeverities = new HashMap<>();
        fillAvgHashMaps(smellResults);
    }

    //gather all the smell results
    private void fillAvgHashMaps(HashMap<String,SmellResult> smellResults){

        for(String smell: smellResults.keySet())
        {
            SmellResult sr = smellResults.get(smell);
            avgSmellSeverities.put(sr.getSmellName(), sr.getAverageSeverity());
            avgSmellOccurrences.put(sr.getSmellName(), sr.getAverageOccurrences());
        }
    }

    /* GETTERS */
    public HashMap<String, Double> getOverallSeverities(){
        return avgSmellSeverities;
    }

    public HashMap<String, Double> getOverallOccurrences(){
        return avgSmellOccurrences;
    }

    public String getComments(String smellName){
        DecimalFormat df = new DecimalFormat("0.00");

        double avgSev = avgSmellSeverities.get(smellName);
        double avgOcc = avgSmellOccurrences.get(smellName);

        String result = smellName + " smell has an average severity rating of " + df.format(avgSev) +
                " and average number of occurrences per file is " + df.format(avgOcc) +"\n";
        
        return result;
    }



}
