//
//  MentionsTVC.swift
//  TweeterTags
//
//  Created by Daniel on 19/03/2020.
//  Copyright © 2020 COMP47390-41550. All rights reserved.
//

import UIKit

class MentionsTVC: UITableViewController {

    var tweet: TwitterTweet?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = tweet?.user.name
        tableView.estimatedRowHeight = tableView.rowHeight
        tableView.rowHeight = UITableView.automaticDimension
    }
    
    
    // MARK: - Table view datasource
    override func numberOfSections(in tableView: UITableView) -> Int { return 4 }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    switch section{
        case 0:
            return (tweet!.media.count)
        case 1:
            return (tweet!.hashtags.count)
        case 2:
            return (tweet!.urls.count)
        case 3:
            return (tweet!.userMentions.count)
        default:
            return 0
        }
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return indexPath.section == 0 ? CGFloat(350) : UITableView.automaticDimension
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
            case 0:
                let cell = tableView.dequeueReusableCell(withIdentifier: "images",for: indexPath)
                if let imageData = try? Data(contentsOf: (tweet!.media[indexPath.row].url)) {
                    cell.imageView?.image = UIImage(data: imageData)
                }
                return cell
            
            case 1:
                let cell = tableView.dequeueReusableCell(withIdentifier: "hashtags",for: indexPath)
                cell.textLabel?.text = tweet!.hashtags[indexPath.row].keyword
                return cell
            
            case 2:
                let cell = tableView.dequeueReusableCell(withIdentifier: "urls",for: indexPath)
                cell.textLabel?.text = tweet!.urls[indexPath.row].keyword
                return cell
            
            case 3:
                let cell = tableView.dequeueReusableCell(withIdentifier: "users",for: indexPath)
                cell.textLabel?.text = tweet!.userMentions[indexPath.row].keyword
                return cell
            
            default:
                return tableView.dequeueReusableCell(withIdentifier: "",for: indexPath) as UITableViewCell
            }
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 0:
            return (tweet!.media.count) > 0 ? "Images" : ""
        case 1:
            return (tweet!.hashtags.count) > 0 ? "Hashtags" : ""
        case 2:
            return (tweet!.urls.count) > 0 ? "URLs" : ""
        case 3:
            return (tweet!.userMentions.count) > 0 ? "Mentions" : ""
        default:
            return ""
        }
    }
      
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 2 {
            UIApplication.shared.open(URL(string: (tweet?.urls[indexPath.row].keyword)!)!)
            
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if sender != nil && self.tableView.indexPathForSelectedRow != nil{
            if segue.identifier == "showZoom" {
                if let imageVC =  segue.destination as? ImageVC {
                    imageVC.title = tweet?.user.screenName
                    imageVC.url = tweet?.media[0].url
                }
            }
            else {
                if let destination =  segue.destination as? TweetsTVC{
                    if let cell = sender as? UITableViewCell {
                        destination.twitterQueryText = (cell.textLabel?.text!)!
                    }
                }
            }
        }
    }
}
