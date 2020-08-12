//
//  TweetsTVC.swift
//  TweeterTags
//
//  Created by Daniel on 18/03/2020.
//  Copyright © 2020 COMP47390-41550. All rights reserved.
//

import UIKit

class TweetsTVC: UITableViewController, UITextFieldDelegate {
    @IBOutlet weak var twitterQueryTextField: UITextField! {
        didSet {
            twitterQueryTextField.delegate = self
            twitterQueryTextField.text = twitterQueryText
        }
    }
    
    var tweets = [[TwitterTweet]]() {
        didSet {
            self.tableView.reloadData()
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        twitterQueryText = textField.text!
        twitterQueryTextField.resignFirstResponder()
        return true
    }
    
    var twitterQueryText : String = "#ucd"{
        didSet{
            let defaults = UserDefaults.standard
            var history = [String]()
            if let hist = defaults.object(forKey: "twitterHistory") as? [String] {
                history = hist
                if(history.last != twitterQueryText && twitterQueryText != "#ucd"){
                    if (history.contains(twitterQueryText)) {
                        if let index = history.firstIndex(of: twitterQueryText) {
                            history.remove(at: index)
                        }
                    }
                    history.append(twitterQueryText)
                }
                if history.count > 49 { // save last 50 searches
                    history.removeLast()
                }
            }
            
                
            defaults.set(history, forKey: "twitterHistory")
            twitterQueryTextField?.text = twitterQueryText
            tweets.removeAll()
            self.tableView.reloadData()
            refresh()
            
        }
    }
    
    
    // MARK: - VC lifecyle
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view
        tableView.estimatedRowHeight = tableView.rowHeight
        tableView.rowHeight = UITableView.automaticDimension
        refresh()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refresh() {
        let request = TwitterRequest(search: twitterQueryText, count: 50)
        DispatchQueue.global(qos: .background).async {
            request.fetchTweets { (fetchedTweets) -> Void in
                if fetchedTweets.count > 0 {
                    DispatchQueue.main.async { () -> Void in
                    self.tweets.insert(fetchedTweets, at: 0)
                    }
                }
            }
        }
        
    }
    
    
    
    
    // MARK: - Table view datasource
    override func numberOfSections(in tableView: UITableView) -> Int {
        return tweets.count
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return tweets[section].count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let dequeuedCell = tableView.dequeueReusableCell(withIdentifier: "Tweet", for: indexPath) as! TweetTVCell
        dequeuedCell.tweet = tweets[indexPath.section][indexPath.row]
        
        return dequeuedCell
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "segueToMention"{
            if let destination =  segue.destination as? MentionsTVC{
                if let index = self.tableView.indexPathForSelectedRow{
                    destination.tweet = tweets[index.section][index.row]
                }
            }
        }
    }
}
    
// MARK: - TweetTVCell class
class TweetTVCell: UITableViewCell {
    @IBOutlet weak var handleLabel: UILabel!
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var tweetLabel: UILabel!
    
    var tweet: TwitterTweet? {
        didSet {
            fetch()
        }
    }
    
    func fetch() {
        if let tweet = self.tweet {
            handleLabel?.text = tweet.user.description
            setProfile()
            setDate()
            setTweet()
        }
    }
    
    func setProfile() {
        if let url = tweet!.user.profileImageURL {
            if let imageData = try? Data(contentsOf: url) {
                profileImageView.image = UIImage(data: imageData)
            }
        }
    }

    func setDate() {
        let secondsSincePost = tweet!.created.distance(to: Date())
        if secondsSincePost > 86400 {
            dateLabel?.text = "\(Int(secondsSincePost)%86400/3600)" + "d"
        }
        else if secondsSincePost > 7200 {
            dateLabel?.text = "\(Int(secondsSincePost/3600))" + "h"
        }
        else if secondsSincePost < 3600 {
            dateLabel?.text = "\(Int(secondsSincePost/60))" + "m"
        }
        else {
            dateLabel?.text = "\(Int(secondsSincePost/3600))" + "h " + "\(Int(secondsSincePost/60)%60)" + "m"
        }
    }
    
    func setTweet() {
        if tweetLabel?.text != nil  {
            let attributedString = NSMutableAttributedString(string: tweet!.text)
            for hashtag in tweet!.hashtags {
                attributedString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.blue, range: hashtag.nsrange)
            }
            for handle in tweet!.userMentions {
                attributedString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.green, range: handle.nsrange)
            }
            for url in tweet!.urls {
                attributedString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.red, range: url.nsrange)
            }
            
            tweetLabel?.attributedText = attributedString
        }
    }
    
}
