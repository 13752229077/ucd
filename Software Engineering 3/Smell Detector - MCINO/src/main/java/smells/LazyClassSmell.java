package smells;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import metrics.FileMetrics;
import metrics.MethodMetrics;
import visitors.MethodDependentVarsVisitor;

import java.util.*;

/*
Class is not lazy if:
- It's a super class to at least one other class
- It's an Associate Class (i.e. as a field)
- It's depended upon by methods of other class

ONLY severity 3 or 0 makes sense as class can't be kind of lazy
 */
public class LazyClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Lazy Class";
    private List<String> lazyClasses;
    private FileMetrics[] allMetrics;

    //Look for all lazy classes upon instantiation
    public LazyClassSmell(FileMetrics[] allMetrics){
        lazyClasses = new ArrayList<>();
        this.allMetrics = allMetrics;
        findLazyClasses();
    }

    //finds lazy classes by getting the ones that we know are not lazy and removing them from the set of all classes
    private void findLazyClasses(){
        Set<String> projectClassSet = new HashSet<>(); //all classes
        Set<String> superClassSet = new HashSet<>(); //superclasses
        Set<String> associateClassSet = new HashSet<>(); //classes
        Set<String> dependentClassSet = new HashSet<>();

        for(FileMetrics fm : allMetrics) {
            projectClassSet.addAll(fm.getClassNames());

            for(ClassOrInterfaceType cit : fm.getImplementedTypes()){ //implementing Initializable means it's a Controller so ignore as Lazy
                if(cit.getNameAsString().equals("Initializable")){
                    associateClassSet.addAll(fm.getClassNames());
                }
            }

            //populate set of superclasses
            for (ClassOrInterfaceType cit : fm.getExtendedTypes()) {
                superClassSet.add(cit.asString());
            }

            //populate the associate classes set
            for (FieldDeclaration field : fm.getFields()) {
                if (!field.getElementType().isPrimitiveType())
                    associateClassSet.add(field.getElementType().asString());
            }

            //populate the dependent classes set
            for (Map.Entry<String, MethodMetrics> entry : fm.getMethodsMetrics().entrySet()) {
                if(entry.getValue().getMethodDeclaration().getNameAsString().equals("main")){
                    dependentClassSet.addAll(fm.getClassNames()); //main method file isnt lazy obviously
                }
                MethodDependentVarsVisitor visitor = new MethodDependentVarsVisitor(fm.getCompilationUnit());
                dependentClassSet.addAll(visitor.getTypes());
            }
        }

        //subtract all other sets from the set of all classes
        projectClassSet.removeAll(superClassSet);
        projectClassSet.removeAll(associateClassSet);
        projectClassSet.removeAll(dependentClassSet);

        lazyClasses.addAll(projectClassSet); //add the remaining classes to the list of lazy classes
    }

    //this method simply returns severity 3 if class is deemed to be Lazy Class and 0 otherwise
    //Using the lazyClasses list to check if a given class is contained in the set
    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        for(String className : metrics.getClassNames()){
            if(lazyClasses.contains(className)){
                severity = 3;
                break;
            }
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
