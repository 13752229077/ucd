package smells;

import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import results.SmellResult;
import utils.Setup;

import java.util.HashMap;

public class SmellDetector {

    private FileMetrics[] metrics;
    private CompilationUnit[] CUs;
    private AbstractCodeSmell[] smells;
    private HashMap<String, SmellResult> smellResults;
    private HashMap<String,String> sourceFiles;

    public SmellDetector(String dirPath){
        Setup setup = new Setup(dirPath);
        CUs = setup.run();
        sourceFiles = setup.getSourceFiles();
        metrics = new FileMetrics[CUs.length];
        getMetrics();
        instantiateSmells();
    }

    private void instantiateSmells(){
        smells = new AbstractCodeSmell[9];
        smells[0] = new LongMethodsSmell();
        smells[1] = new LongClassSmell();
        smells[2] = new CyclomaticComplexitySmell();
        smells[3] = new DataClumpsSmell();
        smells[4] = new DuplicatedCodeSmell(metrics);
        smells[5] = new FeatureEnvySmell();
        smells[6] = new HeavyCommentingSmell();
        smells[7] = new LazyClassSmell(metrics);
        smells[8] = new RefusedBequestSmell(CUs);

    }

    private void getMetrics(){
        for(int i=0; i<metrics.length; i++){
            metrics[i] = new FileMetrics(CUs[i]);
        }
    }

    public void detectSmells() {
        smellResults = new HashMap<>();
        for(AbstractCodeSmell smell : smells){
            SmellResult result = new SmellResult(smell.getSmellName());

            for(FileMetrics fm : metrics) {
                smell.detectSmell(fm);
                result.addOccurrences(fm.getClassNames().get(0), smell.getOccurrences());
                result.addSeverity(fm.getClassNames().get(0), smell.getSeverity());
                smellResults.put(smell.getSmellName(),result);
            }
        }
    }

    public HashMap<String, SmellResult> getSmellResults(){
        return smellResults;
    }

    public AbstractCodeSmell[] getSmells(){
        return smells;
    }

    public CompilationUnit[] getCUs()
    {
        return CUs;
    }

    public HashMap<String,String> getSourceFiles()
    {
        return this.sourceFiles;
    }

}