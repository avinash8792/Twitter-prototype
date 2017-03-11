/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vivekreddy
 */
public class tweets {
    
    private String userid;
    private String tweets;
    private String date;
    private int tweetid;
    
    
   public tweets(String u,String tw,String date1,int tweetid1)
   {
   
       userid=u;
       tweets=tw;
       date=date1;
       tweetid=tweetid1;
       
   
   }
   public tweets()
   {
   
   }

    public int getTweetid() {
        return tweetid;
    }

    public void setTweetid(int tweetid) {
        this.tweetid = tweetid;
    }

   
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTweets() {
        return tweets;
    }

    public void setTweets(String tweets) {
        this.tweets = tweets;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
   
   
   
    
}
