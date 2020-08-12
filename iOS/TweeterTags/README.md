**<span class="underline">Readme - Part 1 </span>**

Step 1.

• I applied for a Twitter Developer account and registered the app idea.

• Upon confirmation for the app I tested that Twitter’s authentication was
working.

• I created a new **UITableViewController** called **TweetsTVC** and
replaced the existing View Controller in storyboard.

• I added the model (**var tweets = \[\[TwitterTweet\]\]**), the search
property **twitterQueryText** along with the **refresh()** method to
fetch new Tweets.

**let** request = TwitterRequest(search: twitterQueryText, count: 50)

    DispatchQueue.global(qos: .background).async {

    request.fetchTweets { (fetchedTweets) -\> Void **in**

This segment of the refresh() method uses the twitterQueryText to create
a TwitterRequest to return 50 tweets. This request then fetches the
tweets asynchronously so as not to block the UI.

• I added the appropriate code to the didSet() property observer of
**twitterQueryText** and implemented all required tableview datasource
delegate methods.

Step 2.

• I added the “Twitter Query” textfield and wired it up to the
**twitterQueryTextField** outlet.

• I added the function **textFieldShouldReturn(\_** textField:
UITextField) -\> Bool to search an entered query upon pressing the enter
key. Passing to the next first responder with

twitterQueryTextField.resignFirstResponder()

• I created a new **TweetTVCell** class (in TweetsTVC) and customised the
cell![](.//media/image1.png) with the following:

• I added all necessary constraints and initialised the tableView’s
**estimatedRowHeight** and **tableView.rowHeight=
UITableView.automaticDimension**.

• I added the following tableView datasource methods which take care of
initialising the tableView rows.

**override** **func** numberOfSections(in tableView: UITableView) -\>
Int {

**return** tweets.count

}

**override** **func** tableView(**\_** tableView: UITableView,
numberOfRowsInSection section: Int) -\> Int {

**return** tweets\[section\].count

}

**override** **func** tableView(**\_** tableView: UITableView,
cellForRowAt indexPath: IndexPath) -\> UITableViewCell {

**let** dequeuedCell = tableView.dequeueReusableCell(withIdentifier:
"Tweet", for: indexPath) **as**\! TweetTVCell

dequeuedCell.tweet = tweets\[indexPath.section\]\[indexPath.row\]

**return** dequeuedCell

}

• In the **TweetTVCell** class I added the outlets for the above UILabels
and UIImageView and assigned the handleLabel for the user. The profile
picture is downloaded from the URL using the following code:

**if** **let** url = tweet\!.user.profileImageURL {

**if** **let** imageData = **try**? Data(contentsOf: url) {

profileImageView.image = UIImage(data: imageData)

}

}

• The date was a bit tricker as I wanted to display it in the following
format:

Posted 1 hour or less -\> Xm eg. 53m

1h \< Posted \< 2h -\> 1h Xm eg. 1h 53m

2h \< Posted \< 24h -\> Yh Xm eg. 7h 53m

24h \< Posted -\> Zd eg. 4d

• The tweet itself was initially just plain text.

**<span class="underline">Readme - Part 2</span>**

• To highlight the different aspects of a tweet (hashtags, URLs, handles)
I made use of the NSAttributed string class

**let** attributedString = NSMutableAttributedString(string:
tweet\!.text)

• It took me a lot of messing around to get the orientation of the labels
picture correct but the following seem good:![](.//media/image2.png)

• I next created the MentionsTVC class and designed the corresponding
UITableViewController. The only real set up in terms of dragging UI
components was for the UIImageView row of this table. The remaining rows
I just left blank but ensured I gave them all meaningful names for
dequeuing later. This gave a section each to images, hashtags, URLs and
users.

• For the method that returns the header of each section I used the
following:

**switch** section {

**case** 0:

**return** (tweet\!.media.count) \> 0 ? "Images" : “”

• If a user clicks either a hashtag or a user then that term is used to
perform a search.

• If a user clicks a link it is opened up in Safari using the following:

UIApplication.shared.open(URL(string:
(tweet?.urls\[indexPath.row\].keyword)\!)\!)

• If a user clicks an image a new ImageVC Scene opens with the image
zoomed. I added two Segues to control this behaviour. 1 for the images:

**if** segue.identifier == "showZoom" {

**if** **let** imageVC = segue.destination **as**? ImageVC {

imageVC.title = tweet?.user.screenName

imageVC.url = tweet?.media\[0\].url

}

}

• The other Segue covers both options as they’re both just searches using
the **twitterQueryText**:

**else** {

**if** **let** destination = segue.destination **as**? TweetsTVC{

**if** **let** cell = sender **as**? UITableViewCell {

destination.twitterQueryText = (cell.textLabel?.text\!)\!

}

}

}

• The design and implementation of the ImageVC was next but took the
longest by far as there are many intricacies of working with images and
scrollViews. I eventually got it working ok with the following
behaviour.

Image opens zoomed in so as no whitespace on horizontal or vertical
margins.

A double tap zooms and zooms to the are you tap (that location becomes
the centre of a CGRect for **scrollView.zoom():**

scrollView.zoom(to: getRectangle(scale: scrollView.maximumZoomScale,
center: sender.location(in: sender.view)), animated: **true**)

A triple tap zooms all the way out (so image is scaled in window):

scrollView.zoom(to: getRectangle(scale: scrollView.minimumZoomScale,
center: CGPoint(x: image\!.size.width/2.0, y: image\!.size.height/2.0)),
animated: **true**)

• Finally, I added persistence by use of the User Defaults. Upon calling
of the **twitterQueryText** property observer, the allocated String is
stored in the User Defaults under the key
“twitterHi![](.//media/image3.png)story”. A new Scene to display these
was created with the initial Navigation Controller changed to a
UITabBarController so there is a quick way to get to and from Search and
History
(Recents)![](.//media/image4.png)

![](.//media/image5.png)![](.//media/image6.png)![](.//media/image7.png)![](.//media/image8.png)

![](.//media/image9.png)
