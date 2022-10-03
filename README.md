#League Rankings Tracker

##Description

The "League Rankings Tracker"(LRT) is a Java console application that ingests data either by uploading a file 
or entering the data on command line. Once all the data has been uploaded LRT will then process
the data and display the results of the team rankings. For each match played a team will be allocated 3 pts for a win 1 pt for a draw and o 
pts for a loss.  
The results will be ordered from the highest to lowest ranking. If teams have same rankings the teams will be alphabetically
ordered  

###Pre-requisites
The LRT has been tested on MacOS & Ubuntu 20 with the following software versions installed:
* Java >= 11
* git >= 2.30
* mvn >= 3.02
 
###Install application
Download the source from github <githublink>
``` 
git clone
```
From the PROJECT_HOME dir run mvn package to generate an executable jar file
```$xslt
mvn clean package
```
Location of artifact will be found in PROJECT_HOME/target/LeagueTracker-1.0-SNAPSHOT.jar


###Run the app
Application can be run using the mvn command or running the executable
mvn
```$xslt
 mvn -e exec:java  -Dexec.mainClass=com.span.devtest.LeagueTrackerApp 
```

Executable jar file
```$xslt
 java -jar <JAR_LOCATION_PATH>/LeagueTracker-1.0-SNAPSHOT.jar
```

User will be prompted input option
'F' for file upload
'I' to input values on console

File Upload
If environment variable(SCAN_DATA_DIR) is set to a location where data files 
are stored the user only needs to enter the filename. If not set the full path needs to be entered when prompted

To set env $SCAN_DATA_DIR. From terminal enter:
```$xslt
export SCAN_DATA_DIR=<LOCATION_TO_DATA_DIR>
```
Format of data file
```
Lions 3, Snakes 3
Tarantulas 1, FC Awesome 0
Lions 1, FC Awesome 1
Tarantulas 3, Snakes 1
Lions 4, Grouches 0
```

Console Input   
Data must be entered in the following format. Hit return key after each line
```$xslt
Lions 3, Snakes 3
```
To exit the data capture, hit Enter on a blank line or type 'exit'

Results output  
Once the data has been captured the data will be processed and results displayed in the following format
```$xslt
1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
4. Grouches, 0 pts
```

###Notes
It is assumed that all data inputted into the application (either by file or via console) will be in the correct format. 
Therefore, for the purpose of this assessment exception handling has not been done to handle input in incorrect formats 





