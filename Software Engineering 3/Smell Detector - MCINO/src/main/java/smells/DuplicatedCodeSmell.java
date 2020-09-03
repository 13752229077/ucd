package smells;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import utils.RabinKarp;

public class DuplicatedCodeSmell extends AbstractCodeSmell {

    private static int NO_BLOCKS_TO_CHECK = 3;
    private final static String smellName = "Duplicated Code";
    private String text;
    private String file;
    private List<String> methodNames;
    private HashMap methods;
    private RabinKarp rk;
    private FileMetrics[] fileMetrics;
    private int methodStartLine;
    private int methodEndLine;

    /** Constructor that takes all files so duplications can be checked across all files */
    DuplicatedCodeSmell(FileMetrics[] metrics){ fileMetrics = metrics; }

    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        occurrences = new ArrayList<>();
        if(getFieldsAndMethods(metrics)) findSmells();

    }
    /** instantiate file name, local HashMap of methods and List of method names
     * @param metrics the current file to analyze
     * @return true if file has methods i.e. is not annotation type**/
    private boolean getFieldsAndMethods(FileMetrics metrics){
        file = metrics.getClassNames().get(0);
        methodNames = new ArrayList<>();
        if(!metrics.getMethodsMetrics().keySet().isEmpty() ) {
            methods = metrics.getMethodsMetrics();
            Collection<String> collection = metrics.getMethodsMetrics().keySet();
            methodNames.addAll(collection);
            return true;
        }
        return false;
    }

    /** iterates through each method, removing it from the local HashMap
     * and if not Abstract, call findBlockDuplicates() on it's body **/
    private void findSmells() {
        MethodMetrics aMethod;
        while(!methods.isEmpty()){
            aMethod = (MethodMetrics) methods.remove(methodNames.get(0));
            methodNames.remove(methodNames.get(0));
            if (!aMethod.getModifiers().contains(Modifier.abstractModifier()) && aMethod.getNumOfLines() > 0) {
                findBlockDuplicates(aMethod.getBody());
            }
        }
    }

    /** @param block the block to be checked for duplicates
     *   if this blocks number of statements are >= NO_BLOCKS_TO_CHECK
     *  then for each block of size = NO_BLOCKS_TO_CHECK, check against:
     *  1. The rest of the method body if its number of statements >= 2*NO_BLOCKS_TO_CHECK (i.e. duplicate in method)
     *  and/or
     *  2. The rest of the methods in the file.
     *   **/
    private void findBlockDuplicates(BlockStmt block){
        int size = block.getStatements().size();
        if(size >= NO_BLOCKS_TO_CHECK) {
            // Iterate through blocks of |NO_BLOCKS_TO_CHECK|
            for(int endIndex = 0; endIndex<size- NO_BLOCKS_TO_CHECK; endIndex+=3) {
                // combine statements, removing comments and whitespace
                String pattern = combineStatements(block, endIndex);
                if(block.getStatement(endIndex).getBegin().isPresent()
                        && block.getStatement(endIndex + (NO_BLOCKS_TO_CHECK - 1)).getEnd().isPresent()) {
                    methodStartLine = block.getStatement(endIndex).getBegin().get().line;
                    methodEndLine = block.getStatement(endIndex + (NO_BLOCKS_TO_CHECK - 1)).getEnd().get().line;
                }
                // instantiate RabinKarp setting the pattern to compare to
                rk = new RabinKarp(pattern);
                // If method size is big enough and we haven't reached last place the duplicate could exist
                if(size >= (2*NO_BLOCKS_TO_CHECK) && endIndex < (size-(2* NO_BLOCKS_TO_CHECK -1)) ) {
                    // for each block of |NO_BLOCKS_TO_CHECK| remaining
                    for (int startIndex = endIndex + NO_BLOCKS_TO_CHECK; startIndex < size - (NO_BLOCKS_TO_CHECK -1); startIndex++) {
                        // combine statements, removing comments and whitespace
                        text = combineStatements(block, startIndex);
                        // search text for instance of pattern returning location of first character
                        searchPattern(pattern, text, block, startIndex, file);
                    }
                }
                // check file's other methods for pattern
                compareWithOtherMethods(pattern);
                // check all other file's methods for pattern
                compareWithOtherFiles(pattern);
            }
        }
    }

    /**This method checks all remaining methods contained in the same file for a duplicate of pattern.
     * @param pattern is the pattern we're searching for **/
    private void compareWithOtherMethods(String pattern){
        for (String methodName : methodNames) {
            MethodMetrics methodMetric = (MethodMetrics) methods.get(methodName);
            checkMethod(file, pattern, methodMetric);
        }
    }

    /** This method checks all methods contained in all other files for a duplicate of pattern.
     * @param  pattern is the pattern we're searching for */
    private void compareWithOtherFiles(String pattern){
        for(FileMetrics fileMetric: fileMetrics){
            String name = fileMetric.getClassNames().get(0);
            if(!name.equals(file)) {
                HashMap<String, MethodMetrics> methodMetrics = fileMetric.getMethodsMetrics();
                for (Map.Entry<String, MethodMetrics> entry : methodMetrics.entrySet()) {
                    checkMethod(name, pattern, entry.getValue());
                }
            }
        }
    }

    /** This method checks a MethodMetric for an instance of pattern by
     * calling searchPattern on sequential blocks in methodMetric
     * @param file java file name of methodMetric
     * @param pattern  is the pattern we're searching for
     * @param methodMetric method we're inspecting for pattern*/
    private void checkMethod(String file, String pattern, MethodMetrics methodMetric){
        BlockStmt aBody = methodMetric.getBody();
        int size = methodMetric.getNumOfStatements();
        if (size >= NO_BLOCKS_TO_CHECK) {
            for(int i = 0; i < size - NO_BLOCKS_TO_CHECK;i++) {
                text = combineStatements(aBody, i);
                searchPattern(pattern, text, aBody, i, file);
            }
        }
    }

    /** This method searches a text for an instance of pattern by using the RabinKarp algorithm
     *  If a match is found (result = position of 1st matching char.) then we check for exact equality
     *  and if the match is 100%, we add both occurrences and link them.
     *  @param pattern  is the pattern we're searching for
     *  @param text is the text we're searching in
     *  @param block is the block containing text (needed for line calculation)
     *  @param offset is the starting index of the statements of text
     *  @param name is the file name of text */
    private void searchPattern(String pattern, String text, BlockStmt block, int offset, String name){
        int result = rk.search(text);
        if (result > -1) {
            double similar = getSimilarityPercentage(pattern, text.substring(result, result + pattern.length()));
            if (similar == 100.0) {
                int startLine, endLine;
                if(block.getStatement(offset).getRange().isPresent() && block.getStatement(offset+NO_BLOCKS_TO_CHECK-1).getRange().isPresent()) {
                    startLine = block.getStatement(offset).getRange().get().end.line;
                    endLine = block.getStatement(offset + NO_BLOCKS_TO_CHECK - 1).getRange().get().end.line;

                    Occurrence first = new Occurrence(file, methodStartLine, methodEndLine);
                    Occurrence second = new Occurrence(first, name, startLine, endLine);
                    first.setLinkedOccurrence(second);
                    occurrences.add(first);
                    severity = 3;
                }
            }
            else if(similar > 97) severity = 2;
            else if(similar > 95) severity = 1;
        }
    }

    /** For a given String block, this method returns a String with all the comments
     * of various types, (i.e. Multiline or Single Line), removed
     * @param block String to return without comments
     * @return String without comments but spaces and newline characters remain**/
    private String removeComments(String block) {
        String str = block.trim();
        str = str.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        return str;
    }

    /** This method removes all whitespace and newline characters in
     * @param block String to remove whitespace and newline characters from
     * @return block with no whitespace or newline characters*/
    private String removeWhitespaceAndNewline(String block){
        return block.replaceAll("\\s", "");
    }

    /** This method combines statements in a method into blocks of size NO_OF_BLOCK_TO_CHECK,
     * returning a String with comments, whitespace and newline characters removed
     * @param block block we want statements combine from
     * @param index starting index of statements */
    private String combineStatements(BlockStmt block, int index){
        StringBuilder str= new StringBuilder();
        List<Statement> list = block.getStatements();
        for(int i=index;i<index+NO_BLOCKS_TO_CHECK;i++) {
            str.append(removeComments(list.get(i).toString()));
        }
        return removeWhitespaceAndNewline(str.toString());
    }

    /** This method returns the similarity percentage of two strings as per the formula:
     * no. of equal characters / total no. of characters of longest string * 100
     * @param text1 first string to check
     * @param text2 second string to check
     * @return percentage similar */
    private double getSimilarityPercentage(String text1, String text2){
        double percent=0.0;
        String str = removeWhitespaceAndNewline(removeComments(text1));
        String str2 = removeWhitespaceAndNewline(removeComments(text2));
        int length = str.length()>str2.length() ? str2.length() : str.length();
        for(int i=0;i<length;i++){
            if(str.charAt(i) == str2.charAt(i)) percent+=1.0;
        }
        percent /= length;
        return percent*100;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public int getSeverity() {
        return severity;
    }
}