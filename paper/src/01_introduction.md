---
    title: Spielstrategie über Baumsuche in deterministischen Spielen
    
    author: Patrick Sabrowski, Til Blechschmidt
    Zenturie: A17a
    Studiengang: Angewandte Informatik
    Matrikelnummer: 8558, 8240

    secPrefix:
      - Sektion
      - Sektionen

    documentclass: article
---

# Einführung

Mit der Absicht einen Computerspieler für das klassische Brettspiel Go zu entwickeln, kreierte Googles DeepMind Team ein Programm namens AlphaGo welches Entscheidungsbäume nutzt um den bestmöglichen Zug in einer gegebenen Spielsituation zu finden. Bei Spielen wie TicTacToe und Vier Gewinnt ist es aufgrund der kleinen Feldgröße und limitierten Anzahl in annehmbarer Zeit möglich den kompletten Baum zu berechnen und folglich den idealen Zug zu tätigen. Dies ist bei Spielen wie Go oder Schach auf einem normalen Brett aufgrund des exponentiellen Wachstums der möglichen Züge mit der steigenden Brettgröße nicht mehr möglich. Bei Schach zeigte Claude Shannon bereits 1950, dass es bei Schach mindestens $10^{120}$ mögliche Spiele gibt[@src:shannon]. Dies ist selbst auf moderner Hardware nicht in einer annehmbaren Zeit zu berechnen. Entsprechend wird nach anderen Methoden geforscht. AlphaGo verfolgt den Ansatz die Entscheidung ob ein Unterbaum exploriert werden soll und die Bewertung der einzelnen Unterbäume durch einen Machine Learning Algorithmus zu lösen[@src:alphaGo]. Dieser Ansatz ist so erfolgreich, dass selbst der Weltmeister geschlagen werden konnte[@src:alphaGoWin].
In dieser Arbeit werden mögliche Abbruchbedingungen zur traversierung des Spielbaums sowie Bewertungsalgorithmen für die entstehenden Züge analysiert mit dem Ziel eine Empfehlung auszusprechen welcher der geprüften Algorithmen unter welchen Performance-Bedingungen das bestmögliche Ergebnis liefert.

## Übertragbarkeit

Entscheidungsbäume wie sie bei z.B. Schach, TicTacToe oder auch Vier Gewinnt Anwendung finden lassen sich auch auf einige Probleme aus der realen Welt anwenden **TODO FIND SOURCE** und ein Algorithmus zur optimierten Entscheidungsfindung ließe sich potentiell auf diese übertragen. Dabei muss allerdings beachtet werden, dass in den meisten realen Situationen keine vollständige Kenntnis der Situation vorhanden ist während dies in den meisten klassischen Brettspielen Spielen der Fall ist und zusätzlich meist nicht nur ein Gegenspieler teilnimmt sondern mehrere. Dies limitiert die Übertragbarkeit.
Ein weiterer Anwendungszweck für Computerspieler ist die Anwendung in modernen Computerspielen als Gegenspieler für Menschen. Sofern sich ein gegebenes Spiel als Entscheidungsbaum modellieren lässt, kann die Erkenntnis dieser Arbeit angewendet werden.

\pagebreak
