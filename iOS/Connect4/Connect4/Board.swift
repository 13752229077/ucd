//
//  Board2.swift
//  Connect4
//
//  Created by Daniel on 28/03/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//

//
import Alpha0Connect4

import UIKit

class Board: UIView {
    // UIDynamicBehavior
    var discBehavior = DiscBehaviour()
    // Views
    public var board = UIView()
    public var view = UIView()
    // Dict of disc views
    var discs : [String: UIView] = [:]
    // Game Board parameters
    var discSize: CGFloat = 0.0
    var margin: CGFloat = 5.0
    // Current game attributes
    var sequence: String = ""
    lazy var user: String = "user"
    private var lastDisc = -1
    // To ensure disc have finished dropping
    var isAnimating : Bool {
        return self.discBehavior.animator.isRunning
    }
    // Default disc and board colours, modified by shuffle button
    var userColour: UIColor = UIColor.yellow
    var botColour: UIColor = UIColor.red
    var boardColour: UIColor = UIColor.blue
    
    // Draw board and add barriers for bottom and sides
    func drawBoard(frame: CGRect) {
        // set up board attributes
        discBehavior = DiscBehaviour(referenceView: self)
        
        self.board.frame = frame
        self.board.backgroundColor = boardColour
        
        discSize = (frame.width - (margin*14)) / 7
        var x_offset: CGFloat = margin
        var y_offset: CGFloat = margin
        
        // draw slots
        for i in 1...6 {
            for j in 0...6 {
                self.board.addSubview(getDiscView(x: x_offset, y: y_offset, index: ((i-1)*7+j), turn:-1, fillColour: UIColor.white.cgColor, text: ""
                ))
                x_offset += discSize + 2 * margin
            }
            y_offset += discSize + 2 * margin
            x_offset = margin
        }
        // add barriers
        (x_offset, y_offset) = (0,0)
        for i in 0...6 {
            discBehavior.addRect(identifier: "left_margin"+i.description as NSCopying, rect: CGRect(x: x_offset, y: y_offset, width: 2, height: frame.height))
            x_offset += discSize+(2*margin)
            discBehavior.addRect(identifier: "right_margin"+i.description as NSCopying, rect: CGRect(x: x_offset, y: y_offset, width: 2, height: frame.height))
            x_offset += discSize+(2*margin)
        }
        discBehavior.addBarrier(identifier: "bottom_margin" as NSCopying, from: CGPoint(x: 0, y: frame.height-15), to: CGPoint(x: frame.width, y: frame.height-15))
        
        self.addSubview(board)
    }
    
    // Create disc view
    // used to create views for both empty slots and discs
    func getDiscView(x: CGFloat, y: CGFloat, index: Int, turn: Int, fillColour: CGColor, text: String) -> UIView {
        // create circle layer
        let layer = CAShapeLayer()
        layer.path = UIBezierPath(ovalIn: CGRect(x: 0, y: 0, width: discSize, height: discSize)).cgPath
        layer.lineWidth = 0.8
        layer.fillColor = fillColour
        layer.name = index.description
        // add to disc view
        let discView = UIView(frame: CGRect(x: x, y: y, width: discSize, height: discSize))
        discView.tag = index
        discView.layer.addSublayer(layer)
        // if disc, set user label and tag then add to disc view
        if text != "" {
            discView.clipsToBounds = true
            let discLabel  = UILabel(frame: CGRect(x: 0, y: 0, width: discSize, height: discSize))
            discLabel.tag = turn
            
            let strokeTextAttributes: [NSAttributedString.Key : Any] = [
                .strokeColor : UIColor.black,
                .foregroundColor : UIColor.white,
                .strokeWidth : -2.0
                ]
            
            discLabel.attributedText = NSAttributedString(string: text, attributes: strokeTextAttributes)
            discLabel.textAlignment = .center
            discView.addSubview(discLabel)
        }
        return discView
    }
    
