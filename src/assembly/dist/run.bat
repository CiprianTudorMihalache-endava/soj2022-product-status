ECHO OFF
ECHO Starting the application

java -cp ${project.build.finalName}.jar;lib/* com.endava.tmd.soj.ps.ProductStatusApplication input.yaml

PAUSE