---
    TFLNummer: 1
    title: Optimierung der Build-Dauer eines Web Application Bundler durch Anpassung der Konfiguration und dessen Auswirkung auf den Entwicklungsprozess

    author: Max Mustermann
    Zenturie: A22f
    Studiengang: Angewandte Informatik
    Matrikelnummer: 1337

    keywords: [keyword1, keyword2]
---
# Treating games a rooted trees
prerequisites: a deterministic strategic games (as defined in the introduction).

representation as an directed graph
all possible gamestates are represented by nodes
all possible moves are represented by edges
all possible moves ending situations are moves

gamestates that could be reached in multiple ways are represented by multiple nodes (for the sake of simplicity)

## Treting strategie as treexploratione
rebuilds human behavior (lice thinking in what if conditions)
can be done depthfirst, breath depthfirst


## recursive strategie
when formalized by a game tree, the expected result of a node is:
    the node if it is a leaf
    the node with the best expected result for the player making the next move, that its child

```java
private GameStateTreeNode<T> choice(GameStateTreeNode<T> start) {
    Set<GameStateTreeNode<T>> children = start.getChildren();
    //recursion anchor
    if (children.isEmpty()) {
        return start;
    }
    //recursion step
    GameStateTreeNode<T> result = children.stream()
            .peek(this::choice)
            .max(Comparator.comparingDouble(node -> getPoints(node, start.getState().getNextChoice())))
            .orElse(start);
    cache.cache(start.getState(), result.getState());
    return result;
}
```
where getPoits is the Method that gets the points chached expected result of the node.

###performance issues
with this recursive Strategie you are soon to face performance issue, because it is exponential value.
    - duration ~ breath ^ depth (if the depth and breath is constant)
this can be done for a tic tac toe, where it is 362.880 (9!), it is even harder for four in a row, where it is depending on the size at least: width!^height
- and chess is way whorse!
thats why we will break early
