# Zusammenfassung

Strategie ist in jedem Fall eine Abwägung zwischen Zeitaufwand und Qualität. Mit genug Rechenkapazität könnte man die ideale Lösung für jede Situation in Schach finden, doch dies ist nicht realistisch. Ansätze die Strategie einer Baumsuche zu optimieren hatten im Laufe dieser Arbeit nur minimale Auswirkungen. Wie zuvor erwähnt führen Bäume in den geprüften Szenarien nur dazu, dass der aktive Spieler weniger verliert. Allerdings gab es kaum Veränderungen in der Anzahl der gewonnenen Spiele. Dies kann man darauf zurückführen, dass eine optimierte Baumsuche nicht in der Lage ist die Schwächen des Gegenspielers auszunutzen und ihn zu Fehlern zu verleiten, die aufgrund seiner Strategie möglich sind. Außerdem lässt sich ableiten, dass eine einfachere Heuristik für die Bewertung von Zügen effektiver ist als eine komplexere.

Abgesehen davon lässt sich keine direkte Schlussfolgerung auf die Auswirkung von bestimmten Heuristiken und Abbruchbedingungen in größeren Bäumen treffen. Stattdessen haben die Ergebnisse eher eine Aussage zu dem jeweils getesteten Spiel zur Folge, da die unterschiedlichen Resultate für Vier Gewinnt, Tic-Tac-Toe sowie Schach eher auf die Art des Spiels zurückzuführen sind als auf die angewendete Heuristik und Abbruchbedingung.

## Forschungsempfehlung

Auch, wenn die Ergebnisse diese Arbeit selbst keinen Rückschluss auf die Anwendbarkeit bei größeren Bäumen zulassen, hat sie einen Einblick in zukünftige Forschungsrichtungen gegeben. Dabei könnten zum Beispiel verschiedene Spiele auf ähnliches Verhalten analysiert und darauf basierend in Gruppen eingeordnet werden in denen eine tiefgreifendere Forschungen zur Optimierung betrieben werden kann. Außerdem können zusätzlich zu den hier angewendeten Algorithmen weitere evaluiert werden um eventuell auch im Kontext der zuvor genannten Gruppen neue Erkenntnisse zu gewinnen. Schlussendlich kann diese Art von Forschung mit größeren Bäumen auf leistungsstärkerer Infrastruktur durchgeführt werden, um potenziell eine generalisierbare Aussage zu treffen.

## Fazit

Die angewendeten Algorithmen können aufgrund des Performance-Overhead nicht mit einer reinen Brute-Force Lösung mithalten. Entsprechend liegt der Wert dieser Arbeit nicht in den Eingangs erwarteten Empfehlungen bezüglich der Algorithmen, sondern darin, dass ein Framework geschaffen wurde, welches zukünftige Forschungen ermöglicht, die durch die Erkenntnisse aus dieser Arbeit ermöglicht werden.

\pagebreak
