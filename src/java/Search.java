/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Shrey
 */
@Named(value = "search")
@SessionScoped
public class Search implements Serializable {

    /**
     * Creates a new instance of Search
     */
    
    private String loginid;
    private String searchLoginID;
    final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    private String following;
    private ArrayList<FollowResult> followResult = new ArrayList<>();
    private ArrayList<FollowersResult> followersResult = new ArrayList<>();
    private ArrayList<String> blockfollowersResult = new ArrayList<>();
    private ArrayList<HashTag> hashTagDetails = new ArrayList<>();
    private ArrayList<Profile> profileDetails = new  ArrayList<>();
    private ArrayList<Tweet> tweetDetails = new  ArrayList<>();

    
    // get and set methods
    public ArrayList<HashTag> getHashTagDetails() {
        return hashTagDetails;
    }

    public void setHashTagDetails(ArrayList<HashTag> hashTagDetails) {
        this.hashTagDetails = hashTagDetails;
    }

    public ArrayList<Tweet> getTweetDetails() {
        return tweetDetails;
    }

    public void setTweetDetails(ArrayList<Tweet> tweetDetails) {
        this.tweetDetails = tweetDetails;
    }
    
    public ArrayList<Profile> getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ArrayList<Profile> profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ArrayList<FollowersResult> getFollowersResult() {
        return followersResult;
    }

    public void setFollowersResult(ArrayList<FollowersResult> followersResult) {
        this.followersResult = followersResult;
    }

    public ArrayList<FollowResult> getFollowResult() {
        return followResult;
    }

    public void setFollowResult(ArrayList<FollowResult> followResult) {
        this.followResult = followResult;
    }    

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getSearchLoginID() {
        return searchLoginID;
    }

    public void setSearchLoginID(String searchLoginID) {
        this.searchLoginID = searchLoginID;
    }
    
