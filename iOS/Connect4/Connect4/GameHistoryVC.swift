//
//  GameHistoryVC.swift
//  Connect4
//
//  Created by Daniel on 07/04/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//

import UIKit
import CoreData

class GameHistoryVC: CoreDataTVC {
    fileprivate var fetchedResultsController: NSFetchedResultsController<GameSequence>?
    let container: NSPersistentContainer? = (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer
    
    // Insert new GameSequence object to CoreData
    func insertObject(gameID: Int, sequenceOfMoves: String, isUser: Bool, winningMoves: String, didBotStart: Bool){
        let context = AppDelegate.viewContext
        let sequence = GameSequence(context: context)
        
        sequence.gameID = Int16(gameID)
        sequence.sequenceOfMoves = sequenceOfMoves
        // remove [ ]
        sequence.winningMoves = winningMoves.replacingOccurrences(of: "[\\[\\]]", with: "", options: [.regularExpression])
        sequence.userWon = isUser ? true : false
        sequence.botStarts = didBotStart
        
        // save
        do {
            try context.save()
        } catch {
            print("Error - CoreData not saved")
        }
        
    }
    // return object with given gameID
    func getObject(gameID: Int) -> GameSequence {
        let context = AppDelegate.viewContext
        let sequence = try? GameSequence.getSequence(gameID: gameID, context: context) as? GameSequence
        return sequence!
        
    }
    // fill table with sorted games by most recent
    private func updateUI() {
        let container: NSPersistentContainer? = (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer
        if let context = container?.viewContext {
            let request: NSFetchRequest<GameSequence> = GameSequence.fetchRequest()
            request.sortDescriptors = [NSSortDescriptor(key: "gameID", ascending: false, selector: #selector(NSString.localizedCaseInsensitiveCompare(_:)))]
            fetchedResultsController = NSFetchedResultsController<GameSequence>(fetchRequest: request, managedObjectContext: context, sectionNameKeyPath: nil, cacheName: nil)
            try? fetchedResultsController?.performFetch()
            tableView.reloadData()
        }
    }
    // get Attr. string with given colour
    func getAttributedSubstring(string: String, colour: UIColor) -> NSAttributedString {
        let attributes: [NSAttributedString.Key: Any] = [
            .strokeColor: colour,
            .strokeWidth: -3.0,
            .foregroundColor : colour
        ]
        return NSAttributedString(string: string, attributes: attributes)
    }
    // prepare for segue to game replay
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let indexPath = tableView.indexPath(for: sender as! GameHistoryTVCCell), let game = fetchedResultsController?.object(at: indexPath), let gameVC = segue.destination as? GameView{
            gameVC.replay = Int(game.gameID)
            gameVC.restart = false
            gameVC.view.isUserInteractionEnabled = false
        }
    }

    // MARK: - VC Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        updateUI()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        updateUI()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }


    
// MARK: - Table view data source
    override func numberOfSections(in tableView: UITableView) -> Int {
        return fetchedResultsController?.sections?.count ?? 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let sections = fetchedResultsController?.sections, sections.count > 0 {
            return sections[section].numberOfObjects
        } else {
            return 0
        }
    }
    
    // set Cell labels
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let dequeuedCell = tableView.dequeueReusableCell(withIdentifier: "GameCell", for: indexPath) as! GameHistoryTVCCell
        
        if let game = fetchedResultsController?.object(at: indexPath) {
            dequeuedCell.id.text = game.gameID.description
            dequeuedCell.user.text = game.userWon ? "user" : "bot"
            // highlight winning 4 discs in red
            let attributedString = NSMutableAttributedString()
            let winning = game.winningMoves!.replacingOccurrences(of: "[\"\\[\\]]", with: "", options: [.regularExpression])
            for move in game.sequenceOfMoves!.components(separatedBy: ",") {
                attributedString.append(getAttributedSubstring(string: move+" ", colour: winning.contains(move) ? UIColor.red : UIColor.black))
            }
            dequeuedCell.sequence.attributedText = attributedString
        }
        return dequeuedCell
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if let sections = fetchedResultsController?.sections, sections.count > 0 {
            return sections[section].name
        } else {
            return nil
        }
    }

    override func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return fetchedResultsController?.sectionIndexTitles
    }

    override func tableView(_ tableView: UITableView, sectionForSectionIndexTitle title: String, at index: Int) -> Int {
        return fetchedResultsController?.section(forSectionIndexTitle: title, at: index) ?? 0
    }
}

// Game History cell
class GameHistoryTVCCell: UITableViewCell {
    @IBOutlet weak var id: UILabel!
    @IBOutlet weak var user: UILabel!
    @IBOutlet weak var sequence: UILabel!
    
    
    
}
