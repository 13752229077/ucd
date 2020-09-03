# MCINO
Software Engineering Project 3

MCINO Code Smeller

---
SETTING UP THE MAVEN PROJECT IN INTELLIJ
-

1.	Import the project from the local repository

2.	Make sure <code>Create project from existing sources</code> is selected and press <code>Next</code>.

3.	Press <code>Next</code> again and the you will see source files have been found.
	Deselect all of them.
	
4.	Your project will now be setup, right-click on the project sidebar and select
	<code>Add as Maven project</code>.

5.	Next go to <code>File -> Project Structure... -> Modules</code>. In the Source tab, make
	sure that the <i>resources</i> folder in <i>src\main\java</i> is set as a resource module.
	
6.	Finally within <code>Project Structure... -> SDKs</code> ensure that <i>jdk1.8.VERSION</i> is selected.

7.	You should now be able to run Main.

<b>NOTE: Sometimes the resources folder is reset in the Project Structure, usually
upon changes to the pom.xml file. If this happens simply change it back.</b>

---
HOW TO RUN THE JAR FILE:
-
1.	Make sure you have <b>Java 8</b> installed to run the jar file.

2.	Download <i>MCINO.jar</i> from the github repository.

3.	Try running the jar file on your computer. If it doesn't work
	download jarfix.exe from the github repository.
	
4.	Run <i>jarfix.exe</i>, if the notification mentions a Java version
	different to <b>Java 8</b> follow the steps below, otherwise 
	<i>MCINO.jar</i> should now run.
	

jarfix.exe finds diiferent JDK/JRE
-
1.	Create a jarfix.ini file and include the following:
        <br><code>[jarfix]<br>
		action="C:\PATH_TO_JAVA8_BIN_FOLDER\javaw.exe" -Xmx512m -jar "%1" %*</code>

2.  Ensure that file is saved as <i>jarfix.ini</i> (extension must be .ini) and that
    it's saved in same directory as <i>jarfix.exe</i>

2.  <i>MCINO.jar should</i> now run.
---
