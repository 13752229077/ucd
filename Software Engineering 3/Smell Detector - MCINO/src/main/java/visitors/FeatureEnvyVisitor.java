package visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

//Using a visitor to determine number of uses of other classes fields / getter methods
public class FeatureEnvyVisitor extends VoidVisitorAdapter<Object> {

    private int count = 0;

    public FeatureEnvyVisitor(MethodDeclaration md){
        visit(md, new Object());
    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        if(!n.toString().startsWith("this.") && !n.toString().startsWith("System.")){
            count++;
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg) {
        if(n.getNameAsString().startsWith("get")){
            count++;
            return;
        }
        super.visit(n, arg);
    }

    public int getNumOfFieldAccesses(){
        return count;
    }
}
