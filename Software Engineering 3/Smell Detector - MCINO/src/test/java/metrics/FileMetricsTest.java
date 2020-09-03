package metrics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileMetricsTest {

    private static CompilationUnit cu1;
    private static CompilationUnit cu2;
    private static FileMetrics fm1;
    private static FileMetrics fm2;

    @BeforeClass
    public static void init(){
        cu1 = JavaParser.parse(
                "public class ArrayQueue<T> extends Object implements Queue<T> {\n" +
                        "\n" +
                        "\tprivate T[] array;\n" +
                        "\tpublic int front = 0; //initial position of the front of the queue\n" +
                        "\tprivate int rear = 0; //initial position of the rear of the queue\n" +
                        "\t\n" +
                        "\t//Constructor without arguments creates an array of size 10\n" +
                        "\tpublic ArrayQueue(){\n" +
                        "\t\tthis(10);\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t//Constructor that takes in a size argument\n" +
                        "\t@SuppressWarnings(\"unchecked\")\n" +
                        "\tpublic ArrayQueue(int size) {\n" +
                        "\t\tarray = (T[]) new Object[size];\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * Adds an element to the end of the queue and\n" +
                        "\t * it also resizes the array if the queue is about to be full\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic void enqueue(T object) {\n" +
                        "\t\tif(size() == array.length-1)\n" +
                        "\t\t\tresize();\n" +
                        "\t\tarray[rear] = object;\n" +
                        "\t\trear = (rear+1) % array.length;\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * Removing and returning the first element in the queue.\n" +
                        "\t * Throws a runtime exception is queue is empty\n" +
                        "\t * @return element at the end of the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic T dequeue() {\n" +
                        "\t\tif(isEmpty()) throw new QueueEmptyException();\n" +
                        "\t\tT temp = array[front];\n" +
                        "\t\tarray[front] = null; //cleaning up the code by dereferencing the slot\n" +
                        "\t\tfront = (front+1) % array.length; //wrapping around the bounds for a circular array\n" +
                        "\t\treturn temp;\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * Returns but does NOT remove the first element in the queue\n" +
                        "\t * @return element at the front of the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic T front() {\n" +
                        "\t\tif(isEmpty()) throw new QueueEmptyException();\n" +
                        "\t\treturn array[front];\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return true if the list is empty\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic boolean isEmpty() {\n" +
                        "\t\treturn front == rear;\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return the number of elements in the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic int size() {\n" +
                        "\t\treturn (array.length + rear - front) % array.length;\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * Doubles the array size to allow for more elements in the queue\n" +
                        "\t */\n" +
                        "\t@SuppressWarnings(\"unchecked\")\n" +
                        "\tprivate void resize() {\n" +
                        "\t\tT[] newArray = (T[]) new Object[array.length * 2]; //the resized array\n" +
                        "\t\tint size = size();\n" +
                        "\t\tint i = 0;\n" +
                        "\t\twhile(!isEmpty()) {\n" +
                        "\t\t\tnewArray[i++] = dequeue();\n" +
                        "\t\t}\n" +
                        "\t\tfront = 0;\n" +
                        "\t\trear = size;\n" +
                        "\t\tarray = newArray;\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * @return a string in the form: (queue size) / (current array size) : (elements of the queue from front to rear)\n" +
                        "\t */\n" +
                        "\tpublic String toString() {\n" +
                        "\t\tString queue = size() + \" / \" + array.length + \" : \";\n" +
                        "\t\tint i = front;\n" +
                        "\t\twhile(i != rear) {\n" +
                        "\t\t\tqueue += array[i] + \" \";\n" +
                        "\t\t\ti = (i+1)%array.length;\n" +
                        "\t\t}\n" +
                        "\t\treturn queue;\n" +
                        "\t}\n" +
                        "}"
        );

        cu2 = JavaParser.parse(
                "public class Insert implements SQLStatement {\n" +
                        "\tprotected String name;\n" +
                        "\tprivate String[] columns;\n" +
                        "\tprivate String[] values;\n" +
                        "\t\n" +
                        "\tclass Dummy {\n" +
                        "\t\tpublic void innerMethod(){\n" +
                        "\t\t\t\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\tpublic Insert(String name, String[] columns, String[] values) {\n" +
                        "\t\tthis.name = name;\n" +
                        "\t\tthis.columns = columns;\n" +
                        "\t\tthis.values = values;\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t@Override\n" +
                        "\tpublic SQLResult execute(Map<String, Table> tables) throws SQLException {\n" +
                        "\t\tif (name.equals(\"mapsql.tables\")) throw new SQLException(\"Table 'mapsql.tables' cannot be modified\");\n" +
                        "\n" +
                        "\t\tfinal Table table = tables.get(name);\n" +
                        "\t\tif (table == null) throw new SQLException(\"Unknown table: \" + name);\n" +
                        "\n" +
                        "\t\ttable.description().checkForNotNulls(columns);\n" +
                        "\t\t\n" +
                        "\t\tString[] cols = table.description().resolveColumns(columns);\n" +
                        "\t\t\n" +
                        "\t\ttable.insert(cols, values);\n" +
                        "\t\treturn new SQLResult() {\n" +
                        "\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic TableDescription description() {\n" +
                        "\t\t\t\treturn table.description();\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic List<Row> rows() {\n" +
                        "\t\t\t\treturn null;\n" +
                        "\t\t\t}\n" +
                        "\t\t\t\n" +
                        "\t\t\tpublic String toString() {\n" +
                        "\t\t\t\treturn \"OK\";\n" +
                        "\t\t\t}\n" +
                        "\t\t};\n" +
                        "\t}\n" +
                        "}"
        );

        fm1 = new FileMetrics(cu1);
        fm2 = new FileMetrics(cu2);
    }

    @Test
    public void getClassLengths() {
        assertEquals(98, (int)fm1.getClassLengths().get(0));
        assertEquals(5, (int)fm2.getClassLengths().get(0)); //inner
        assertEquals(47, (int)fm2.getClassLengths().get(1)); //outer
    }

    @Test
    public void getFields() {
        assertEquals(3, fm1.getFields().size());
        assertEquals(3, fm2.getFields().size());

        //check all fields are found in the classes making sure to not include comments
        assertEquals("private T[] array;", fm1.getFields().get(0).toString());
        assertEquals("public int front = 0;", fm1.getFields().get(1).toString().replace(fm1.getFields().get(1).getComment().get().toString(), ""));
        assertEquals("private int rear = 0;", fm1.getFields().get(2).toString().replace(fm1.getFields().get(2).getComment().get().toString(), ""));

        assertEquals("protected String name;", fm2.getFields().get(0).toString());
        assertEquals("private String[] columns;", fm2.getFields().get(1).toString());
        assertEquals("private String[] values;", fm2.getFields().get(2).toString());
    }

    @Test
    public void getNumOfPublicFields() {
        assertEquals(1, fm1.getNumOfPublicFields());
        assertEquals(0, fm2.getNumOfPublicFields());
    }

    @Test
    public void getNumOfMethods() {
        assertEquals(7, fm1.getNumOfMethods());
        assertEquals(1, fm2.getNumOfMethods());
    }

    @Test
    public void getNumOfPublicMethods() {
        assertEquals(6, fm1.getNumOfPublicMethods());
        assertEquals(1, fm2.getNumOfPublicMethods());
    }

    @Test
    public void getClassNames() {
        assertEquals("ArrayQueue", fm1.getClassNames().get(0));
        assertEquals("Insert", fm2.getClassNames().get(0));
    }

    @Test
    public void getClassComments() {
        List<Comment> comments = cu1.getComments();
        int i=0;
        for(Comment c : fm1.getClassComments()){
            assertEquals(comments.get(i++), c);
        }

        comments = cu2.getComments();
        i=0;
        for(Comment c : fm2.getClassComments()){
            assertEquals(comments.get(i++), c);
        }
    }

    @Test
    public void getCompilationUnit() {
        assertEquals(cu1, fm1.getCompilationUnit());
        assertEquals(cu2, fm2.getCompilationUnit());
    }

    @Test
    public void getInnerClasses() {
        assertEquals(0, fm1.getInnerClasses().size());
        assertEquals(1, fm2.getInnerClasses().size());
    }

    @Test
    public void getClassConstructors() {
        assertEquals(2, fm1.getClassConstructors().size());
        assertEquals(1, fm2.getClassConstructors().size());

        assertEquals("public ArrayQueue()", fm1.getClassConstructors().get(0).getDeclarationAsString());
        assertEquals("public ArrayQueue(int size)", fm1.getClassConstructors().get(1).getDeclarationAsString());
        assertEquals("public Insert(String name, String[] columns, String[] values)", fm2.getClassConstructors().get(0).getDeclarationAsString());
    }

    @Test
    public void getInnerClassMethods() {
        assertEquals(0, fm1.getInnerClasses().size());
        assertEquals(1, fm2.getInnerClasses().size());

        assertEquals("Dummy", fm2.getInnerClasses().get(0).getNameAsString());
    }

    @Test
    public void getExtendedTypes() {
        assertEquals(1, fm1.getExtendedTypes().size());
        assertEquals(0, fm2.getExtendedTypes().size());

        assertEquals("Object", fm1.getExtendedTypes().get(0).asString());
    }

    @Test
    public void getImplementedTypes() {
        assertEquals(1, fm1.getImplementedTypes().size());
        assertEquals(1, fm2.getImplementedTypes().size());
    }
}