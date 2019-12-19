# Methodik

Um die Varianten der Treeexploration zu egründen, haben wir den Algorithmus in mehrerer kleine Sektionen Zerlegt, für die wir mehrere beispielhafte Varianten implementiert haben. (siehe auch ([@sec:components]))
Die gefundenen und implementierten Algorithmen werden anschließend in jeder möglichen Kombination, die in einer für den Umfang dieser Arbeit annehmbaren Zeit berechnet werden kann, ausgeführt. Die Messwerte in dieser Arbeit wurden auf einem 16" MacBook Pro der ersten Generation mit einem Intel Core i9-9880H und einem stabilen Takt unter Last von $3.10 - 3.20$ GHz gesammelt (siehe Taktfrequenz Diagramm im Anhang).

Dabei werden folgende Daten für jedes Spiel in eine Datei geschrieben:

- Parameter
  - id
  - Spielbrettgröße
  - Spieltyp
  - Spielkonfiguration
    - Gravitation und Gewinnlänge für TicTacToe/VierGewinnt
  - Initiales Brett
  - Spielerkonfigurationen
    - Traversierungsstrategie
    - Cachestrategie
    - Bewertungsstrategie
    - Abbruchbedingung
- Metriken
  - Anzahl der Züge
  - Finales Brett
  - Endergebnis
  - Errors
  - CPU Zeit des Threads **TODO Elaborate what CPUtime is and what its drawbacks are and why we still use it**
  - Spielermetriken
    - Betrachtete Knoten
    - CPU Zeit des Spielers

Die gesammelten Metriken werden anschließend jeweils nach den genutzten Abbruchbedingung und Bewertungsstrategie gruppiert, akkumuliert, das arithmetische Mittel gebildet (bis auf die Endergebnisse) und grafisch aufgearbeitet.

## Analysierte Spiele

### Deterministische Strategiespiele {#sec:possibleGames}

Damit ein Spiel mittels Wurzelbäume gelöst werden kann müssen folgende Bedingungen erfüllt sein:

Vollständiges Wissen
: Der komplette Zustand des Spiels muss bekannt sein.

Nullsummenspiel
: Spiele bei denen die Summe der Gewinne und Verluste aller Spieler zusammengenommen gleich null ist.
<!-- TODO Cite this: Manfred J. Holler, Gerhard Illing: Einführung in die Spieltheorie. 7. Auflage. Springer, Berlin u. a. 2009, ISBN 978-3-540-69372-7, S. 55. -->

Endlich
: Die Anzahl der möglichen Spielzustände muss endlich und ein Gewinnzustand klar definiert sein

Formalisierbar
: Keine kreativen oder zufälligen Komponenten
  Klar definierte Regeln

[@src:sauer]

In dieser Arbeit wurden anhand von Komplexität und Implementationsaufwand die in den folgenden Sektionen erklärten Spiele gewählt. Des Weiteren wurde Go, das in Erwägung gezogen wurde, aufgrund der hohen Spielbaum breite und der schwierig zu implementierenden Heuristik zur Bewertung von Spielzügen nicht gewählt.

#### Tic Tac Toe
Bei TicTacToe werden spielerspezifische Steine auf einem quadratischen Feld mit einer Seitenlänge von drei abwechselnd von zwei Spielern gesetzt. Gewonnen hat derjenige, dessen Steine eine zusammenhängende vertikale, horizontale oder diagonale Reihe mit einer Länge von drei bilden. Eine übliche Strategie ist es, Steine in Positionen zu setzen, wo mehrere Möglichkeiten einer Reihe entstehen. Dies sind im Normalfall die Mitte und die Ecken. [@src:ticTacToe]

#### Vier gewinnt
Bei Vier gewinnt ist es das Ziel, genau wie bei TicTacToe, eine zusammenhängende vertikale, horizontale oder diagonale Reihe mit einer Länge von drei zu bilden. Die Rahmenbedingungen sind jedoch dahingehend anders, dass das Feld sieben Spalten breit und sechs Zeilen hoch ist und Steine nach unten fallen. Hier gilt ebenfalls die Strategie, dass es von Vorteil ist die Felder zu belegen, die einem mehr Optionen geben eine Reihe zu vervollständigen. [@src:fourConnect]

#### Schach
Das dritte Spiel was hier betrachtet wird ist Schach. Hierbei gibt es sechs verschiedene Figuren, die jeweils ihre eigenen Bewegungsregeln haben. Die zwei Spieler ziehen abwechselnd und es beginnt Weiß. Ein gegnerischer Stein kann geschlagen werden, indem eine eigene Figur auf das Feld gezogen wird. Das Ziel ist es die Königsfigur des Gegners zu Schach Matt zu setzen, also in eine Position zu bringen, in der sie geschlagen werden könnte und nicht ausweichen kann.
[@src:fideChessRules]

