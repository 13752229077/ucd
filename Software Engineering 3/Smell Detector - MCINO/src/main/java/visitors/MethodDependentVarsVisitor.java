package visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

//get all the different Types used (i.e. get all classes used)
public class MethodDependentVarsVisitor extends VoidVisitorAdapter<Void> {

    private List<String> types;

    public MethodDependentVarsVisitor(CompilationUnit cu){
        types = new ArrayList<>();
        super.visit(cu, null);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        types.add(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        super.visit(n, arg);
    }

    public List<String> getTypes() {
        return types;
    }
}
