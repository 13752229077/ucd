//
//  ImageVC.swift
//  TweeterTags
//
//  Created by Daniel on 19/03/2020.
//  Copyright © 2020 COMP47390-41550. All rights reserved.
//

import UIKit

class ImageVC: UIViewController {
    private var imageView: UIImageView!
    private var scrollView: UIScrollView!
    var url: URL? {
        didSet {
            getImage()
        }
    }
    
    private func getImage() {
        if let url = url {
            let imageData = try? Data(contentsOf: url)
            if imageData != nil {
                self.image = UIImage(data: imageData!)
            }
            else { self.image = nil }
        }
    }

    private var image: UIImage? {
        get {
            return imageView.image
        }
        set {
            imageView = UIImageView()
            imageView.image = newValue
            imageView.sizeToFit()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
    }
    
    func setup() {
        scrollView = UIScrollView(frame: view.bounds)
        scrollView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        scrollView.backgroundColor = .black
        scrollView.contentSize = imageView.bounds.size
        scrollView.delegate = self
        scrollView.addSubview(imageView)
        
        let tripTapGesture = UITapGestureRecognizer(target: self, action: #selector(self.tripleTap(_:)))
        tripTapGesture.numberOfTapsRequired = 3
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.doubleTap(_:)))
        tapGesture.numberOfTapsRequired = 2
        tapGesture.require(toFail: tripTapGesture)
        view.addGestureRecognizer(tapGesture)
        view.addGestureRecognizer(tripTapGesture)
        view.addSubview(scrollView)
    }
    @objc func doubleTap(_ sender: UITapGestureRecognizer) {
        if scrollView.zoomScale == 1 {
            scrollView.zoom(to: getRectangle(scale: scrollView.maximumZoomScale, center: sender.location(in: sender.view)), animated: true)
        }else {
            scrollView.setZoomScale(1, animated: true)
        }
    }
    
    @objc func tripleTap(_ sender: UITapGestureRecognizer) {
        if scrollView.minimumZoomScale == 1 {
            setZoom(for: scrollView.bounds.size, reset: false)
            scrollView.zoom(to: getRectangle(scale: scrollView.minimumZoomScale, center: CGPoint(x: image!.size.width/2.0, y: image!.size.height/2.0)), animated: true)
        }
    }
    
    
}

extension ImageVC: UIScrollViewDelegate {
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return imageView
    }
    
    func getRectangle(scale: CGFloat, center: CGPoint) -> CGRect {
        let height = imageView.frame.size.height / scale
        let width = imageView.frame.size.width  / scale
        let center = scrollView.convert(center, from: imageView)
        
        return CGRect(x: (center.x - (width / 2.0)), y: (center.y - (height / 2.0)), width: width, height: height)
    }
    
    func setZoom(for scrollViewSize: CGSize, reset: Bool) {
        let imageSize = imageView.bounds.size
        let wScale = scrollViewSize.width / imageSize.width
        let hScale = scrollViewSize.height / imageSize.height
        let scale = reset ? max(wScale, hScale) : min(wScale, hScale)
        
        scrollView.minimumZoomScale = scale
        scrollView.maximumZoomScale = 4.0
    }
    
}
