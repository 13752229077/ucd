package smells;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeavyCommentingSmellTest {

    private static HeavyCommentingSmell hcs = new HeavyCommentingSmell();

    @BeforeClass
    public static void init(){
        CompilationUnit cu = JavaParser.parse(
                "public class ArrayQueue<T> implements Queue<T> {\n" +
                        "\n" +
                        "\tprivate T[] array;//test comment\n" +
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
                        "\tpublic ArrayQueue(int size) {//test comment\n" +
                        "\t\tarray = (T[]) new Object[size];//test comment\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * Adds an element to the end of the queue and\n" +
                        "\t * it also resizes the array if the queue is about to be full\n" +
                        "\t */\n" +
                        "\t@Override//test comment\n" +
                        "\tpublic void enqueue(T object) {\n" +
                        "\t\t\n" +
                        "\t\t//test comment\n" +
                        "\t\tif(size() == array.length-1)\n" +
                        "\t\t\tresize();//test comment\n" +
                        "\t\tarray[rear] = object;//test comment\n" +
                        "\t\trear = (rear+1) % array.length;//test comment\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * Removing and returning the first element in the queue.\n" +
                        "\t * Throws a runtime exception is queue is empty\n" +
                        "\t * @return element at the end of the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic T dequeue() {\n" +
                        "\t\tif(isEmpty()) throw new QueueEmptyException();//test comment\n" +
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
                        "\t\tif(isEmpty()) throw new QueueEmptyException();//test comment\n" +
                        "\t\treturn array[front];//test comment\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return true if the list is empty\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic boolean isEmpty() {//test comment\n" +
                        "\t\treturn front == rear;//test comment\n" +
                        "\t}\n" +
                        "\n" +
                        "\t/**\n" +
                        "\t * @return the number of elements in the queue\n" +
                        "\t */\n" +
                        "\t@Override\n" +
                        "\tpublic int size() {//test comment\n" +
                        "\t\treturn (array.length + rear - front) % array.length;//test comment\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * Doubles the array size to allow for more elements in the queue\n" +
                        "\t */\n" +
                        "\t@SuppressWarnings(\"unchecked\")\n" +
                        "\tprivate void resize() {//test comment\n" +
                        "\t\tT[] newArray = (T[]) new Object[array.length * 2]; //the resized array\n" +
                        "\t\tint size = size();//test comment\n" +
                        "\t\tint i = 0;//test comment\n" +
                        "\t\twhile(!isEmpty()) {//test comment\n" +
                        "\t\t\tnewArray[i++] = dequeue();\n" +
                        "\t\t}\n" +
                        "\t\tfront = 0;//test comment\n" +
                        "\t\trear = size;//test comment\n" +
                        "\t\tarray = newArray;//test comment\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\t/**\n" +
                        "\t * @return a string in the form: (queue size) / (current array size) : (elements of the queue from front to rear)\n" +
                        "\t */\n" +
                        "\tpublic String toString() {\n" +
                        "\t\tString queue = size() + \" / \" + array.length + \" : \";//test comment\n" +
                        "\t\tint i = front;//test comment\n" +
                        "\t\twhile(i != rear) {//test comment\n" +
                        "\t\t\tqueue += array[i] + \" \";\n" +
                        "\t\t\ti = (i+1)%array.length;\n" +
                        "\t\t}\n" +
                        "\t\treturn queue;\n" +
                        "\t}\n" +
                        "\n" +
                        "}"
        );

        hcs.detectSmell(new FileMetrics(cu));
    }

    @Test
    public void getSeverity() {
        assertEquals(1, hcs.getSeverity());
    }

    @Test
    public void getOccurrences() {
        assertNull(hcs.getOccurrences());
    }

    @Test
    public void getSmellName() {
        assertEquals("Heavy Commenting", hcs.getSmellName());
    }
}