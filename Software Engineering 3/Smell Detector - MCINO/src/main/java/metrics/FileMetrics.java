package metrics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import visitors.ClassLengthVisitor;
import visitors.FieldCollector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/* Class that stores some information of a file such as class names and method metrics etc */
public class FileMetrics {
    private CompilationUnit cu;
    private List<String> classNames;
    private List<ClassOrInterfaceDeclaration> innerClasses;
    private List<ConstructorDeclaration> classConstructors;
    private List<Integer> classLengths;
    private List<Comment> classComments;
    private List<ClassOrInterfaceType> extendedTypes;
    private List<ClassOrInterfaceType> implementedTypes;
    private List<FieldDeclaration> fields;
    private int numOfPublicFields;
    private int numOfMethods;
    private int numOfPublicMethods;
    private HashMap<String, MethodMetrics> methodsMetrics;
    private HashMap<String, List<MethodDeclaration>> innerClassMethods;

    //initialize all data structures and extract metrics from the compilation unit
    public FileMetrics(CompilationUnit cu){
        this.cu = cu;
        methodsMetrics = new HashMap<>();
        innerClassMethods = new HashMap<>();
        classNames = new ArrayList<>();
        innerClasses = new ArrayList<>();
        classConstructors = new ArrayList<>();
        classLengths = new ArrayList<>();
        classComments = new ArrayList<>();
        extendedTypes = new ArrayList<>();
        implementedTypes = new ArrayList<>();
        fields = new ArrayList<>();
        extractMetrics();
    }

    //calls the different extract methods
    private void extractMetrics(){
        extractClassNames();
        extractClassLengths();
        extractNumOfFields();
        extractMethodsMetrics();
        extractComments();
    }

    //use classVisiotr to get class length
    private void extractClassLengths(){
        VoidVisitor<List<Integer>> classVisitor = new ClassLengthVisitor();
        classVisitor.visit(cu, classLengths);
    }

    //extract class names in the file from the Type Declarations and also the extended types and implemented types
    private void extractClassNames(){
        for(TypeDeclaration t : cu.getTypes()){
            if(t.isClassOrInterfaceDeclaration() && !t.isAnnotationDeclaration()) {
                if(!((ClassOrInterfaceDeclaration)t).getExtendedTypes().isEmpty())
                    extendedTypes.addAll(((ClassOrInterfaceDeclaration)t).getExtendedTypes());

                if(!((ClassOrInterfaceDeclaration)t).getImplementedTypes().isEmpty())
                    implementedTypes.addAll(((ClassOrInterfaceDeclaration)t).getImplementedTypes());

                classNames.add(t.getName().toString());

                // Find Inner Classes
                for (int i = 0; i < t.getMembers().size(); i++) {
                    BodyDeclaration body = t.getMember(i);
                    // below line will add inner classes to className but code needs to be altered to work with it
                    if (body.isClassOrInterfaceDeclaration())  innerClasses.add(body.asClassOrInterfaceDeclaration());
                }
            }
        }
    }

    //using the field collector visitor this method finds the fields in
    private void extractNumOfFields(){
        VoidVisitor<List<FieldDeclaration>> fieldCollector = new FieldCollector();
        fieldCollector.visit(cu, fields);

        for(FieldDeclaration f : fields){
            if(f.isPublic())
                numOfPublicFields++;
        }
    }

    /* This method sorts through the files members sorting them into
    * Types: ClassOrInterfaceDeclaration, MethodDeclaration & ConstructorDeclaration
    * and adds them to their respective List or HashMap  */
    private void extractMethodsMetrics(){
        for (TypeDeclaration td : cu.getTypes()) {
            List list = td.getMembers();
            for (Object o : list) {

                if (o instanceof AnnotationDeclaration) //ignore annotation files
                    continue;

                if (o instanceof ClassOrInterfaceDeclaration) { //if class add to innerClass methods
                    ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) o;
                    innerClassMethods.put(clazz.getName().asString(), clazz.getMethods());
                } else if (o instanceof MethodDeclaration) { //if method add to number of methods and get its metrics
                    MethodDeclaration md = (MethodDeclaration) o;
                    numOfMethods++;
                    if (md.isPublic())
                        numOfPublicMethods++;

                    String methodName = md.getName().toString();
                    methodsMetrics.put(methodName, new MethodMetrics(md));
                } else if (o instanceof ConstructorDeclaration) { //collect the constructors
                    ConstructorDeclaration cd = (ConstructorDeclaration) o;
                    classConstructors.add(cd);
                }
            }
        }
    }

    private void extractComments(){
        classComments.addAll(cu.getComments());
    }


    /* GETTERS */
    public List<Integer> getClassLengths() {
        return classLengths;
    }

    public List<FieldDeclaration> getFields() {
        return fields;
    }

    public int getNumOfPublicFields() {
        return numOfPublicFields;
    }

    public int getNumOfMethods() {
        return numOfMethods;
    }

    public int getNumOfPublicMethods() {
        return numOfPublicMethods;
    }

    public List<String> getClassNames(){
        return classNames;
    }

    public List<Comment> getClassComments() {
        return classComments;
    }

    public HashMap<String, MethodMetrics> getMethodsMetrics() {
        return (HashMap<String, MethodMetrics>) methodsMetrics.clone();
    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }

    public List<ClassOrInterfaceDeclaration> getInnerClasses() {
        return innerClasses;
    }

    public List<ConstructorDeclaration> getClassConstructors() {
        return classConstructors;
    }

    public HashMap<String, List<MethodDeclaration>> getInnerClassMethods() {
        return innerClassMethods;
    }

    public List<ClassOrInterfaceType> getExtendedTypes() {
        return extendedTypes;
    }

    public List<ClassOrInterfaceType> getImplementedTypes() {
        return implementedTypes;
    }
}