# Team: SYNTAX_HIGHLIGHTERS

![Logo](assets/logo.png)

Meeting time: Tuesdays, 12-14 and Thursdays, 14-16

Coach: Jonathan Prieto-Cubides(jcu043@uib.no)

Team members:

- Eirik Jørgensen	(cum003)
- Benjamin Dyhre Bjønnes	(bbj018)
- Loc Tri Le	(lle016)
- Stian Soltvedt	(zuk005)
- Robin Grundvåg	(rgr015)
- Vegard Itland	(vit005)
- Sverre Magnus Engø	(sen006)
- Robin el Salim	(rsa035)


## Building

### Setup

- You will need:
    - JDK 8 or higher, you can find it [Here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
    - Maven
- Clone the repository to a local folder and run the command `mvn install && mvn -Pdesktop package` in the root folder (this might take a minute)

### Running and developing

#### Intellij IDEA

- Open the chess.iml file and you should be ready to go.
- Note: You might get a notification that says "Maven projects need to be imported", if this happens, then just select the "Import Changes" option.

#### Manual build

- To build the project, run the `mvn -Pdesktop package`
- The jar file for desktop should now be located in `src/desktop/target/chess-desktop-1.0-jar-with-dependencies.jar`
- Alternatively, run the command `java -jar "src/desktop/target/chess-desktop-1.0-jar-with-dependencies.jar"`
