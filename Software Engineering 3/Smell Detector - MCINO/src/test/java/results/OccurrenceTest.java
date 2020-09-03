package results;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class OccurrenceTest {


    private static Occurrence o1;
    private static Occurrence o2;
    private static Occurrence o3;

    @BeforeClass
    public static void init(){
        o1 = new Occurrence(10, 20);
        o2 = new Occurrence(1, 5);
        o3 = new Occurrence(30, 50);
        o2.setLinkedOccurrence(o3);
        o3.setLinkedOccurrence(o2);
    }

    @Test
    public void getStartLine() {
        assertEquals(10, o1.getStartLine());
        assertEquals(1, o2.getStartLine());
        assertEquals(30, o3.getStartLine());
    }

    @Test
    public void getEndLine() {
        assertEquals(20, o1.getEndLine());
        assertEquals(5, o2.getEndLine());
        assertEquals(50, o3.getEndLine());
    }

    @Test
    public void getLinkedOccurrence() {
        assertNull(o1.getLinkedOccurrence());
        assertEquals(o3, o2.getLinkedOccurrence());
        assertEquals(o2, o3.getLinkedOccurrence());
    }

    @Test
    public void hasLink() {
        assertTrue(!o1.hasLink());
        assertTrue(o2.hasLink());
        assertTrue(o3.hasLink());
    }

    @Test
    public void setLinkedOccurrence() {
        o1.setLinkedOccurrence(o2);
        assertEquals(o2, o1.getLinkedOccurrence());
        o1.setLinkedOccurrence(null);
        assertNull(o1.getLinkedOccurrence());
    }
}