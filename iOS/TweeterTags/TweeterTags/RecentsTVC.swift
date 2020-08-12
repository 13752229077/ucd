//
//  RecentsTVC.swift
//  TweeterTags
//
//  Created by Daniel on 21/03/2020.
//  Copyright © 2020 COMP47390-41550. All rights reserved.
//

import UIKit

class RecentsTCV: UITableViewController {
    
    var history:[String]? {
        return UserDefaults.standard.object(forKey: "twitterHistory") as? [String]
    }
    
    // MARK: - VC lifecyle
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    // MARK: -  Table view data source
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "historyCell" , for: indexPath)
        cell.textLabel?.text = history![indexPath.row]
        return cell
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return history == nil ? 0 : history!.count
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "historySearchSegue" {
            if let destination =  segue.destination as? TweetsTVC{
                if let cell =  sender as? UITableViewCell{
                    destination.twitterQueryText = (cell.textLabel?.text)!
                }
            }
        }
    }
}
