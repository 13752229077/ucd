//
//  DiscBehaviour.swift
//  Connect4
//
//  Created by Daniel on 30/03/2020.
//  Copyright © 2020 CS UCD. All rights reserved.
//

import UIKit

// Class to add disc behaviour
class DiscBehaviour: UIDynamicBehavior {
    var animator = UIDynamicAnimator()
    var referenceView: UIView!
    var discViews = [UIView]()
    var gravity = UIGravityBehavior()
    private lazy var collider: UICollisionBehavior = {
        let collider = UICollisionBehavior()
        collider.translatesReferenceBoundsIntoBoundary = true
        return collider
    }()
    private lazy var itemBehavior : UIDynamicItemBehavior = {
        let behavior = UIDynamicItemBehavior()
        behavior.allowsRotation = true
        behavior.elasticity = 0.1 // looked natural at 0.1
        return behavior
    }()

    override init() {}
    
    init(  referenceView: UIView) {
        super.init()
        
        self.referenceView = referenceView
        
        self.animator = UIDynamicAnimator(referenceView: referenceView)
        
        animator.addBehavior(gravity)
        animator.addBehavior(collider)
        animator.addBehavior(itemBehavior)
    }
    // add a disc to view with behaviour
    func addDisc(_ disc: UIView) {
        animator.referenceView?.addSubview(disc)
        discViews.append(disc)
        gravity.addItem(disc)
        collider.addItem(disc)
        itemBehavior.addItem(disc)
    }
    // remove from board
    func removeDisc(_ disc: UIView) {
        gravity.removeItem(disc)
        collider.removeItem(disc)
        itemBehavior.removeItem(disc)
        disc.removeFromSuperview()
    }
    // add barrier for line given
    func addBarrier(identifier: NSCopying, from: CGPoint, to: CGPoint) {
        collider.addBoundary(withIdentifier: identifier, from: from, to: to)
    }
    
    func addRect(identifier: NSCopying, rect: CGRect) {
        collider.addBoundary(withIdentifier: identifier, for: UIBezierPath(rect: rect))
    }
    // remove all barriers so disc drop
    func removeAllBarriers() {
        collider.removeAllBoundaries()
        
    }

}

