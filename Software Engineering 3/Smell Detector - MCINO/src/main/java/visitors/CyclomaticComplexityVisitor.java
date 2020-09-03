package visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

//using a visitor to find all branching in the code for cyclomatic complexity computation
public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Object> {

    private int count = 1; //cyclomatic complexity = num of (if, elsifs, switch cases, loops) + 1

    public CyclomaticComplexityVisitor(MethodDeclaration md){
        visit(md, new Object());
    }

    public int getComplexity(){
        return count;
    }

    @Override
    public void visit(ForStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }

    @Override
    public void visit(ForEachStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }

    @Override
    public void visit(DoStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }

    @Override
    public void visit(IfStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg) {
        super.visit(n, arg);
        count++;
    }
}
