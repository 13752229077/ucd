package visitors;


import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

//Visitor that computes the length of a class
public class ClassLengthVisitor extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<Integer> arg) {
        super.visit(n, arg);
        arg.add(n.getEnd().get().line - n.getBegin().get().line + 1);
    }
}
