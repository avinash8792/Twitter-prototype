// Java class for Forgot  Password

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.*;
import java.util.Random;
import javax.faces.bean.ManagedBean;



@Named(value = "forgotPassword")
@ManagedBean
@SessionScoped

public class ForgotPassword implements Serializable {

    private HashSet<String> Questions1;
    private HashSet<String> Questions2;
    private String selectedQuestion;
    private String UserID;
    private String question;
    private String correctAnswer;
    private String answer;
    String password;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mehtas1700";
    
    // creating List of HashSet for Drop Down List
    public ForgotPassword() {
        
        Questions1 = new HashSet<>();
        Questions2 = new HashSet<>();
        
        // load the initial drivers
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // return to internalEror.xhtml
            //return("");
        }
         try
        {           
            
        //connect to the database with user name and password
            conn = DriverManager.getConnection(DATABASE_URL, "mehtas1700", "1368776");   
            st = conn.createStatement();
            
            rs = st.executeQuery("Select * from twittersecurityquestion1" );
            //add the bank accounts under this SSN
            while(rs.next())
            {
               
                Questions1.add(rs.getString("questiontext"));
          
                
            }
            rs.close();
            
            rs = st.executeQuery("Select * from twittersecurityquestion2" );
            
            while(rs.next())
            {
               
                Questions2.add(rs.getString("questiontext"));
                
            }
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
                st.close();
                conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }
         
    
         
         
    }
    // selecting radom question from two question while password retreival
    public  String fpCheck()
    {
          try
        {           
            
        //connect to the database with user name and password
            conn = DriverManager.getConnection(DATABASE_URL, "mehtas1700", "1368776");   
            st = conn.createStatement();
            
             // generate random number form 0 and 1
            Random r = new Random();
            
            int que=r.nextInt(2);
           
            rs=st.executeQuery("select * from twitterprofiledetails where UserID='"+UserID+"'");
            
            if(rs != null)
            {
            while(rs.next())
            {
                
                password = rs.getString("Password");
                // if 0 so first question
                if(que==0)
                {
                  question = rs.getString("SecurityQuestion1");
                  correctAnswer = rs.getString("SecurityAnswer1");
                  
                }
                // else second question if 1
                else if (que==1)
                {
                    question = rs.getString("SecurityQuestion2");
                    correctAnswer = rs.getString("SecurityAnswer2");
                }
                
                return("forgotPasswordQuestion");
               
                
               
            }  
            }
           
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
                st.close();
                conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }
        
        String t="Hello";
        return t;
    }
    
    // check for the answer matches and display credentials for that Login ID and remove Login ID from locked accounts
public String fpAnswer()
{
    if(answer.equals(correctAnswer))
                {
                   
                   
                    try{
                    st.executeUpdate("delete from lockedloginid where loginID='"+UserID+"'"); 
                     
                    }
                     catch (SQLException e)
        {           
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
                st.close();
                conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }
                    return("forgotPasswordAnswer");
                   
                   // break;
                }
                // or display error message
                else
                {
                    return("invalid");
                }
    
    
}

    public HashSet<String> getQuestions2() {
        return Questions2;
    }

    public void setQuestions2(HashSet<String> Questions2) {
        this.Questions2 = Questions2;
    }

// get and set methods for variables
    public String getLoginID() {
        return UserID;
    }

    public void setLoginID(String loginID) {
        this.UserID = loginID;
    }

    public HashSet<String> getQuestions1() {
        return Questions1;
    }

    public void setQuestions1(HashSet<String> Questions1) {
        this.Questions1 = Questions1;
    }

    public String getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(String selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }
        public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
