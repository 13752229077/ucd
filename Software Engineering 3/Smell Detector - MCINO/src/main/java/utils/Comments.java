package utils;

import java.util.HashMap;

public class Comments {

    private static Comments cache = null;
    private HashMap<String, String[]>  comments = new HashMap<String, String[]>() {{

        put("Long Class", new String[]{
                "Acceptable class length (<800)",
                "Class length is above the recommended maximum of 800 lines.",
                "This class is too long, consider breaking down your class.",
                "The length of this class is overwhelming, refactor the class to shorten it!"
        });

        put("Long Methods", new String[]{
                "All methods are nice and compact.",
                "At least one method is longer than the recommended 40 lines",
                "The average method length is 150% more than recommended 40 lines.",
                "The average method length is 200% more than recommended 40 lines. It is highly advised that the method is broken down"
        });

        put("Cyclomatic Complexity", new String[]{
                "This class has an acceptable average cyclomatic complexity (< 10).",
                "Complexity is greater than 10 for at least one method, be cautious not to increase it much more.",
                "The code within this class is very complex, consider refactoring to make scalability easier.",
                "Your class is way too complex. Affected methods must be refactored!"
        });
        put("Data Clumps", new String[]{
                "No data clumps detected.",
                "Some potential data clumps detected, please inspect the affected methods.",
                "Many potential data clumps detected, please check the affected methods and modify where possible",
                "Poor program structure due to an overwhelming number of data clumps detected. Needs urgent attention!"
        });
        put("Duplicated Code", new String[]{
                "No duplicated code detected.",
                "Few cases of duplicated code found, inspect the affected code stumps.",
                "Many cases of duplicated code found, consider enclosing affected code in a method",
                "This file's a mess. Do you even know what reusable code means?"
        });
        put("Feature Envy", new String[]{
                "No signs of feature envy detected",
                "Signs of feature envy detected, inspect affected methods.",
                "A few cases of potential feature envy smell found, consider doing computations within the Class to which the data belongs",
                "Feature Envy is a big problem. Move computations to classes to which the used data belongs to."
        });
        put("Heavy Commenting", new String[]{
                "No heavy comments detected.",
                "Traces of over commenting found, try make your code more self explanatory",
                "Heavy Commenting detected, consider whether your classes and variables are named appropriately if you require so many comments.",
                "This class is overloaded with comments meaning its likely hard to follow. Use more descriptive method/variable names"
        });
        put("Lazy Class", new String[]{
                "Not a lazy class.",
                "...",
                "...",
                "This class is a lazy class, make sure you are actually using it, otherwise you might be able to delete it."
        });
        put("Refused Bequest", new String[]{
                "No refused bequest detected.",
                "Signs of refused bequest found, remember what a subclass is for!",
                "Refused Bequest prominent, unused methods or no overriding found. Check affected areas.",
                "The contract of the base class is not honoured by the derived class. Violation of coding principles. FIX IT!"
        });
    }};

    private Comments(){
    }

    public static synchronized Comments getCommentsClass(){
        if(cache == null)
            cache = new Comments();
        return cache;
    }

    public String getComment(String smellName, int severity){
        return comments.get(smellName)[severity];
    }

}
