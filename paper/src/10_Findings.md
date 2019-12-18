---
    TFLNummer: 1
    title: Optimierung der Build-Dauer eines Web Application Bundler durch Anpassung der Konfiguration und dessen Auswirkung auf den Entwicklungsprozess
    
    author: Max Mustermann
    Zenturie: A22f
    Studiengang: Angewandte Informatik
    Matrikelnummer: 1337

    keywords: [keyword1, keyword2]
---
#Findings
#In A Row
- inculde diagrams
##Win rates
- no relevant change by making deeper evaluation
    - Contrary to Expectations
    - possible explanations
        - In A Row game usually a Tie if both players are good
        - Tree exploration assumes competent opponent,
            - no heuristic for exploiting opponent weaknesses
            - no heuristic for tricking foe into mistakes
- dependent on evaluation (for in a Row games)
    - evaluation can be focused on finishing early
    - best result by just counting the consecutive fields in a row
           - we basicly overengeneered it with the other options (maybe find a euphemism)
## Lose Rates
- making deeper evaluation decreases lose-rate
- possible explanations:
    - more data to evaluate upon
    - sees longer winning strategies of oponent

- braking before By Comparation to other nodes has bad effekt
- possible explanations: 
    - less Nodes evaluated
    - maybe brakes on the wrong positions
        - someone else might investigate other heuristics what to evaluate

- 
## Turn counts
- actually never planned to have a plain result - just for making sense of perfomace values
- turn counts go up, if lose rates go down
- possible explanations:
    - the game goes until somone looses
    - our algorithms are better at not loosing, than on winning, for they don't exploit weaknesses

## Performance
- only the values for player 1 have remarkable changes
    - since we grouped the data by player 1 Strategies - we should not expect anything else.
### Nodes expanded
- increases with increasing turns -> thats why we also use Nodes expanded/turn
- due to caching referes importend for RAM performance
- Increases with increasing depth of evaluation
- increases round about in an exponential manner (on increasing limit of Expansion)
    - as expected for deaper expansion makes more nodes needed
- braking before By Comparation to other nodes has only liddle effect
    - maby to high tollarance
    - cutting of an arm does not give that much since we have liddle bredth and depth
        - method might still be intresting on 
### duration
- better metric for how expensive it is to calculate the value
- takes in account how much is needed for the exit condition

### 

\pagebreak