package utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Setup {

    private String javaDir;
    private File[] files;
    private HashMap<String, String> javaFiles;

    public Setup(String dirPath){
        javaFiles = new HashMap<>();
        findJavaFiles(dirPath);
    }

    private void getFiles() {
        File directory = new File(this.javaDir);
        this.files = directory.listFiles();

        if (this.files != null) {
            for (File file : this.files) {
                if (isJava(file)) {// check if current file has .java extension

                    String javaFileName = file.getName().substring(0, file.getName().indexOf(".")); //removing .java extension from file name

                    try{

                        // extract the contents of source file into a string
                        Scanner sc = new Scanner(file);
                        StringBuilder code = new StringBuilder();
                        while(sc.hasNextLine())
                        {
                           code.append(sc.nextLine()).append("\n");
                        }
                        sc.close();

                        this.javaFiles.put(javaFileName,code.toString()); // add filename and its contents to hashmap

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    private void findJavaFiles(String smellyCodeDir) {
        if(getDirectory(smellyCodeDir)){ // gets path of next java file
            getFiles(); // extract all files from directory
            for(File f: this.files)
            {
                if(f.isDirectory()) { // then repeat if any of the files inside current directory, is a directory
                    findJavaFiles(f.getAbsolutePath());
                }
            }
        }
    }

    private boolean getDirectory(String directoryName)
    {

        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();

        boolean found = false;

        int i = 0;

        if (fileList != null && fileList.length > 0) { // ensures directory is not empty
            while (i < fileList.length && !found) { // while java file is not found
                if (fileList[i].isDirectory()) {
                    found = getDirectory(fileList[i].getAbsolutePath()); // run recursively on newly found directory
                } else if (isJava(fileList[i])) { // if any of the files is a java file
                    this.javaDir = fileList[i].getAbsolutePath().substring(0, fileList[i].getAbsolutePath().indexOf(fileList[i].getName())); // get directory path in which the java file is in
                    found = true;
                }
                i++;
            }
            return found;
        }
        return false;
    }

    private boolean isJava(File file)
    {
        return getFileExtension(file).equals(".java");
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return ""; // empty extension
        }

        return name.substring(lastIndexOf);
    }


    public CompilationUnit[] run(){
        CompilationUnit[] cUnit = new CompilationUnit[javaFiles.keySet().size()];
        int index = 0;
        for(Map.Entry<String, String> code: javaFiles.entrySet())
        {
            cUnit[index] = JavaParser.parse(code.getValue()); // parsing all Java files
            index++;
        }

        return cUnit;

    }

    public HashMap<String, String> getSourceFiles(){
        return this.javaFiles;
    }

}