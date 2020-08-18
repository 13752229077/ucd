//
//  GameSequence+CoreDataProperties.swift
//  Connect4
//
//  Created by Daniel on 08/04/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//
//

import Foundation
import CoreData


extension GameSequence {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<GameSequence> {
        return NSFetchRequest<GameSequence>(entityName: "GameSequence")
    }
    // all moves
    @NSManaged public var sequenceOfMoves: String?
    // just winning moves
    @NSManaged public var winningMoves: String?
    // if user won
    @NSManaged public var userWon: Bool
    // if bot started first
    @NSManaged public var botStarts: Bool
    // game id (unique)
    @NSManaged public var gameID: Int16

}


