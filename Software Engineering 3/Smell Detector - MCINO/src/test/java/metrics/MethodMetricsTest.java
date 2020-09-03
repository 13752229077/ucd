package metrics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MethodMetricsTest {

    @Parameter(0)
    public static MethodMetrics methodMetrics;

    @Parameter(1)
    public static int numOfLines;

    @Parameter(2)
    public static BlockStmt body;

    @Parameter(3)
    public static int numOfStatements;

    @Parameter(4)
    public static int numOfParams;

    @Parameter(5)
    public static Modifier[] modifiers;

    @Parameter(6)
    public static int startLine;

    @Parameter(7)
    public static int endLine;

    @Parameter(8)
    public static MethodDeclaration md;

    @Parameters
    public static Collection<Object> data(){
        CompilationUnit cu = JavaParser.parse(
                "public class ArrayQueue<T> implements Queue<T> {\n" +
                        "\n" +
                        "\tprivate T[] array;\n" +
                        "\tprivate int front = 0; //initial position of the front of the queue\n" +
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
                        "\n" +
                        "}"
        );

        Object[][] data = new Object[][] {
                {null, 6, null, 3, 1,
                        new Modifier[] {Modifier.publicModifier()},
                        23, 28, null
                },
                {null, 7, null, 5, 0,
                        new Modifier[] {Modifier.publicModifier()},
                        36, 42, null
                },
                {null, 4, null, 2, 0,
                        new Modifier[] {Modifier.publicModifier()},
                        49, 52, null
                },
                {null, 3, null, 1, 0,
                        new Modifier[] {Modifier.publicModifier()},
                        58, 60, null
                },
                {null, 3, null, 1, 0,
                        new Modifier[] {Modifier.publicModifier()},
                        66, 68, null
                },
                {null, 11, null, 7, 0,
                        new Modifier[] {Modifier.privateModifier()},
                        74, 84, null
                },
                {null, 9, null, 4, 0,
                        new Modifier[] {Modifier.publicModifier()},
                        89, 97, null
                }
        };
        int i=0;
        for(BodyDeclaration bd : cu.getType(0).getMembers()){
            if(bd.isMethodDeclaration()){
                data[i][0] = new MethodMetrics((MethodDeclaration)bd);
                data[i][2] = ((MethodDeclaration)bd).getBody().get();
                data[i++][8] = bd;
            }
        }

        return Arrays.asList(data);
    }

    @Test
    public void getNumOfLines() {
        assertEquals(numOfLines, methodMetrics.getNumOfLines());
    }

    @Test
    public void getBody() {
        System.out.println(methodMetrics.getBody().toString());
        assertEquals(body, methodMetrics.getBody());
    }

    @Test
    public void getNumOfStatements() {
        assertEquals(numOfStatements, methodMetrics.getNumOfStatements());
    }

    @Test
    public void getNumOfParams() {
        assertEquals(numOfParams, methodMetrics.getNumOfParams());
    }

    @Test
    public void getModifiers() {
        assertEquals(1, methodMetrics.getModifiers().size());
        assertTrue(methodMetrics.getModifiers().contains(modifiers[0]));
    }

    @Test
    public void getStartLine() {
        assertEquals(startLine, methodMetrics.getStartLine());
    }

    @Test
    public void getEndLine() {
        assertEquals(endLine, methodMetrics.getEndLine());
    }

    @Test
    public void getMethodDeclaration() {
        assertEquals(md, methodMetrics.getMethodDeclaration());
    }
}