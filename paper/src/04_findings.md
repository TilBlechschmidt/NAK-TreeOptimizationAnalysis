# Untersuchungsergebnisse

Die Diagramme zu den folgenden Abschnitten befinden sich nach Spiel gruppiert im Anhang.

## Gewinnraten

Die Anzahl der Spiele, die eine Spielerkonfiguration gewonnen hat.

### TicTacToe & Vier Gewinnt

Entgegen der Erwartungen gab es keinerlei nennenswerte Abweichung dadurch, dass die Explorationstiefe erhöht wurde. Dies ließe sich dadurch erklären, dass diese Spielart meistens in einem Patt enden, wenn beide Spieler gut sind. Außerdem erwartet die Baumstrategie, dass der Gegenspieler den bestmöglichen Zug tätigt. Ein Ausnutzen von den Schwächen in der Strategie des Gegners oder diesen zu Fehlern zu verleiten ist dabei nicht möglich.
Das Ergebnis ist dabei abhängig von der Art der Bewertung. Die besten Resultate wurden erreicht, indem die zusammenhängenden Steine gezählt wurden.

### Schach

Bei Schach hingegen entsteht ein signifikanter Unterschied durch die Anpassung der Rekursionstiefe. Dies lässt vermuten, dass bei Schach eine komplizierte Strategie über mehrere Züge hinweg notwendig ist um zu gewinnen, während Spiele wie TicTacToe eher darauf basieren den Gegner nicht gewinnen zu lassen. Des Weiteren hatten Abbruchbedingungen auf Basis von Heuristiken keine starke Auswirkung, was darauf schließen lässt, dass sich die Güte einer Spielsituation nicht durch einfache Algorithmen bestimmen lässt.

## Verlustraten

Hier gab es im Gegensatz zu den Gewinnraten keine Unterschiede zwischen Schach und TicTacToe/Vier Gewinnt. Eine höhere Rekursionstiefe hat in jedem Fall die Anzahl der verlorenen Spiele reduziert. Dies ist darauf zurückzuführen, dass Situationen in denen der Gegner gewinnt früher erkannt und verhindert werden können. Diese Vermutung ist außerdem durch den proportionalen Zusammenhang zwischen der Rekursionstiefe und der Anzahl von Patt Situationen bestärkt.

Außerdem ist ersichtlich, dass ein Abbruch durch den Vergleich der Bewertung von verschiedenen Unterbäumen einen negativen Effekt hat. Dies lässt darauf schließen, dass die Bewertungsmethoden nicht optimal gewählt sind und entsprechend schlechtere Bäume exploriert werden.


## Zuganzahl

Die Anzahl der Züge verhält sich anti-proportional zu der Anzahl der verlorenen Spiele. Vermutlich ist das ein Effekt davon, dass Spiele solange laufen, bis eine Seite verliert und da die evaluierten Strategien laut vorher analysierten Daten sehr gut darin sind nicht zu verlieren aber nicht zu gewinnen gehen die Spiele entsprechend solange bis eine Partei verliert oder eine Patt Situation entsteht. Abgesehen davon wird dieser Wert lediglich als Teiler für andere Messwerte verwendet um die Metriken pro Zug zu erhalten.

## Performance

Lediglich die Daten für Spieler 1 haben signifikante Veränderungen, was nicht verwunderlich ist, da die Werte nach den Konfigurationen von diesem gruppiert wurden. Abgesehen davon lassen sich keine weiterführenden Ergebnisse aus den Daten ableiten.


### Anzahl der evaluierten Knoten

Die Anzahl steigt proportional zu der Anzahl der Züge. Deswegen wird im folgenden der Wert du durch die Anzahl der Züge geteilt verwendet. Die Anzahl der betrachteten Knoten pro Zug steigt exponentiell zu der Rekursionstiefe. Ein Abbruch durch Vergleich der Bewertung verschiedener Teilbäume hat nur einen marginalen Effekt. Dies lässt auf eine zu hohe Toleranz schließen oder darauf, dass dies bei der größe der betrachteten Bäume keine messbare Auswirkung hat.

### Laufzeit

Das Ergebnis der Laufzeitmessung zeigt bei beiden Spielarten, dass es in den gemessenen Situationen wesentlich teurer ist eine Abbruchbedingung auf Basis der Knotenbewertung zu evaluieren als es wäre den betreffenden Teilbaum zu berechnen. Wie erwartet steigt die Laufzeit mit einer tieferen Traversierung.

\pagebreak
