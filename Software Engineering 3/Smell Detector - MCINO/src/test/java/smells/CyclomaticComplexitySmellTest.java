package smells;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import results.Occurrence;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CyclomaticComplexitySmellTest {
    @Parameterized.Parameter()
    public static CyclomaticComplexitySmell ccs;

    @Parameterized.Parameter(1)
    public static int severity;

    @Parameterized.Parameter(2)
    public static int numOfOccurrences;

    @Parameterized.Parameter(3)
    public static int[] occurrenceStart;

    @Parameterized.Parameter(4)
    public static int[] occurrenceEnd;

    @Parameterized.Parameters
    public static Collection<Object> data(){
        Object[][] data = new Object[][] {
                {new CyclomaticComplexitySmell(), 1, 1, new int[] {23}, new int[] {65}}, //severity one
                {new CyclomaticComplexitySmell(), 2, 6, new int[] {23, 150, 120, 180, 220, 86}, new int[] {65, 174, 144, 215, 251, 114}}, //severity two (order is mixed since occurrences because CyclomaticComplexity loops through a HashMap)
                {new CyclomaticComplexitySmell(), 0, 0, new int[] {}, new int[] {}}, //no severity (empty file)
        };

        CompilationUnit[] cu = new CompilationUnit[3];
        cu[0] = JavaParser.parse(
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
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tfor(int i=0; i<5; i++){\n" +
                        "\t\t\tfor(int j=0; j<10; j++){\n" +
                        "\t\t\t\tif(i*j > 20)\n" +
                        "\t\t\t\t\tSystem.out.println(\"test\");\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
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
                        "}"
        );

        cu[1] = JavaParser.parse(
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
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tfor(int i=0; i<5; i++){\n" +
                        "\t\t\tfor(int j=0; j<10; j++){\n" +
                        "\t\t\t\tif(i*j > 20)\n" +
                        "\t\t\t\t\tSystem.out.println(\"test\");\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\t\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
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
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\tif(isEmpty()) throw new QueueEmptyException();\n" +
                        "\t\treturn array[front];\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return true if the list is empty\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic boolean isEmpty() {\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\treturn front == rear;\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return the number of elements in the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic int size() {\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
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
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * @return a string in the form: (queue size) / (current array size) : (elements of the queue from front to rear)\n" +
                        "\t */\n" +
                        "\tpublic String toString() {\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
                        "\t\tif(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}if(1 == 1){}\n" +
                        "\t\telse if(2 == 2){}\n" +
                        "\t\telse if(1 != 1){}\n" +
                        "\t\telse{}\n" +
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

        cu[2] = JavaParser.parse(
                ""
        );

        for(int i=0; i<3; i++){
            ((CyclomaticComplexitySmell)data[i][0]).detectSmell(new FileMetrics(cu[i]));
        }

        return Arrays.asList(data);
    }

    @Test
    public void getSeverity() {
        assertEquals(severity, ccs.getSeverity());
    }

    @Test
    public void getOccurrences() {
        if(ccs.getOccurrences() == null){
            return;
        }
        assertEquals(numOfOccurrences, ccs.getOccurrences().size());

        int i=0;
        for(Occurrence o : ccs.getOccurrences()){
            assertEquals(occurrenceStart[i], o.getStartLine());
            assertEquals(occurrenceEnd[i++], o.getEndLine());
        }
    }

    @Test
    public void getSmellName() {
        assertEquals("Cyclomatic Complexity", ccs.getSmellName());
    }
}