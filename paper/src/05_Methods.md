---
    TFLNummer: 1
    title: Optimierung der Build-Dauer eines Web Application Bundler durch Anpassung der Konfiguration und dessen Auswirkung auf den Entwicklungsprozess
    
    author: Max Mustermann
    Zenturie: A22f
    Studiengang: Angewandte Informatik
    Matrikelnummer: 1337

    keywords: [keyword1, keyword2]
---

# Methods
- find a lot of algorithms
	- divide into multiple steps
	- go through all methods
- test all algorithims with multiple games
- metrics
	- bla bla bla Messured Circumstances
	- parameters	
		- id: a Counter.
		- board height & board width: - offiecly - relevant for categorizing games
		- game type - Chess | in a Row Game
		- winlength & gravity: parameters for in a row games
		- initial board: the board that was the situation at the beginning
		- values per player
			- expansion strategy: breath or depth search
			- cache: the cache that was used, influences only performance
			- exit Strategy

	- metrics
		- turns played: how many turns where it
		- final board: the board in the end of the game
		- result: who is the winner or is it a TIE
		- errors: hopefully 0, sometimes " timed out " when the game has taken longer than allowed, hopfully nevver Exeption + Stacktrace
		- full Durations: The full duration in Ms as UserTime of the Thread used.
		- chronic: list of single steps - might be used for in detail analysis of games
		- metrics per player
			- nodes whatched: number of nodes that where calculated as part of tree-exploration,
				importent value since it is the only performancens Metric not dependend on the CPU
			- total speed: total time he needed for his calculation.

- Agregate Values by parameters to find max and mins for different metrics
- Interpret results

## Games researched
### Strategic deterministic game
- full knowledge
- 0-Sum
- limmited number of options,
- no creativity
- strict, formalized rules
- defined goal / win condition
- finit number of possible states

definition based on what he said in the lessons, maby find source

### In a Row Game
#### Tic Tac Toe
- TODO describe rules
- common strategies: Middle first, corner before edges
	- reasons Middle end corner are part of the diagonals and therefor part of more fields
#### 4 in a Row
- TODO describe rules
- TODO common strategies: start somewher in the middle and build your rows
	- reasons Middle end corner are part of the diagonals and therefor part of more fields
#### Things in common
- resons for the Strategies are simumlar
	- a bit represented in the Heuristic evaluation by number of stones in possible rows
		- Positions will block

## Chess
- rules
	- each piece has its own rules of movment
	- beating a piece by moving on its field
	- goal: Chessmate
		- must be able to beat the king
		- opponent must be unable to mace any move to save the king
- simplifications we made
	- no casteling
	- no Remi (since hard to simulate with AI players)
	- no give up before ChessMate
- beginner Strategies
	- trying to beat opponent pieces
	- trying to place your pieces in a way that they can not be beaten
	- trying to place your pieces, where if you get beaten you could beat the beating piece

	- represented by thinking 1 step ahead (beating) 2 steps ahead (avoid beeing beaten) and 3 steps (bea able to return being beaten)

## Games initialy planned 
(and why we didn't prioritise them)
- Go: to many moves, no idea of an appropriat usefull heuristic evaluation
- Reversi: not enaugh time, possibility of changing lots of stones at the same time makes heuristic evaluation hard


## Architecture
- statments abput advandages and disatvantages are expectations, if you want to see wether they were found in our data head to #findings

- Central Controller, directly acessing exchangeble components
- A Game Consists of two Controllers.
- Factory pattern for everything except Controllers
- -> Classdiagramm

## Exchangeble Components
### Chaches
- Only Relevant for performance
- basicl maps - but transitiv
- Variants
	- Compressed:
		- calculation for transitivit done on put
		- faster on get
	- Uncompressed:
		- fastter on put
		- calculation for transitivity done on get.

### Expansion Strategy
- defines order of Expansion
- Variants: Depth first, brath first
- defines what data is avaible during expansion (relevant for only some Exit-Strategies)

### Exit Strategy
- defines how deep to explore the tree
	- highest impact on performance
	- highest impact on precision
	- allways a Trade performance for precision
- Variants
	- counting:
		- shure performance (always the same depth)
		- requires no game mnlage
	- compareToOtherOptionsByHeuristicEvaluation
		- focus on promising results
		- less empphasis on offiasly stupid moves

### Heuristic Evaluation
- defines how to value a node that was not expanded to the verry end
- Hightly dependent on the game
- trying to be not that sofisticated in order not to replace the exploration

#### Heuristic Evaluation for in a Row Games
- counting how many Stones are in a row
	- counting consecutive stones
		- solid performance (linear to number of stones * win length)
		- not taking into account rows with a gap
		- taking into account rows that for shure are not going to be finished
	- counting number of stones in unoccupied in any given row of length that no jet contains
		- weaker performance (linar to number of fields * win length)
		- takes into account if there are multiple optins to finish a row
- adding up having multiple options
	- adding up squres of length
		- gives reward for having multiple options
	- making max
		- reqards only focussing on the best try

#### Heuristic Evaluation for Chess
- just counting the Figur values by a table
	- king -> not kounted, since always present
	- queen -> 9
	- bishop -> 3
	- knight -> 3
	- rook -> 5
	- pawn -> 1
- kind of a chess standart
- only used for beginners since it doesn't value positioning, (and anny other advanced concepts)


		

\pagebreak
