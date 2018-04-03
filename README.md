![Logo](assets/logo.png)

-----------------------------------------------------------------------------

# Chess

A chess game developed at University of Bergen for INF112
course in Spring 2018.

## Getting Started

### Prerequisites

- [JDK v8 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

### Installing

```
$ git clone https://gitlab.uib.no/inf112-v2018/gruppe-6.git chess-syntax-highlighters
$ cd chess-syntax-highlighters
```

### Running

We can play the game running the following command:

```
$ gradlew run
```

There is now a tar and zip file available in `build/distributions/` that contains a bin folder with a run file for running the game.

Note: This will not run the tests

### Building a fat-jar

A fat-jar is a jar file with all the dependencies aswell

```
$ gradlew fatjar
```

the jar should now be available in `build/libs/DoodleChess-fat.jar` and can be run using `java -jar build/libs/DoodleChess-fat.jar`

Note: This will not run the tests

### Running tests

```
$ gradlew test
```

### Building javadoc

```
$ gradlew javadoc
```

The javadoc should now be available in `docs/api`

## Hacking

## Game design model

See our updated [product specification](/docs/product-spec/product-specification.pdf) (section on class diagram) to see an overview of the classes and the relationships between them, coupled with a brief, high-level explanation of the design choices that we have made. Also see the [HTML version of the class diagram](/docs/diagrams/classdiagram.html) to see a version of the class diagram where you can open up the classes and look at the methods available in their public API. If you click on the diagram to enlarge, then click on the edit button at the bottom, you are brought to draw.io, where you can even edit the diagram as you please! (Changes made to the version at draw.io does not affect the files in this repo.)

## Contributing

Please read our [contribution guidelines](CONTRIBUTING.md)!

## Authors

Developers:

- Eirik Jørgensen (cum003)
- Benjamin Dyhre Bjønnes  (bbj018)
- Loc Tri Le (lle016)
- Stian Soltvedt  (zuk005)
- Robin Grundvåg  (rgr015) (sometimes theAzack9 in commits)
- Vegard Itland (vit005)
- Sverre Magnus Engø  (sen006)
- Robin el Salim  (rsa035)

Coach: Jonathan Prieto-Cubides(jcu043@uib.no)

Meeting time: Tuesdays, 12-14 and Thursdays, 14-16

## Attribution

The program's UI is built on LibGDX, which is licensed under ![Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0). A copy of the license can be found at `assets/LICENSE.txt`.

The program also uses the Indie Flower font, licensed under the Open Font License that can be found at `assets/OFL.txt`.

The chess AI is based on the examples from the Chess Programming Wiki. It is a common MiniMax implementation with alpha/beta pruning. See the ![Chess Programming Wiki](https://chessprogramming.wikispaces.com/Minimax)), including pages for A/B pruning, for the resources used during the development of the AI.
The scores used for evaluating the score of a given chess position is also from the ![Chess Programming Wiki](https://chessprogramming.wikispaces.com/Simplified+evaluation+function), which are shared by default under the Creative Commons Attribution-ShareAlike 3.0. However, it is taken from an email from Tomasz Michniewski sent to a Polish chess programming wiki without linking to the source, so whether or not the wiki can claim copyright on the content in the first place is unknown.
