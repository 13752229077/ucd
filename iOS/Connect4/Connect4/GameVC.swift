//
//  GameView.swift
//  Connect4
//
//  Created by Daniel on 28/03/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//

// Connect4 Framework with Deep Learning Bot
import Alpha0Connect4

import UIKit

class GameView: UIViewController, UIDynamicAnimatorDelegate {
    // game and board classes
    var board = Board()
    var session = GameSession()
    var gameHistory = GameHistoryVC()
    var game : GameSequence!
    // board view and game labels
    @IBOutlet weak var boardView: UIView!
    @IBOutlet weak var gameMessage: UILabel!
    @IBOutlet weak var replayLabel: UILabel!
    // Bools for ensuring game wont break when tapped too much
    var turnOver: Bool = true
    var restart: Bool = true
    // attr. for replay match
    var replay: Int = -1
    var replayIdx: Int = 0
    var replayMoves: [Int] = [Int]()
    // timer for replay animation
    var timer: Timer?
    // frame of board
    var boardFrame = CGRect()
    
    //add gestures and set up board
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(swipedUp(_:)))
        swipeUp.direction = .up
        view.addGestureRecognizer(swipeUp)
        
        let doubleTapGesture = UITapGestureRecognizer(target: self, action: #selector(self.doubleTap(_:)))
        doubleTapGesture.numberOfTapsRequired = 2
        view.addGestureRecognizer(doubleTapGesture)
        
        boardFrame = CGRect(x: 0, y: 0, width: boardView.frame.width, height: boardView.frame.height)
    }
    
    override func viewDidLayoutSubviews() {
        // if restart, setup then ask for starter
        if restart {
            setup()
            showAlert()
            restart = false
        }
        // if replay, setup then start replay of gameID
        if replay != -1 {
            setup()
            replay(gameID: replay)
            replay = -1
        }
    }
    // increment counter stored in user defaults to ensure DB consistency
    func incrementGameID() -> Int{
        let defaults = UserDefaults.standard
        if defaults.object(forKey: "gameID") != nil {
            var gameID = defaults.integer(forKey: "gameID")
            gameID += 1
            defaults.set(gameID, forKey: "gameID")
            return gameID
        } else {
            UserDefaults.standard.set(0, forKey: "gameID")
            return 0
        }
    }
    // ask who wants to start
    func showAlert() {
        let alert = UIAlertController(title: "Who goes first?", message: "Select the player to go first", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "You", style: UIAlertAction.Style.default, handler:{ action in
            self.setup()
            self.board.user = "user"
            self.gameMessage.text = "Your Turn"
        }))
        alert.addAction(UIAlertAction(title: "α-C4", style: UIAlertAction.Style.default, handler: { action in
            self.setup()
            self.board.user = "bot"
            self.session.botStarts = true
            self.botRound()
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    // setup new board and session then re draw board
    func setup() {
        board = Board()
        session = GameSession()
        board.drawBoard(frame: boardFrame)
        boardView.addSubview(board)
        restart = false
    }
    // play round
    func playRound(column: Int, tag: Int) {
        // if users go
        if session.userPlay(at: column) {
            if self.board.user == "user"{
                if let move = self.session.move {
                    DispatchQueue.main.async {
                        self.board.addDisc(index: move.index, tag: tag)
                    }
                }
            }
            // update turn message
            updateMessage(user: false)
            // let bot go
            if !session.done {
                Timer.scheduledTimer(timeInterval: 1.5, target: self, selector: #selector(self.botRound), userInfo: nil, repeats: false)
            }
        }
        // else bot first
        else {
            Timer.scheduledTimer(timeInterval: 1.5, target: self, selector: #selector(self.botRound), userInfo: nil, repeats: false)
        }
    }
            
    // objc func for bots turn
    @objc func botRound() {
        if let move = session.move {
            DispatchQueue.main.async {
                self.board.addDisc(index: move.index, tag: move.action)
                self.updateMessage(user: true)
                
            }
        }
    }
    
    // for game with gameID start replay
    func replay(gameID: Int) {
        // add replay label
        let replayMessage = replayLabelAttrString(set: true, string: "Action Replay", colour: UIColor.red)
        replayMessage.append(replayLabelAttrString(set: true, string: "\rDouble-tap to jump back in!", colour: UIColor.black))
        replayLabel.attributedText = replayMessage
        // retrieve game from CoreData
        game = gameHistory.getObject(gameID: gameID)
        board.user = game.botStarts ? "bot" : "user"
        replayMoves = game.sequenceOfMoves!.components(separatedBy: ",").map{Int($0)! }
        // start replay
        timer = Timer.scheduledTimer(timeInterval: 2, target: self, selector: #selector(self.moveAnimation), userInfo: nil, repeats: true)
    }
   
    // if not double tapped or replay not over, play round
    @objc func moveAnimation() {
        view.isUserInteractionEnabled = true
        if replayIdx == replayMoves.count { timer?.invalidate() }
        turnOver = self.board.isUserInteractionEnabled
        if timer!.isValid {
            if board.user == "α-C4" {
                self.botRound()
                replayIdx += 1
            }
            else {
                if(session.userPlay(at: replayMoves[replayIdx]%7)){
                    if let move = self.session.move {
                        DispatchQueue.main.async {
                            self.board.addDisc(index: move.index, tag: self.replayMoves[self.replayIdx])
                            self.replayIdx += 1
                        }
                    }
                    self.updateMessage(user: false)
                }
            }
        }
    }
    
    // get bold shadowed text
    func replayLabelAttrString(set: Bool, string: String, colour: UIColor) -> NSMutableAttributedString{
        let (size, shadowColour) = colour == UIColor.black ? (28, UIColor.white) : (36, UIColor.black)
        let font = UIFont(name: "Optima-Bold", size: CGFloat(size))
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.alignment = .center
        let shadow = NSShadow()
        shadow.shadowColor = shadowColour
        shadow.shadowBlurRadius = 5
        let attributes: [NSAttributedString.Key: Any] = [
            .font: font!,
            .foregroundColor: colour,
            .shadow: shadow,
            .paragraphStyle: paragraphStyle
        ]
        return NSMutableAttributedString(string: string, attributes: attributes)
    }
    
    // add appropriate message per game status
    func updateMessage(user: Bool) {
        if !session.done {
            self.gameMessage.text  = user ? "Your Turn" : "AlphaC4's Turn"
        } else {
            self.gameMessage.text = "Game Over ☹️ " + session.outcome!.message + "\r\rTap to show game sequence"
            let gameID = incrementGameID()
            let bot = session.outcome?.message.starts(with: "A")
            board.sequence.remove(at: board.sequence.startIndex)
            let seq = board.sequence
            // add game to CoreData if complete
            gameHistory.insertObject(gameID: gameID, sequenceOfMoves: seq, isUser: !bot!, winningMoves: session.outcome!.winningPieces.description, didBotStart: session.botStarts)
        }
    }
    
    
    // MARK:- TOUCHES
    // if user touches a column, add disc there if user round, session not over and board not animating
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if session.done {
            self.board.highlightPieces(player: session.outcome!.message)
            self.gameMessage.text = "Game Over ☹️ " + session.outcome!.message + "\r\rSwipe up to start again"
        }
        if restart { setup() }
        turnOver = self.board.isUserInteractionEnabled
        
        let touch = touches.first
        guard var point = touch?.location(in: self.board) else { return }
        point.y = point.y < 0 ? board.margin : point.y
        let subViews = self.board.subviews.first?.subviews
        for view in subViews ?? [] {
            if (view.frame.contains(point)) {
                if(turnOver && !session.done && !self.board.isAnimating){
                    let col = view.tag % 7
                    playRound(column: col, tag: view.tag)
                }
            }
        }
    }
    // invalidate timer on users turn to interrupt replay
    @objc func doubleTap(_ sender: UITapGestureRecognizer) {
        if (replayIdx > 0) && board.user == "user"  {
            replayIdx = 0
            timer?.invalidate()
            replayLabel.text = ""
            view.isUserInteractionEnabled = true
        }
    }
    // restart game buttom (top left)
    @IBAction func restartButtonAction(sender: UIBarButtonItem) {
        restartAlert()
    }
    // shuffle colours buttom (top right)
    @IBAction func shuffleButtonAction(sender: UIBarButtonItem) {
        board.randomiseColours()
        
    }
    // swipped up so ask if restart
    @objc func swipedUp(_ sender: UISwipeGestureRecognizer) {
        restartAlert()
    }
    // alert to double check user wants to restart
    func restartAlert() {
        let alert = UIAlertController(title: "Restart Game", message: "Are you sure you want to restart?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Restart", style: UIAlertAction.Style.destructive, handler:{ action in
            self.board.restartGame()
            self.session = GameSession()
            self.restart = true
            self.showAlert()
               }))
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.cancel, handler: nil))
               self.present(alert, animated: true, completion: nil)
    }
    
}

