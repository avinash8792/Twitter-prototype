/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PRAVALIKA
 */




/**
 *
 * @author sabep7342
 */
@Named(value = "twitterHomeBean")
@ManagedBean
@SessionScoped
public class twitterHomeBean implements Serializable {

  
    private String userId;
    private String fullName;
    private String eMail;
    private String phoneNum;
    private String password;
    private String profileImageLink;
    private String securityQues1;
    private String securityAns1;
    private String securityQues2;
    private String securityAns2;
    private String location;
    private DateAndTime createDateTime;
    private String correctPassword;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public String getSecurityQues1() {
        return securityQues1;
    }

    public void setSecurityQues1(String securityQues1) {
        this.securityQues1 = securityQues1;
    }

    public String getSecurityAns1() {
        return securityAns1;
    }

    public void setSecurityAns1(String securityAns1) {
        this.securityAns1 = securityAns1;
    }

    public String getSecurityQues2() {
        return securityQues2;
    }

    public void setSecurityQues2(String securityQues2) {
        this.securityQues2 = securityQues2;
    }

    public String getSecurityAns2() {
        return securityAns2;
    }

    public void setSecurityAns2(String securityAns2) {
        this.securityAns2 = securityAns2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DateAndTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(DateAndTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCorrectPassword() {
        return correctPassword;
    }

    public void setCorrectPassword(String correctPassword) {
        this.correctPassword = correctPassword;
    }
    
    
    public String signUp()
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        
        try
        {
            final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";
            
            //connect to the database with user name and password
            conn = DriverManager.getConnection(DB_URL, 
                   "mehtas1700", "1368776");   
            stat = conn.createStatement();
            //to search an onlineaccount based on id or ssn
            rs = stat.executeQuery("Select * from twitterprofiledetails "
                    + "where userId = '" + 
                    userId + "' or phone = '" + phoneNum + "'" );
            
            if(rs.next())
            {
                 return("Either you have an Peeper account already"+
                "or your ID/Phone number is not available to register");
            }
            else
            {
                String date = DateAndTime.DateTime(); 
                int r=stat.executeUpdate("insert into twitterprofiledetails values ( null,'"+userId+"', '"+fullName+"', '"+eMail+"', "
                        + "'"+phoneNum+"', '"+password+"', '"+profileImageLink+"', '"+
                        securityQues1+"', '"+securityAns1+"', '"+securityQues2+"', '"+securityAns2+"', '"+location+"','"+date+"')");
                
                return ("Registration Successful! "
                         
                        + "Welcome to Twitter!!! :" );
                
            }   
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return("internalError");
             
        }
        finally
        {
            try
            {
                rs.close();
                stat.close();
                conn.close();
                
            }
            catch (Exception e)
            {
                 
                e.printStackTrace();
                return("internalError");
            }
        }
    
        
    }
    
           
    // Login logic
    public String login()
    {
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
                    // check weather account is already locked or not if locked send user to password retrieval option only.
                    rs= st.executeQuery("select UserId from twitterprofiledetails where UserId='"+userId+"'");
                    if(rs.next())
                    {
                        rs= st.executeQuery("select password from twitterprofiledetails where UserId='"+userId+"'");
                        if(rs.next())
                        {
                            correctPassword=rs.getNString(1);
                            if(password.equals(correctPassword))
                            {
                                HttpSession hs = Util.getSession();
                                hs.setAttribute("user", userId);               
                                return("tweet");
                                
                            }
                            // password is incorrect first time
                            else
                            {
                                return("invalid");
                            }


                        }
                        // error message
                        else
                        {
                            return("signUpError");
                            
                        }
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
        return ("tweet");
    }
    
    
}
