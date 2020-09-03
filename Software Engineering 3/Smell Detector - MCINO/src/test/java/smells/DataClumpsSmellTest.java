package smells;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import org.junit.BeforeClass;
import org.junit.Test;
import results.Occurrence;

import static org.junit.Assert.*;

public class DataClumpsSmellTest {

    private static DataClumpsSmell dcs = new DataClumpsSmell();

    @BeforeClass
    public static void init(){
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
                        "\tpublic ArrayQueue(int size, int param1, float param2, String param3, String param4, Object param5, Object param6, Object param7) {\n" +
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
                        "\tpublic T dequeue(int param1, float param2, String param3, String param4, Object param5, Object param6, Object param7) {\n" +
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
                        "\tpublic T front(int param1, float param2, String param3, String param4, Object param5, Object param6, Object param7) {\n" +
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
                        "\tpublic int size(int param1, float param2, String param3, String param4, Object param5, Object param6, Object param7) {\n" +
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
                        "\tpublic String toString(int param1, float param2, String param3, String param4, Object param5, Object param6, Object param7) {\n" +
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
        dcs.detectSmell(new FileMetrics(cu));
    }

    @Test
    public void getSeverity() {
        assertEquals(1, dcs.getSeverity());
    }

    @Test
    public void getOccurrences() {
        Occurrence[] occurrences = new Occurrence[]{
                new Occurrence(66, 66),
                new Occurrence(36, 36),
                new Occurrence(89, 89),
                new Occurrence(49, 49)
        };
        assertEquals(4, dcs.getOccurrences().size());
        int i=0;
        for(Occurrence o : dcs.getOccurrences()){
            assertEquals(occurrences[i].getStartLine(), o.getStartLine());
            assertEquals(occurrences[i++].getEndLine(), o.getEndLine());
        }
        assertEquals(4, i);
    }

    @Test
    public void detectSmell() {
    }

    @Test
    public void getSmellName() {
        assertEquals("Data Clumps", dcs.getSmellName());
    }
}