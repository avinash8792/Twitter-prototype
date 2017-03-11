/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vivekreddy
 */
@Named(value = "tweetfunction")
@RequestScoped
public class tweetfunction {

    /**
     * Creates a new instance of tweetfunction
     */
    private ArrayList<tweets>alltweets= new ArrayList<>();
     private ArrayList<allmessages>allmsg= new ArrayList<>();
     

    public ArrayList<allmessages> getAllmsg() {
        return allmsg;
    }

    public void setAllmsg(ArrayList<allmessages> allmsg) {
        this.allmsg = allmsg;
    }
     
   private static String previd=""; 
   
    private static int prevtweetid=0; 
    private message seletedtweetmsg;
    private tweets selecttweet=null;

    public message getSeletedtweetmsg() {
        return seletedtweetmsg;
    }

    public void setSeletedtweetmsg(message seletedtweetmsg) {
        this.seletedtweetmsg = seletedtweetmsg;
    }
    
    
          private String userid;
    private String tweet;

    public ArrayList<tweets> getAlltweets() {
        return alltweets;
    }

    public void setAlltweets(ArrayList<tweets> alltweets) {
        this.alltweets = alltweets;
    }

    public tweets getSelecttweet() {
        return selecttweet;
    }

    public void setSelecttweet(tweets selecttweet) {
        this.selecttweet = selecttweet;
    }
    
    

    
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
    
    @PostConstruct
    public void inti()
    {
    
         HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
         try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception e) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
          ResultSet rs1 = null;
          ResultSet rs2 = null;
        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
            
            //for test we are using this 
            rs2=stat.executeQuery("select * from twittertweetdetails where UserID in ('"+userid+"') order by tweetdatetime desc");
          
          while(rs2.next())
          {
          
               alltweets.add(new tweets(rs2.getString(2),rs2.getString(3) , rs2.getString(5), rs2.getInt(1)));
              //electtweet=new tweets();
          
          }
            
            
            
                    //to get the following people and add to news feed
           rs1=stat.executeQuery("select FollowingID from twitterfollowdetails where UserID='"+userid+"'");
            while(rs1.next())
            {
                
                 rs=stat.executeQuery("select * from twittertweetdetails where UserID in ('"+rs1.getString("FollowingID")+"') order by tweetdatetime desc");
          
          while(rs.next())
          {
          
               alltweets.add(new tweets(rs.getString(2),rs.getString(3) , rs.getString(5), rs.getInt(1)));
               selecttweet=new tweets();
          
          }
            
            }
    
         

 
                
                
        
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        } finally {
            try {
                 rs2.close();
                rs1.close();
                rs.close();
                stat.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    
   
    
    }
    
    
    public String retweet(tweets e)
    {
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
        
        System.out.println(e.getTweets());
      String day=DateAndTime.DateTime();
      
      
    
    try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception x) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
           
          

          
                //insert a record into twitterretweetdetails 
               int r = stat.executeUpdate(
                        "insert into twitterretweetdetails values ( null, '" + e.getUserid() + "', '" + e.getTweetid() + "','"+userid+"') "); 
                
                
                int r1 = stat.executeUpdate(
                        "insert into twittertweetdetails values ( null, '" + userid + "', '" + e.getTweets() + "',null,'"+day+"') "); 
                
        
        } catch (SQLException x) {
            x.printStackTrace();
            
            
        } finally {
            try {
               
                stat.close();
                conn.close();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        
        return("newtweet");
    
    }
    
    
    
    
    public String like(tweets e)
    {
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
    
        String day=DateAndTime.DateTime();
      
      
    
    try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception x) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
            //make sure ssn is not used or id is not used
          

          
                //insert a record twitterlikedetails
               int r = stat.executeUpdate(
                        "insert into twitterlikedetails values ( null, '" + e.getUserid() + "', '" + e.getTweetid() + "','"+userid+"') "); 
                
                
        
        } catch (SQLException x) {
            x.printStackTrace();
            
            
        } finally {
            try {
                rs.close();
                stat.close();
                conn.close();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    
        return("like");
    }
    
    
    
    
    
    public String dotweet()
    {
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
         String day=DateAndTime.DateTime();
    try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception e) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
         
          

          
                //insert a record into twittertweetdetails 
                int r = stat.executeUpdate(
                        "insert into twittertweetdetails values ( null, '" + userid + "', '" + tweet + "',null,'"+day+"') "); 
                
                
        
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        } finally {
            try {
                rs.close();
                stat.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    
    return("newtweet");
    
    }
    
    
    public String reply(tweets e)
    {
    
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
    seletedtweetmsg=new message(e.getUserid(), e.getTweetid(),e.getTweets());
    previd=e.getUserid();
    prevtweetid=e.getTweetid();
        allmessages(e.getTweetid());
    return("message");
    }
    
    
    public void allmessages(int i)
    {
    
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
         try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception e) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
          ResultSet rs1 = null;
        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
            
            //for test we are using this 
            
            
            rs1=stat.executeQuery("select * from twitterreplydetails where TweetID='"+i+"'");
            
                    //to get the following people and add to msessage
           //rs1=stat.executeQuery("select FollowingID from twitterfollowdetails where UserID='"+userid+"'");
            while(rs1.next())
            {
                
        allmsg.add(new allmessages(rs1.getString(4), rs1.getString(5)));
            
            }
    
         

 
                
                
        
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        } finally {
            try {
                rs1.close();
                rs.close();
                stat.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    
    
    }
    
     public String domessage()
    {
        HttpSession hs = Util.getSession();
         userid = (String) hs.getAttribute("user");
    try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception x) {
            
        }

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";

        Connection conn = null;
        Statement stat = null;
    

        try {
            //connect to DB
            conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
            stat = conn.createStatement();
         
          

          
                //insert a record into twittertmessage details
          
              
                 int r = stat.executeUpdate(
                        "insert into twitterreplydetails values ( null, '" + previd+ "', '" + prevtweetid + "','"+userid+"','"+tweet+"') "); 
                
                
        
        } catch (SQLException x) {
            x.printStackTrace();
            
            
        } finally {
            try {
            
                stat.close();
                conn.close();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        
    allmessages(prevtweetid);
    return("replay");
    
    }
    
    
    
    
    }
    

