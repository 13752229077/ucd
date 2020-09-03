package utils;

import java.util.HashMap;

public class HTMLUtil {

    private static HTMLUtil cache = null;
    private HashMap<String,String> tabNameToHtmlFile = new HashMap<String, String>(){
        {
            put("Project", "html/project.html");
            put("Long Method", "html/long-method.html");
            put("Long Class", "html/long-class.html");
            put("Cyclomatic Complexity", "html/cyclomatic-complexity.html");
            put("Data Clumps", "html/data-clumps.html");
            put("Duplicated Code", "html/duplicated-code.html");
            put("Feature Envy", "html/feature-envy.html");
            put("Heavy Commenting", "html/heavy-commenting.html");
            put("Lazy Class", "html/lazy-class.html");
            put("Refused Bequest", "html/refused-bequest.html");

        }};



    private HTMLUtil(){
    }


    public String getHtmlFile(String tabName){
        return tabNameToHtmlFile.get(tabName);
    }

    public static synchronized HTMLUtil getHTMLUtil(){
        if(cache == null)
            cache = new HTMLUtil();
        return cache;
    }
}
