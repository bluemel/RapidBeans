@echo on
%JAVA_HOME%\bin\java -Xmx512M -classpath "%~dp0dist\rapidbeans-0.5.0.jar;%~dp0\classes_test;%RAPIDBEANS_TOOLS_HOME%\JUnit\3.8.1\junit.jar" org.rapidbeans.test.TestSuiteRuntimePerf
