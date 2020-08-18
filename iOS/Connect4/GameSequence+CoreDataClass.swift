//
//  GameSequence+CoreDataClass.swift
//  Connect4
//
//  Created by Daniel on 08/04/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//
//

import Foundation
import CoreData


public class GameSequence: NSManagedObject {
    // get unique game - error if more than one
    class func getSequence(gameID: Int, context: NSManagedObjectContext) throws -> Any? {
        
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "GameSequence")
        request.predicate = NSPredicate(format: "gameID == \(gameID)")
        do {
            let matches = try context.fetch(request)
            if matches.count > 0 {
                assert(matches.count == 1, "GameSequence.getSequence -- database inconsistency")
                return matches[0]
            }
        } catch { throw error }
        let gameSequence = GameSequence(context: context)
        gameSequence.gameID = Int16(gameID)
        return gameSequence
    }
}
