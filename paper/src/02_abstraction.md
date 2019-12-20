# Repräsentation & Strategie

Um eine Strategie für Spiele umzusetzen, muss zunächst eine Abstraktion durchgeführt werden.

## Darstellung von Spielen durch Wurzelbäume

Die erste Abstraktionsebene stellt die Repräsentation von Spielzuständen und Zügen in einem Wurzelbaum [@src:strategicGames] dar. Alle möglichen Spielzustände werden als Knoten repräsentiert, während die möglichen Spielzüge durch Kanten dargestellt werden. Spielzustände die auf mehreren Wegen erreicht werden können, werden zur Vereinfachung der Implementation durch mehrere Knoten abgebildet.
Ein Spiel lässt sich nur unter bestimmten Bedingungen als Baum darstellen. Auf diese wird in [@sec:possibleGames] näher eingegangen.

## Implementation von Strategie durch Exploration

Die Umsetzung von Strategie wird durch die Exploration des zuvor erstellen Baums umgesetzt. Dies kann sowohl durch Breadth-First-Traversal als auch Depth-First-Traversal umgesetzt werden. Durch diese Methode wird das menschliche Verhalten bei Strategiespielen nachgebildet, um mögliche Situationen zu evaluieren und die bestmögliche zu wählen.

## Rekursion

In einem formalisierten Spielbaum ist das erwartete Ergebnis eines Knotens:

- Der Knoten selbst, wenn er ein Blatt ist.
- Der Unterknoten mit dem besten erwarteten Ergebnis für den Spieler, der den nächsten Zug tätigt.

Dies ist umgesetzt durch:

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

    return result;
}
```

Wobei

```java
  getPoints(GameStateTreeNode, Player)
```

die Bewertung des jeweiligen Knotens durch die gecachten Werte ausführt, die im Vorraus mit ```this::choice``` ermittelt wurden.

### Performance

Mit einer rein rekursiven Strategie tritt sehr schnell ein Performance-Problem auf. Bei einem $3x3$ Tic-Tac-Toe Feld sind es $(3*3)! = 362.880$ mögliche Zustände, während es bei einem $7x6$ VierGewinnt bereits mindestens $7!^6 \approx 1.64 * 10^{22}$ sind. Bei klassischem Schach bewegt sich die Anzahl der möglichen Zustände in der Größenordnung von $10^{120}$ [@src:shannon]. Da dies selbst auf moderner Hardware nicht in einer annehmbaren Zeit berechenbar ist, wird die Traversierung im Rahmen dieser Arbeit anhand von verschiedenen Kriterien abgebrochen.
