package visitors;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

//check if a given method has been used
public class MethodCallVisitor extends VoidVisitorAdapter<Void> {
    private String methodName;
    private int count = 0;

    public MethodCallVisitor(String methodName){
        super();
        this.methodName = methodName;
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        if(n.getName().asString().contains(methodName)) {
            count++;
        }
        else {
            super.visit(n, arg);
        }
    }

    public int getCount(){
        return count;
    }


}