Für diese Arbeit wurden einige Einschränkungen getätigt:
* Keine Rochade
* Kein Schlagen en Passant
* Kein Spielende durch Remis ausser es nur noch die Könige auf dem Feld
* Aufgeben nicht möglich

Aufgrund der hohen Komplexität von Schach können nur wenige Schritte im Voraus berechnet werden. Hier werden nur maximal drei Schritte berechnet um grundlegende Strategien wie das schlagen gegnerischer Figuren, dieses zu verhindern und einen Schlagabtausch zu ermöglichen.

## Architektur
<!-- statments abput advandages and disatvantages are expectations, if you want to see wether they were found in our data head to #findings - TODO Which statements?! -->

Ein Spiel besteht aus zwei Controllern, die auf austauschbare Komponenten zugreifen. Diese austauschbaren Komponenten werden dabei durch eine Factory erstellt, genauere Informationen sind dem UML Diagramm im Anhang zu entnehmen.

## Austauschbare Komponenten {#sec:components}
### Caches
Die Baum-Caches sind lediglich zur Beschleunigung der Berechnung implementiert worden und wurden durch transitive HashMaps umgesetzt. Dabei gibt es zwei Varianten

Komprimiert
: Transitivität wird bei .put() sichergestellt, schnellere .get() Operation

Unkomprimiert
: Transitivität wird bei .get() berechnet, schnellere .put() Operation

### Traversierungsstrategien
Die Traversierungsstrategie definiert die Reihenfolge in der die Knoten des Spielbaums berechnet werden. Dabei gibt es zwei Varianten, die Breitentraversierung und Tiefentraversierung, wobei erstere den Baum Ebene für Ebene berechnet, während letztere Pfad für Pfad bis zum Blatt berechnet. Die Art der Traversierung bestimmt dabei welche Daten für die Abbruchbedingungen (siehe [@sec:exitConditions]) zur Verfügung stehen.

### Abbruchbedingungen {#sec:exitConditions}
Um den Baum nicht vollständig zu berechnen werden Abbruchbedingungen definiert. Wie früh abgebrochen wird hat dabei eine Auswirkung auf sowohl die Performance als auch die Präzision der Bewertung. Somit stellen die Bedingungen einen Trade-off zwischen Performance und Präzision dar. Im Rahmen dieser Arbeit wurden zwei Varianten betrachtet:

Zuganzahl
: Bricht nach einer festgelegten Anzahl von Zügen ab. Dabei ist die Performance immer gleich und es ist kein Wissen über das Spiel notwendig.

Heuristik
: Bricht bei Unterbäumen ab, die keine absehbar vorteilhafte Spielsituation darstellen und umgeht somit unter anderem Teilbäume die zu einem garantierten Verlust führen.

### Bewertung von Zügen
Bestimmt die Güte einer Spielsituation ohne den Teilbaum vollständig zu berechnen und ist möglichst minimal implementiert um nicht die Exploration des Baums vom Zeitaufwand her zu ersetzen. Die Umsetzung ist jedoch sehr stark von dem Spiel abhängig und wird im Folgenden erläutert.

#### TicTacToe & Vier Gewinnt
Die Anzahl der Steine in einer Reihe werden gezählt. Dabei gibt es zwei Varianten:

1. Anzahl der zusammenhängenden Steine wird gewertet
  - Performance linear zu der Anzahl an Steinen
  - Ignoriert Reihen mit freien Feldern dazwischen
  - Betrachtet Reihen, die nicht vervollständigt werden können
2. Anzahl der Steine und leeren Felder in einer Linie
  - Performance linear zu der Anzahl an Feldern
  - Betrachtet mehrere Möglichkeiten eine Reihe zu vervollständigen

Sofern mehr als eine Reihe existiert gibt es zwei Möglichkeiten diese zu akkumulieren:

1. Die quadrierten Summen der Länge aufaddieren
  - Legt Wert darauf mehrere Optionen zu haben
2. Maximale Länge verwenden
  - Fokussiert sich nur auf die beste Strategie

#### Schach
Es werden die Figuren auf dem Brett mit Zahlenwerten aus einer Tabelle belegt und aufsummiert. Dabei werden Aspekte wie die Positionierung der Figuren vernachlässigt. Außerdem wird dem König kein Wert zugewiesen, da dieser während einer laufenden Partie immer auf dem Spielfeld ist.

| Figur | Wert |
|:--|--:|
| König | -- |
| Dame | 9 |
| Turm | 5 |
| Läufer | 3 |
| Springer | 3 |
| Bauer | 1 |
[@src:chessValues]
\pagebreak
