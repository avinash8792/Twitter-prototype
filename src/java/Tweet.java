/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Tweet {
    String tweetDateTime;
    String tweet;
    String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

   
    public String getTweetDateTime() {
        return tweetDateTime;
    }

    public void setTweetDateTime(String tweetDateTime) {
        this.tweetDateTime = tweetDateTime;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Tweet() {
    }
    
}
