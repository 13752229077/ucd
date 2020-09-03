package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Android {
    public static void main(String[] args) {
        ArrayList<String []> details1 = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader("services.csv"));
            String line = br.readLine();
            while(line != null){
                String[] service = line.split(",");
                details1.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int counter=0;
        for(String [] details:details1){
            //System.out.println(aDetail[0]+" " +aDetail[1]+" " +aDetail[2]+" " +aDetail[3]+" ");
            System.out.println("INSERT INTO \"main\".\"services\" (\"Service_id\", \"Name\", \"Website\", \"PhoneNum\", \"County\") VALUES ('"+counter+"', '"+details[0]+"', '"+details[1]+"', '"+details[2]+"', '"+details[3]+"');");
        }
    }

}


/**String [] array = {"Dublin" , "db1234",
 "Kildare" , "bc8214",
 "Wicklow" , "14bc14",
 "Wexford" , "06edca",
 "Carlow" , "c9009c",
 "Laois" , "000fff",
 "Kilkenny" , "fff000",
 "Waterford" , "171717",
 "Limerick" , "858585",
 "Clare" , "a55aa5",
 "Offaly" , "062309",
 "Roscommon" , "511000",
 "Sligo" , "b9b9b9",
 "Leitrim" , "520dab",
 "Cavan" , "ecdecd",
 "Monaghan" , "abcdef",
 "Longford" , "001020",
 "Meath" , "51fc00",
 "Louth" , "666def",
 "Westmeath" , "fc130c",
 "Galway" , "e3accc",
 "Mayo" , "708708",
 "Cork" , "ea9aaa",
 "Tipperary" , "7bab76",
 "Kerry" , "ff9b3b",
 "Donegal" , "ff7432"};
 for(int i =0;i<array.length-1;i+=2) {
 String sql = "INSERT INTO \"main\".\"county_colours\" (\"County\", \"Colour\") VALUES ('"+array[i]+ "', '"+array[i+1]+"')";
 System.out.println(sql);
 }
 }
 * */
