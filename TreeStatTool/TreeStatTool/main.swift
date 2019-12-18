//
//  main.swift
//  TreeStatTool
//
//  Created by Til Blechschmidt on 17.12.19.
//  Copyright © 2019 Til Blechschmidt. All rights reserved.
//

import Foundation
import CSV

enum EndCondition: Equatable, Hashable {
    case P1
    case P2
    case Tie
    
    init?(raw: String) {
        if raw.hasSuffix("P1") {
            self = .P1
        } else if raw.hasSuffix("P2") {
            self = .P2
        } else if raw.hasSuffix("Tie") {
            self = .Tie
        } else {
            return nil
        }
    }
}

enum EvaluationStrategy: Equatable, Hashable {
    case consecutiveLengthAsMaxDiv1000
    case consecutiveLengthAsSquareSumDiv1000
    case occupiedInRowAsMaxDiv1000
    case occupiedInRowAsSquareSumDiv1000
    
    init?(raw: String) {
        let keys: [String : EvaluationStrategy] = [
            "InARowGameHeuristicEvaluation(consecutive length accumulated asmax/1000)": .consecutiveLengthAsMaxDiv1000,
            "InARowGameHeuristicEvaluation(consecutive length accumulated assquare sum/1000)": .consecutiveLengthAsSquareSumDiv1000,
            "InARowGameHeuristicEvaluation(occupied in possible row accumulated asmax/1000)": .occupiedInRowAsMaxDiv1000,
            "InARowGameHeuristicEvaluation(occupied in possible row accumulated assquare sum/1000)": .occupiedInRowAsSquareSumDiv1000
        ]
        
        if let value = keys[raw] {
            self = value
        } else {
            return nil
        }
    }
}

enum ExitCondition: Equatable, Hashable {
    case turnLimit(distance: Int)
    case heuristic(difference: Double, distance: Int)
    
    init?(raw: String) {
        let components = raw.components(separatedBy: "=")
        
        guard components.count > 1 else { return nil }
        
        var distanceComponent: String? = nil
        var differenceComponent: String? = nil
        
        if components.first!.hasPrefix("or(TurnCountingCondition") || components.first!.hasPrefix("TurnCountingCondition") {
            distanceComponent = String(components[1].split(separator: "}")[0])
        } else {
            distanceComponent = String(components[2].split(separator: "}")[0])
        }
        
        if components.count == 3 && components.first!.hasPrefix("or(CompareToOtherOptions") {
            differenceComponent = String(components[1].split(separator: "}")[0])
        } else if components.count == 3 {
            differenceComponent = String(components[2].split(separator: "}")[0])
        }
        
        if components.count == 2, let distanceComp = distanceComponent, let distance = Int(distanceComp) {
            self = .turnLimit(distance: distance)
        } else if components.count == 3, let diffComp = differenceComponent, let difference = Double(diffComp), let distanceComp = distanceComponent, let distance = Int(distanceComp) {
            self = .heuristic(difference: difference, distance: distance)
        } else {
            return nil
        }
    }
}

struct GameExecution: Decodable {
    let id: Int
    let timestamp: String
    let totalExecutionTimeNanos: Int
    var totalExecutionTime: TimeInterval { return Double(totalExecutionTimeNanos) / 1_000_000_000.0 }
    
    let boardHeight: Int
    let boardWidth: Int
    let winLength: Int
    
    let gameType: String
    let gravity: Bool
    
    let turnsPlayed: Int
    let chronic: String
    
    let initialBoard: String
    let finalBoard: String
    let result: String
    let errors: String
    var endCondition: EndCondition? { return EndCondition(raw: result) }
    
    let cacheP1: String
    let cacheP2: String
    let nodesP1: Int
    let nodesP2: Int
    let executionTimeP1Nanos: Int
    let executionTimeP2Nanos: Int
    var executionTimeP1: TimeInterval { return Double(executionTimeP1Nanos) / 1_000_000_000.0 }
    var executionTimeP2: TimeInterval { return Double(executionTimeP2Nanos) / 1_000_000_000.0 }
    
    let expansionStrategyP1: String
    let expansionStrategyP2: String
    let rawEvaluationStrategyP1: String
    let rawEvaluationStrategyP2: String
    var evaluationStrategyP1: EvaluationStrategy? { return EvaluationStrategy(raw: rawEvaluationStrategyP1) }
    var evaluationStrategyP2: EvaluationStrategy? { return EvaluationStrategy(raw: rawEvaluationStrategyP2) }
    let rawExitConditionP1: String
    let rawExitConditionP2: String
    
    var exitConditionP1: ExitCondition? { return ExitCondition(raw: rawExitConditionP1) }
    var exitConditionP2: ExitCondition? { return ExitCondition(raw: rawExitConditionP2) }
    
    enum CodingKeys: String, CodingKey {
        case id
        case timestamp = "system time"
        case boardHeight = "board height"
        case boardWidth = "board width"
        case gameType = "game type"
        case winLength = "win length"
        case gravity = "gravity"
        case turnsPlayed = "turns played"
        case initialBoard = "initial board"
        case finalBoard = "final board"
        case result
        case errors
        case totalExecutionTimeNanos = "full duration"
        case chronic
        