    // search profiles for Peeper
    public String search() 
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
    // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
            return("internalError");
        }
         try
                {
                    conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
                    st=conn.createStatement();
                    ResultSet rs2 = null;
                    blockfollowersResult.clear();
                    String slct2="select * from twitterblockdetails where userid = '"+loginid+"' or blockuserid = '"+loginid+"'";
                    rs2=st.executeQuery(slct2);
                    while (rs2.next())
                    {
                       
                        blockfollowersResult.add(rs2.getString("blockuserid"));
                        blockfollowersResult.add(rs2.getString("userid"));
                    }
                    String slct="select * from twitterprofiledetails where (userid like '%"+searchLoginID+"%' or fullname like '%"+searchLoginID+"%') and userid <> '"+loginid+"' and userid not in (select followingid from twitterfollowdetails where userid = '"+loginid+"')";
                    rs=st.executeQuery(slct);
                    int count=0;
                    followResult.clear();
                    while (rs.next())
                    {
                        count++;
                        FollowResult f = new FollowResult();
                        FollowersResult ff = new FollowersResult();
                        f.setFullname(rs.getString("fullname"));
                        f.setSearchloginID(rs.getString("userid"));
                        ff.setFollowloginID(rs.getString("userid"));
                        if(blockfollowersResult.contains(rs.getString("userid")))
                        {
                           
                        }
                        else
                        {
                            followResult.add(f);
                        }
                        
                    }
                    
                    if(count==0 || followResult.size()==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("searchresults");
                       
                    }
                    }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    try
                    {
                        conn.close();
                        st.close();
                        rs.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }            
                }   
        
        return "hello";
    }
    // Details about Peeper Profile
    public String profiledetails()
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
         // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
            return("internalError");
        }
         try
                {
                    conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
                    st=conn.createStatement();
                    String slct3="SELECT * FROM `twitterprofiledetails` WHERE userid = '"+loginid+"'";
                    rs=st.executeQuery(slct3);
                    
                    Profile p = new Profile();
                    int count=0;
                    profileDetails.clear();
                    while (rs.next())
                    {
                        count++;
                        
                        p.setProfileName(rs.getString("fullname"));
                        
                    }
                    
                 
                    
                    rs.close();
                    if(count!=0)
                    {
                    String slct2="SELECT count(followingid) as followers FROM `twitterfollowdetails` WHERE followingid = '"+loginid+"'";
                    rs=st.executeQuery(slct2);
                    
                    
                    while (rs.next())
                    {
                        count++;
                        
                        p.setFollowers(rs.getInt("followers"));
                        
                    }
                    rs.close();
                    String slct="SELECT count(userid) as following FROM `twitterfollowdetails` WHERE userid = '"+loginid+"'";
                    rs=st.executeQuery(slct);
                    while (rs.next())
                    {
                        count++;                        
                        p.setProfileID(loginid);
                        p.setFollowing(rs.getInt("following"));
                      
                    }
                    rs.close();
                    
                    String slct4="SELECT count(tweetid) as tweets FROM `twittertweetdetails` WHERE userid = '"+loginid+"'";
                    rs=st.executeQuery(slct4);
                    
                    
                    while (rs.next())
                    {
                        count++;
                        
                        p.setTweets(rs.getInt("tweets"));
                        
                    }
                    profileDetails.add(p);                    
                    }
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("profileresults");
                       
                    }
                    }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    try
                    {
                        conn.close();
                        st.close();
                        rs.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }            
                }   
        return "hello";
    }
    // Block logic
    public String blck() 
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
    // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
            return("internalError");
        }
         try
                {
                    conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
                    st=conn.createStatement();
                    String slct="select * from twitterprofiledetails where (userid like '%"+searchLoginID+"%' or fullname like '%"+searchLoginID+"%') and userid <> '"+loginid+"'";
                    rs=st.executeQuery(slct);
                    int count=0;
                    followersResult.clear();
                    while (rs.next())
                    {
                        count++;
                        FollowersResult f = new FollowersResult();
                        f.setFollowloginID(rs.getString("userid"));
                        followersResult.add(f);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("blockresults");
                       
                    }
                    }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    try
                    {
                        conn.close();
                        st.close();
                        rs.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }            
                }   
        
        return "hello";
    }
     
    
    // Block Peeper Profile
    public String blckfollowers() 
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
    // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
            return("internalError");
        }
         try
                {
                    conn = DriverManager.getConnection(DB_URL, "mehtas1700", "1368776");
                    st=conn.createStatement();
                    String slct="select distinct *  from twitterblockdetails where userid = '"+loginid+"'";
                    rs=st.executeQuery(slct);
                    int count=0;
                    followersResult.clear();
                    while (rs.next())
                    {
                        count++;
                        FollowersResult f = new FollowersResult();
                        f.setFollowloginID(rs.getString("blockuserid"));
                        followersResult.add(f);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("blockfollowersresults");
                       
                    }
                    }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    try
                    {
                        conn.close();
                        st.close();
                        rs.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }            
                }   
        
        return "hello";
    }
    
    // Follow Peeper Profile
    public String follow(FollowResult f)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(FollowResult fr : followResult)
       {
           if(fr.equals(f))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String insrt="INSERT INTO `twitterfollowdetails`(`UserID`, `FollowingID`) VALUES ( '"+loginid+"', '"+fr.getSearchloginID()+"')";
                   st.executeUpdate(insrt);
                   following=fr.getSearchloginID();
                   return("followsuccess");
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
    
    // Unfollow Peeper Profile
    public String unfollow(FollowersResult f)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(FollowersResult fr : followersResult)
       {
           if(fr.equals(f))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String dlt="delete from `twitterfollowdetails` where `UserID`='"+loginid+"' and `FollowingID` ='"+fr.getFollowloginID()+"'";
                   st.executeUpdate(dlt);
                   following=fr.getFollowloginID();
                   return("unfollowsuccess");
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
    
    //Block with Class Logic
    public String block(FollowersResult f)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(FollowersResult fr : followersResult)
       {
           if(fr.equals(f))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String dlt="delete from `twitterfollowdetails` where (`followingid`='"+loginid+"' and `userid` ='"+fr.getFollowloginID()+"') or (`userid`='"+loginid+"' and `followingid` ='"+fr.getFollowloginID()+"')";
                   st.executeUpdate(dlt);
                   String insrt="INSERT INTO `twitterblockdetails`(`UserID`, `blockuserID`) VALUES ( '"+loginid+"', '"+fr.getFollowloginID()+"')";
                   st.executeUpdate(insrt);
                   following=fr.getFollowloginID();

                   return("blocksuccess");
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
    // Block with Another Class Logic
     public String blockf(FollowResult f)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(FollowResult fr : followResult)
       {
           if(fr.equals(f))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String dlt="delete from `twitterfollowdetails` where (`followingid`='"+loginid+"' and `userid` ='"+fr.getSearchloginID()+"') or (`userid`='"+loginid+"' and `followingid` ='"+fr.getSearchloginID()+"')";
                   st.executeUpdate(dlt);
                   String insrt="INSERT INTO `twitterblockdetails`(`UserID`, `blockuserID`) VALUES ( '"+loginid+"', '"+fr.getSearchloginID()+"')";
                   st.executeUpdate(insrt);
                   following=fr.getSearchloginID();

                   return("blocksuccess");
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
    
     // Unblock Peeper Profile
     public String unblock(FollowersResult f)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(FollowersResult fr : followersResult)
       {
           if(fr.equals(f))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String dlt="delete from `twitterblockdetails` where (`userid`='"+loginid+"' and `blockuserid` ='"+fr.getFollowloginID()+"')";
                   st.executeUpdate(dlt);                   
                   following=fr.getFollowloginID();

                   return("unblocksuccess");
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
     
     // Followes Details
    public String followers()
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
      // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
           // return("internalError");
        }
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String slct="select * from twitterfollowdetails where followingid = '"+loginid+"'";
                   rs= st.executeQuery(slct);
                   int count=0;
                   followersResult.clear();
                   while (rs.next())
                    {
                        count++;
                        FollowersResult f = new FollowersResult();
                        f.setFollowloginID(rs.getString("userid"));                        
                        followersResult.add(f);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("followersresults");
                       
                    }
                   
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           
       
       return "hello";
    }    
    
    
    // Following Detail
    public String following()
    {
      HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
           // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
          //  return("internalError");
        }  
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String slct="select * from twitterfollowdetails where userid = '"+loginid+"'";
                   rs=st.executeQuery(slct);
                   int count=0;
                   followersResult.clear();
                   while (rs.next())
                    {
                        count++;
                        FollowersResult f = new FollowersResult();
                        f.setFollowloginID(rs.getString("followingid"));                        
                        followersResult.add(f);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("followingresults");
                       
                    }
                   
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           
       
       return "hello";
    }
    
    // Tweet Details
    public String tweets()
    {
      HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
           // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
          //  return("internalError");
        }  
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String slct="select * from twittertweetdetails where userid = '"+loginid+"' order by tweetdatetime desc";
                   rs=st.executeQuery(slct);
                   int count=0;
                   tweetDetails.clear();
                   while (rs.next())
                    {
                        count++;
                        Tweet t = new Tweet();
                        t.setTweetDateTime(rs.getString("tweetdatetime")); 
                        t.setTweet(rs.getString("tweet"));
                        t.setUserName(rs.getString("userid"));
                        tweetDetails.add(t);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("tweetresults");
                       
                    }
                   
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           
       
       return "hello";
    }
    
    
    // HashTag Trends Logic
    public String hashtag()
    {
      HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
           // load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
          //  return("internalError");
        }  
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String slct="select * from twitterhashtagdetails";
                   rs=st.executeQuery(slct);
                   int count=0;
                   hashTagDetails.clear();
                   while (rs.next())
                    {
                        count++;
                        HashTag h = new HashTag();
                        h.setHashTagName(rs.getString("hashtagname"));
                        hashTagDetails.add(h);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("hashtagresults");
                       
                    }
                   
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           
       
       return "hello";
    }
    
    // Trend Tweets Details Logic
    public String hashTDetails(HashTag h)
    {
        HttpSession hs = Util.getSession();
         loginid = (String) hs.getAttribute("user");
       for(HashTag hd : hashTagDetails)
       {
           if(hd.equals(h))
           {
             
               try
               {
                   conn= DriverManager.getConnection(DB_URL,"mehtas1700","1368776");
                   st=conn.createStatement();
                   String slct="select * from twittertweetdetails where tweet like '%"+h.getHashTagName()+"%' order by tweetdatetime desc";
                   rs=st.executeQuery(slct);
                   int count=0;
                   tweetDetails.clear();
                   while (rs.next())
                    {
                        count++;
                        Tweet t = new Tweet();
                        t.setTweetDateTime(rs.getString("tweetdatetime")); 
                        t.setTweet(rs.getString("tweet")); 
                        t.setUserName(rs.getString("userid"));
                        tweetDetails.add(t);
                    }
                    
                    if(count==0)
                    {
                        return("searchresultnotfound");
                    }
                    else
                    {
                        return("tweetresults");
                       
                    }
                   
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
               try
               {
                   st.close();
                   rs.close();
                   conn.close();
               }
               catch(SQLException ex)
               {
                   ex.printStackTrace();
               }
           }
       }
       return "hello";
    }
     //log out, kill the session and return to the main page
    public String logout()
        {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";        
    }
}
