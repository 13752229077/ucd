package utils;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class RabinKarpTest {

    private static RabinKarp rk;
    private static String[] haystack;
    //=;{
    @BeforeClass
    public static void init()
    {
        rk = new RabinKarp("A Needle");
        haystack = new String[] {"A Needle in a small haystack containing ",

                        "That is no country for old men. The young\n" +
                        "In one another’s arms, birds in the trees\n" +
                        "—Those dying generations—at their song,\n" +
                        "The salmon-falls, the mackerel-crowded seas,\n" +
                        "Fish, flesh, or fowl, commend all summer long\n" +
                        "Whatever is begotten, born, and dies.\n" +
                        "Caught in that sensual music all neglect\n" +
                        "Monuments of unageing intellect.\n" +
                        "\n" +
                        "An aged man is but a paltry thing,\n" +
                        "A tattered coat upon a stick, unless\n" +
                        "Soul clap its hands and sing, and louder sing\n" +
                        "For every tatter in its mortal dress,\n" +
                        "Nor is there singing school but studying\n" +
                        "Monuments of its own magnificence;\n" +
                        "And therefore I have sailed the seas and come\n" +
                        "To the holy city of Byzantium.\n" +
                        "\n" +
                        "O sages standing in God’s holy fire\n" +
                        "As in the gold mosaic of a wall,\n" +
                        "Come from the holy fire, perne in a gyre,\n" +
                        "And be the singing-masters of my soul.\n" +
                        "Consume my heart away; sick with desire\n" +
                        "And fastened to a dying animal\n" +
                        "It knows not what it is; and gather me\n" +
                        "Into the artifice of A Needle eternity.\n" +
                        "\n" +
                        "Once out of nature I shall never take\n" +
                        "My bodily form from any natural thing,\n" +
                        "But such a form as Grecian goldsmiths make\n" +
                        "Of hammered gold and gold enamelling\n" +
                        "To keep a drowsy Emperor awake;\n" +
                        "Or set upon a golden bough to sing\n" +
                        "To lords and ladies of Byzantium\n" +
                        "Of what is past, or passing, or to come.",


                        "Once out of nature I shall never take\n" +
                        "My bodily form from any natural thing,\n" +
                        "But such a form as Grecian goldsmiths make\n" +
                        "Of hammered gold and gold enamelling\n" +
                        "To keep a drowsy Emperor awake;\n" +
                        "Or set upon a golden bough to sing\n" +
                        "To lords and ladies of Byzantium\n" +
                        "Of what is past, or passing, or to come."};
    }

    @Test
    public void search1() {
        assertEquals(0, rk.search(haystack[0]));
    }
    @Test
    public void search2() {
        assertEquals(90, rk.search(haystack[1]));
    }
    @Test
    public void search3() {
        assertEquals(-1, rk.search(haystack[2]));
    }

}