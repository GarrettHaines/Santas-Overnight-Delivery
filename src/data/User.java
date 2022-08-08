package data;

public class User
{
    // User entry class containing user variables
    public String username;
    public int highScore;
    User next;
    
    // Constructor
    User(String username, int highScore)
    {
        this.username = username;
        this.highScore = highScore;
        this.next = null;
    }
}