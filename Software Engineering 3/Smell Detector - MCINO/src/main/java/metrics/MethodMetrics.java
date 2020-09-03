package metrics;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;


public class MethodMetrics {
    private int numOfParams;
    private int numOfLines;
    private NodeList<Modifier> modifiers;
    private int startLine;
    private int endLine;
    private MethodDeclaration md;
    private BlockStmt body;

    MethodMetrics(MethodDeclaration md){
        this.md = md;
        computeMetrics(md);
    }

    //Extracting number of lines and parameters of the method from MethodDeclaration
    private void computeMetrics(MethodDeclaration md){
        modifiers = md.getModifiers();
        if(md.getBody().isPresent()){
            body = md.getBody().get();
            numOfLines = body.getEnd().get().line - body.getBegin().get().line + 1;
            startLine = body.getBegin().get().line;
            endLine = body.getEnd().get().line;

        }
        else{
            numOfLines = 0;
        }
        numOfParams = md.getParameters().size();
    }

    /* GETTERS */
    public int getNumOfLines() {
        return numOfLines;
    }

    public BlockStmt getBody() {
        return body;
    }

    public int getNumOfStatements() {
        return md.getBody().isPresent() ? md.getBody().get().getStatements().size() : 0;
    }

    public int getNumOfParams() {
        return numOfParams;
    }

    public NodeList<Modifier> getModifiers() {
        return modifiers;
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public MethodDeclaration getMethodDeclaration() {
        return md;
    }

    @Override
    public String toString() {
        return body.toString();
    }
}