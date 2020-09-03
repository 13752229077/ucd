package utils;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class SetupTest {

    private static Setup setup;
    private static String[] javaFiles;

    @BeforeClass
    public static void init()
    {
        setup = new Setup(System.getProperty("user.dir"));
        javaFiles = new String[]{"DataClumpsSmell","FileMetrics","FieldCollector","MethodMetrics","SyntaxHighlighter","HTMLUtil","UI","ClassLengthVisitor","SmellResult","Resultable","MethodDependentVarsVisitor","SmellDetector","MethodCallVisitor","Setup","Occurrence","CyclomaticComplexitySmell","HeavyCommentingSmell","CyclomaticComplexityVisitor","FeatureEnvySmell","Comments","OverallResult","FeatureEnvyVisitor","DuplicatedCodeSmell","LazyClassSmell","RefusedBequestSmell","RabinKarp","LongMethodsSmell","LongClassSmell","Main","AbstractCodeSmell"};
    }

   @Test
   public void findJavaFiles()
   {
       int i =0;
       for(String currFile: setup.getSourceFiles().keySet()){
            assertEquals(javaFiles[i],currFile);
            i++;
       }
   }
}