    // get frame of disc with tag
    func getDiscFrame(tag: Int) -> CGRect? {
        for view in self.subviews.first!.subviews {
            if(view.tag == tag){
                return view.frame
            }
        }
        return CGRect(x: 0, y: 0, width: 0, height: 0)
    }
    
    // add disc to board
    func addDisc(index: Int, tag: Int) {
        // add barrier for previous disc
        board.isUserInteractionEnabled = false
        let idx = getFreeSlot(column: tag % 7)
        if (lastDisc != -1) { addDiscBarrier(lastSlot: getDiscFrame( tag: lastDisc)!) }
        lastDisc = idx
        sequence += "," + idx.description
        // get frame and view of disc
        let discFrame = getDiscFrame(tag: idx % 7)
        var colour: UIColor
        var label = ""
        (colour, user, label) = user == "user" ? (userColour, "α-C4", "U") : (botColour, "user", "A")
        let discView = getDiscView(x: discFrame!.origin.x, y:-160, index: idx, turn: index, fillColour: colour.cgColor, text: label)
        // add disc to board
        self.discBehavior.addDisc(discView)
        discs.updateValue(discView, forKey: label+idx.description)
        discBehavior.animator.delegate = self
    }
    
    // add barrier above disc so it sits in slot
    func addDiscBarrier(lastSlot: CGRect) {
        self.discBehavior.addBarrier(identifier: ("top_barrier"+discs.count.description as NSCopying), from: CGPoint(x: lastSlot.minX, y:lastSlot.minY-(2*margin)+1), to: CGPoint(x: lastSlot.maxX+margin, y: lastSlot.minY-(2*margin)+1))
    }
    // find free slot for column
    func getFreeSlot(column: Int) -> (Int) {
        for i in stride(from: column+35, to: column-7, by: -7) {
            let userInSlot = discs["U"+i.description] != nil
            let botInSlot = discs["A"+i.description] != nil
            if (!userInSlot && !botInSlot) {
                return i
            }
        }
        return 0
    }
    
    // remove barriers and let gravity do its thing
    func restartGame() {
        discBehavior.removeAllBarriers()
        self.discs.removeAll()
        sequence = ""
    }
    
    // randomise board and disc colours
    func randomiseColours() {
        let newUserColour: UIColor = .random
        let newBotColour: UIColor = .random
        boardColour = .random
        self.board.backgroundColor = boardColour
        board.setNeedsDisplay()
        for (key, value) in discs {
            var colour: CGColor
            if key[key.startIndex] == "U" {
                (colour, userColour) = (newUserColour.cgColor, newUserColour)
            } else {
                (colour, botColour) = (newBotColour.cgColor, newBotColour)
            }
            let layer: CAShapeLayer = value.layer.sublayers![0] as! CAShapeLayer
            layer.fillColor = colour
            value.layer.sublayers![0] = layer
        }
    }
    
    // highlight pieces by adding sequence
    func highlightPieces(player: String) {
        for (key, value) in discs {
            let label = value.subviews[0] as! UILabel
            label.font = UIFont.boldSystemFont(ofSize: 30.0)
            label.text = label.tag.description
            if key.lowercased()[key.startIndex] == player.lowercased()[player.startIndex] {
                label.font = UIFont.boldSystemFont(ofSize: 30.0)
            }
            
        }
    }
    
}

//MARK:- UIDynamicAnimator Delegate
extension Board : UIDynamicAnimatorDelegate {
    func dynamicAnimatorDidPause(_ animator: UIDynamicAnimator) {
        self.view.isUserInteractionEnabled = true
        self.board.isUserInteractionEnabled = true
    }
}
// random colour extension
extension UIColor {
    static var random: UIColor {
        return UIColor(red: .random(in: 0...1),
                       green: .random(in: 0...1),
                       blue: .random(in: 0...1),
                       alpha: 1.0)
    }
}
