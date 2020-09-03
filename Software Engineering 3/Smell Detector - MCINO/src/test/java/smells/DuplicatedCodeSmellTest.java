package smells;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.util.List;

import results.Occurrence;
import metrics.FileMetrics;


import static org.junit.Assert.*;

public class DuplicatedCodeSmellTest {
    private static Object[][] data, actual;

    @Test
    public void getOccurrences() {
        for (int i = 0; i < 5; i++) {
            for(int j=0;j<6; j++){
                assertEquals(actual[i][j], data[i][j]);
            }
        }
    }
    
    @BeforeClass
    public static void getData(){
        CompilationUnit[] CUs = new CompilationUnit[] {
                JavaParser.parse("public class Kelvin implements HeatScalable {\n" +
                        "    // private variable of double precision that will hold the temperature in Kelvin (assigned below in constructor)\n" +
                        "    private double temperature;\n" +
                        "\n" +
                        "    public Kelvin(double value) {\n" +
                        "        this.temperature = value;\n" +
                        "\t}\n" +
                        "\n" +
                        "\tpublic void aMethod(int sum_b){\n" +
                        "\t\tsum_b = 0;\n" +
                        "\n" +
                        "\t\tfor (int i = 0; i < 4; i++)\n" +
                        "\t\t\tsum_b += array_b[i];\n" +
                        "\t\n" +
                        "\t\tint average_b = sum_b / 4;\n" +
                        "\t\tsum_a = 0;\n" +
                        "\n" +
                        "\t\tfor (int i = 0; i < 4; i++)\n" +
                        "\t\t\tsum_a += array_a[i];\n" +
                        "\t\tfor(int i = 0;i<10;i++){\n" +
                        "\t\t\ttemperature++;\n" +
                        "\t\t}\n" +
                        "\t\t\n" +
                        "\t\tint average_a = sum_a / 4;\n" +
                        "    }\n" +
                        "    // returns a converted to Celsius value for temperature as per formula K - 273.15 = C\n" +
                        "    @Conversion(min = -273.15)\n" +
                        "    public double toCelsius() { return temperature - 273.15; }\n" +
                        "\n" +
                        "    // returns a converted to Fahrenheit value for temperature as per formula (K - 273.15) * 9/5 + 32 = F\n" +
                        "    @Conversion(min = -459.67)\n" +
                        "    public double toFahrenheit() { return temperature * 1.8 - 459.67; }\n" +
                        "\n" +
                        "\tpublic String toAString(String someOtherString) { return formatter.format(temperature) + \"K\" + someOtherString; }\n" +
                        "    // as class is Kelvin this method just returns the stored variable temperature\n" +
                        "    @Invariant\n" +
                        "    @Conversion(min = 0)\n" +
                        "    public double toKelvin(int sum_a) { \n" +
                        "\tsum_a = 0;\n" +
                        "\n" +
                        "\t\tfor (int i = 0; i < 4; i++)\n" +
                        "\t\t\tsum_a += array_a[i];\n" +
                        "\t\tfor(int i = 0;i<10;i++){\n" +
                        "\t\t\ttemperature++;\n" +
                        "\t\t}\n" +
                        "\t// return formatter.format(temperature) + \"K\";\n" +
                        "\treturn temperature; }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public String toString(String someString) { return formatter.format(temperature) + \"K\" + someString; }\n" +
                        "\t\n" +
                        "\tpublic class innerClass{\n" +
                        "\t\tpublic String doesNothing(){\n" +
                        "\t\t\treturn \"Nothing\";\n" +
                        "\t\t}\n" +
                        "\t\tpublic String doesNada(){\n" +
                        "\t\t\treturn \"Nothing\";\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n"),
                JavaParser.parse("package nosejob;\n" +
                        "\n" +
                        "public class Celsius implements HeatScalable {\n" +
                        "    // private variable of double precision that will hold the temperature in Celsius (assigned below in constructor)\n" +
                        "    private double temperature = 0;\n" +
                        "\n" +
                        "    public Celsius(double value) {\n" +
                        "        this.temperature = value;\n" +
                        "    }\n" +
                        "\n" +
                        "    // as class is Celsius this method just returns the stored variable temperature\n" +
                        "    @Invariant\n" +
                        "    @Conversion(min = -273.15)\n" +
                        "    public double toCelsius() {\n" +
                        "\t\tsum_a = 0;\n" +
                        "\n" +
                        "\t\tfor (int i = 0; i < 4; i++)\n" +
                        "\t\t\tsum_a += array_a[i];\n" +
                        "\t\tfor(int i = 0;i<10;i++){\n" +
                        "\t\t\ttemperature++;\n" +
                        "\t\t}\n" +
                        "\t\t\n" +
                        "        return temperature;\n" +
                        "    }\n" +
                        "\n" +
                        "    // returns a converted to Fahrenheit value for temperature as per formula C * 9/5 + 32 = F\n" +
                        "    @Conversion(min = -459.67)\n" +
                        "    public double toFahrenheit() {\n" +
                        "        return temperature * 1.8 + 32;\n" +
                        "    }\n" +
                        "\n" +
                        "    // returns a converted to Kelvin value for temperature as per formula C + 273.15 = K\n" +
                        "    @Conversion(min = 0)\n" +
                        "    public double toKelvin() { \n" +
                        "\t\tsum_a = 0;\n" +
                        "\n" +
                        "\t\tfor (int i = 0; i < 4; i++)\n" +
                        "\t\t\tsum_a += array_a[i];\n" +
                        "\t\tfor(int i = 0;i<10;i++){\n" +
                        "\t\t\ttemperature++;\n" +
                        "\t\t}\n" +
                        "\treturn temperature + 273.15; }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public String toString() { return formatter.format(temperature) + \"C\"; }\n" +
                        "}\n" +
                        "\n")
        };

        FileMetrics[] allMetrics = new FileMetrics[]{
                new FileMetrics(CUs[0]),
                new FileMetrics(CUs[1])
        };

        DuplicatedCodeSmell duplicatedCodeSmell = new DuplicatedCodeSmell(allMetrics);
        for(FileMetrics file : allMetrics){
            duplicatedCodeSmell.detectSmell(file);
        }

        List<Occurrence> occ = duplicatedCodeSmell.getOccurrences();
        Object[][] datum = new Object[][] {{occ.get(0).getFile(), occ.get(0).getStartLine(), occ.get(0).getEndLine(),
                occ.get(0).getLinkedOccurrence().getFile(), occ.get(0).getLinkedOccurrence().getStartLine(), occ.get(0).getLinkedOccurrence().getEndLine()},

                {occ.get(1).getFile(), occ.get(1).getStartLine(), occ.get(1).getEndLine(),
                        occ.get(1).getLinkedOccurrence().getFile(), occ.get(1).getLinkedOccurrence().getStartLine(), occ.get(1).getLinkedOccurrence().getEndLine()},

                {occ.get(2).getFile(), occ.get(2).getStartLine(), occ.get(2).getEndLine(),
                        occ.get(2).getLinkedOccurrence().getFile(), occ.get(2).getLinkedOccurrence().getStartLine(), occ.get(2).getLinkedOccurrence().getEndLine()},

                {occ.get(3).getFile(), occ.get(3).getStartLine(), occ.get(3).getEndLine(),
                        occ.get(3).getLinkedOccurrence().getFile(), occ.get(3).getLinkedOccurrence().getStartLine(), occ.get(3).getLinkedOccurrence().getEndLine()},

                {occ.get(4).getFile(), occ.get(4).getStartLine(), occ.get(4).getEndLine(),
                        occ.get(4).getLinkedOccurrence().getFile(), occ.get(4).getLinkedOccurrence().getStartLine(), occ.get(4).getLinkedOccurrence().getEndLine()}};
        data = datum;

        Object[][] data = new Object[][]   {{"Celsius", 15, 21, "Celsius", 35, 41},

                {"Celsius", 15, 21, "Kelvin", 16, 22},

                {"Celsius", 15, 21, "Kelvin", 39, 45},

                {"Celsius", 35, 41, "Kelvin", 16, 22},

                {"Celsius", 35, 41, "Kelvin", 39, 45}};
        actual = data;
    }

}