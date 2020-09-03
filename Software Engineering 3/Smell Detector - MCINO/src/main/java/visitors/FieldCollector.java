package visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

//visitor which gathers all fields into a list
public class FieldCollector extends VoidVisitorAdapter<List<FieldDeclaration>> {

    @Override
    public void visit(FieldDeclaration n, List<FieldDeclaration> arg) {
        super.visit(n, arg);
        arg.add(n);
    }
}
