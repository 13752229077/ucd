package visitors;

import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class SuperCallVisitor extends VoidVisitorAdapter<Void> {

    private boolean hasSuperCall = false;

    public boolean hasSuperCall(){
        return hasSuperCall;
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        super.visit(n, arg);
        System.out.println("HI");
        hasSuperCall=true;
    }
}
