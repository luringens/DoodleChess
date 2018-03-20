![Logo](assets/logo.png)

-----------------------------------------------------------------------------

# Chess

A chess game developed at University of Bergen for INF112
course in Spring 2018.

## Getting Started

### Prerequisites
 
- [JDK v8 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
- [Maven](https://maven.apache.org/download.cgi)

### Installation

```
$ git clone https://gitlab.uib.no/inf112-v2018/gruppe-6.git chess-syntax-highlighters
$ cd chess-syntax-highlighters
$ mvn install
$ mvn -Pdesktop package
```

We can play the game running the following command:

```
$ java -jar "src/desktop/target/chess-desktop-1.0-jar-with-dependencies.jar
```


## Hacking 

### Intellij IDEA

- Open the `chess.iml` file and you should be ready to go.

*Note*: You might get a notification that says "Maven projects need to
be imported", if this happens, then just select the "Import Changes"
option.

### Manual build

- To build the project run the command:

```
$ mvn -Pdesktop package
```

- To skip tests run the command:

```
$ mvn -Pdesktop package -DskipTests
```

- The jar file for desktop should now be located in `src/desktop/target`. To run
the game use the command:

```
$ java -jar src/desktop/target/chess-desktop-1.0-jar-with-dependencies.jar
```

Some error on OSX can be solved (for now) adding the `-XstartOnFirstThread` flag.

## Contributing

Please read our [contribution guidelines](CONTRIBUTING.md)!

## Authors

Developers:

- Eirik Jørgensen (cum003)
- Benjamin Dyhre Bjønnes  (bbj018)
- Loc Tri Le (lle016)
- Stian Soltvedt  (zuk005)
- Robin Grundvåg  (rgr015)
- Vegard Itland (vit005)
- Sverre Magnus Engø  (sen006)
- Robin el Salim  (rsa035)

Coach: Jonathan Prieto-Cubides(jcu043@uib.no)

Meeting time: Tuesdays, 12-14 and Thursdays, 14-16