        case expansionStrategyP1 = "expansion strategy of Player 1"
        case cacheP1 = "cache of Player 1"
        case nodesP1 = "watched nodes of Player 1"
        case rawEvaluationStrategyP1 = "evaluation stategy of Player 1"
//        case exitConditionTypeP1 = "exit condition type of Player 1"
        case rawExitConditionP1 = "exit condition of Player 1"
        case executionTimeP1Nanos = "total Speed of Player 1"
        
        case expansionStrategyP2 = "expansion strategy of Player 2"
        case cacheP2 = "cache of Player 2"
        case nodesP2 = "watched nodes of Player 2"
        case rawEvaluationStrategyP2 = "evaluation stategy of Player 2"
//        case exitConditionTypeP2 = "exit condition type of Player 2"
        case rawExitConditionP2 = "exit condition of Player 2"
        case executionTimeP2Nanos = "total Speed of Player 2"
    }
}

let filePath = "/Users/themegatb/Projects/Studies/KIIntro/TreeOptimizationAnalysis/ticTacToeOutput.csv"
var executions = [GameExecution]()

do {
    let stream = InputStream(fileAtPath: filePath)!
    let reader = try! CSVReader(stream: stream, hasHeaderRow: true, trimFields: true, delimiter: ";", whitespaces: .whitespaces)
    let decoder = CSVRowDecoder()
    
    while reader.next() != nil {
        let row = try decoder.decode(GameExecution.self, from: reader)
        executions.append(row)
        
        if executions.count % 1000 == 0 {
            print("Read \(executions.count) rows.")
        }
    }
} catch {
    // Invalid row format
    print("Invalid row format!", error)
}

//typealias GroupedByType = ExitCondition
typealias GroupedByType = EvaluationStrategy

extension Dictionary where Key == GroupedByType, Value == [GameExecution] {
    func accumulate<T: AdditiveArithmetic>(_ keyPath: KeyPath<GameExecution, T>, initialValue: T) -> [Key : T] {
        return mapValues { $0.reduce(initialValue) { $0 + $1[keyPath: keyPath] } }
    }
    
    func average(_ keyPath: KeyPath<GameExecution, Int>) -> [Key : Double] {
        var result: [Key : Double] = [:]
        
        accumulate(keyPath, initialValue: 0).forEach {
            result[$0.key] = Double($0.value) / Double(self[$0.key]?.count ?? 1)
        }
        
        return result
    }
    
    func average(_ keyPath: KeyPath<GameExecution, Double>) -> [Key : Double] {
        var result: [Key : Double] = [:]
        
        accumulate(keyPath, initialValue: 0).forEach {
            result[$0.key] = $0.value / Double(self[$0.key]?.count ?? 1)
        }
        
        return result
    }
}

let groupedGames: [GroupedByType : [GameExecution]] = executions.reduce(into: [:], { accumulator, execution in
    guard let exitCondition = execution.evaluationStrategyP1 else { return }
    accumulator[exitCondition, default: []] += [execution]
})


let groupedTurnsPlayed = groupedGames.average(\.turnsPlayed)
let groupedTotalExecutionTime = groupedGames.average(\.totalExecutionTime)
let groupedExecutionTimeP1 = groupedGames.average(\.executionTimeP1)
let groupedExecutionTimeP2 = groupedGames.average(\.executionTimeP2)
let groupedNodesP1 = groupedGames.average(\.nodesP1)
let groupedNodesP2 = groupedGames.average(\.nodesP2)

let groupedEndConditions: [GroupedByType : [EndCondition : Double]] = groupedGames.mapValues {
    return $0.reduce(into: [:]) {
        guard let endCondition = $1.endCondition else { return }
        $0[endCondition, default: 0] += 1
    }
}

let stream = OutputStream(toFileAtPath: "/Users/themegatb/Downloads/ticTacToeAccumulated.csv", append: false)!
let csv = try! CSVWriter(stream: stream, delimiter: ";")

let keys = groupedGames.keys
let dictionaries = [groupedTurnsPlayed, groupedTotalExecutionTime, groupedExecutionTimeP1, groupedExecutionTimeP2, groupedNodesP1, groupedNodesP2]

// ["distance, "difference", "evaluationStrategy", "turnsPlayed", "totalExecutionTime", "executionTimeP1", "executionTimeP2", "nodesP1", "nodesP2", "winCountP1", "winCountP2", "tieCount"]
try! csv.write(row: ["evaluationStrategy", "turnsPlayed", "totalExecutionTime", "executionTimeP1", "executionTimeP2", "nodesP1", "nodesP2", "winCountP1", "winCountP2", "tieCount"])
csv.beginNewRow()

keys.forEach { key in
//    switch key {
//    case .turnLimit(let distance):
//        try! csv.write(field: "\(distance)")
//        try! csv.write(field: "")
//    case .heuristic(let difference, let distance):
//        try! csv.write(field: "\(distance)")
//        try! csv.write(field: "\(difference)")
//    }
    try! csv.write(field: String(describing: key))
    
    dictionaries.forEach { dict in
        try! csv.write(field: dict[key].flatMap { "\($0)" } ?? "")
    }
    
    let endConditions = groupedEndConditions[key]
    try! csv.write(field: "\(endConditions?[.P1] ?? 0)")
    try! csv.write(field: "\(endConditions?[.P2] ?? 0)")
    try! csv.write(field: "\(endConditions?[.Tie] ?? 0)")
    
    csv.beginNewRow()
}

csv.stream.close()
