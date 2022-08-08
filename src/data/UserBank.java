package data;

import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserBank
{
    // UserBank class for type User, utilizes hash table structure
    // Implements Leaderboard class
    // Variables for number of items in hash table, size of hash table, array of type User, and Leaderboard object
    private int items;
    private int size;
    private User[] table;
    private Leaderboard board;

    // UserData.bin containing user data to read from/write to file
    File userData = new File("UserData.bin");

    // TempBoard.bin to temporarily write and read back in data
    File tempBoard = new File("TempBoard.bin");

    // Initialize FileWriter and BufferedWriter
    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;

    // Constructor
    public UserBank (int size)
    {
        // Set number of items in table to 0, set table size
        this.items = 0;
        this.size = size;

        // Initialize table of user entries, set entries to null
        table = new User[size];
        for (int i = 0; i < size; i++)
            table[i] = null;

        // Initialize leaderboard
        board = new Leaderboard( );
        
        File userData = new File("UserData.bin");
        readFile(userData,false);
    }

    // Return user's high score as integer, takes username
    // Average and amortized case, O(1)
    // Worst case, O(n)
    public int getHighScore (String username)
    {
        // Get hash index code for provided username
        int index = getIndex(username);

        // Check if item at provided index is null, return -1 if null
        if (table[index] == null)
            return -1;
        else
        {
            // Walk through linked list, return high score if match is found, or return -1
            User entry = table[index];

            while (entry != null && !entry.username.equals(username))
                entry = entry.next;

            if (entry == null)
                return -1;
            else
                return entry.highScore;
        }
    }

    // Insert new user's score or update existing user's high score
    // Average and amortized case, O(log(n))
    // Worst case, O(mlog(n))
    public void add (String username, int score)
    {
        // Get hash index code for provided username
        int index = (getIndex(username) % size);

        // Insert user at given index if it is empty
        if (table[index] == null)
        {
            table[index] = new User(username, score);
            board.add(table[index]);
        }

        // Else, walk through linked list and insert
        else
        {
            // Create pointer user for insertion
            User entry = table[index];

            while (entry.next != null && !entry.username.equals(username))
                entry = entry.next;

            // If username already exists, compare scores and update highest score, or insert new entry
            if (entry.username.equals(username))
            {
                if (entry.highScore < score)
                    entry.highScore = score;
            }
            else
            {
                entry.next = new User(username, score);
                board.add(entry.next);
            }
        }

        // Write data to file UserData.bin
        try
        {
            fileWriter = new FileWriter(userData, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            if (userData.length() != 0)
                bufferedWriter.newLine( );

            bufferedWriter.append(username);
            bufferedWriter.newLine( );
            bufferedWriter.append(String.valueOf(score));
            bufferedWriter.flush( );
        }

        catch (IOException e)
        {
            e.printStackTrace( );
        }

        finally
        {
            try
            {
                if(fileWriter != null)
                    fileWriter.close( );
                if(bufferedWriter != null)
                    bufferedWriter.close( );
            }

            catch (IOException e)
            {
                e.printStackTrace( );
            }
        }

        // Update size of table
        items++;
    }

    // Insert new user's score or update existing user's high score
    // Used for insertion from data file
    // Average and amortized case, O(log(n))
    // Worst case, O(mlog(n))
    public void add (String username, int score, boolean fromFile)
    {
        // If fromFile, reading in from TempBoard.bin for temporary write/read, insert to leaderboard
        if (fromFile == true)
        {
            User newUser = new User(username, score);
            board.add(newUser);
        }
        // Else, reading in from UserData.bin for insertion, insert to hash table and leaderboard
        else
        {
            // Get hash index code for provided username
            int index = (getIndex(username) % size);

            // Insert user at given index if it is empty
            if (table[index] == null)
            {
                table[index] = new User(username, score);
                board.add(table[index]);
            }
            // Else, walk through linked list and insert
            else
            {
                // Create pointer user for insertion
                User entry = table[index];

                while (entry.next != null && !entry.username.equals(username))
                    entry = entry.next;

                // If username already exists, compare scores and update highest score, or insert new entry
                if (entry.username.equals(username))
                {
                    if (entry.highScore < score)
                        entry.highScore = score;
                }
                else
                {
                    entry.next = new User(username, score);
                    board.add(entry.next);
                }
            }

            // Update size of table
            items++;
        }
    }

    // Remove user data from table
    // Average and amortized case, O(log(n))
    // Worst case, O(mlog(n))
    public void delete (String username)
    {
        // Get hash index code for provided username
        int index = getIndex(username);

        // Check if any entries exist at given index
        if (table[index] != null)
        {
            // Create two variables refercing entry to be deleted and entry to link to rest of list
            User entry = table[index];
            User linkUser = null;

            // Walk through linked list until match is found and update user to be linked
            while (entry.next != null && !entry.username.equals(username))
            {
                linkUser = entry;
                entry = entry.next;
            }

            // Once match is found, check if other entries need to be linked when deleting
            if (entry.username.equals(username))
            {
                if (linkUser != null)
                    linkUser.next = entry.next;
                else
                    table[index] = entry.next;

                // Update size of table
                items--;
            }

            // Delete user from leaderboard
            board.delete(username);

            // Write user deletion command to file UserData.bin
            try
            {
                fileWriter = new FileWriter(userData,true);
                bufferedWriter = new BufferedWriter(fileWriter);

                if (userData.length() != 0)
                    bufferedWriter.newLine( );

                bufferedWriter.append(".del");
                bufferedWriter.newLine( );
                bufferedWriter.append(username);
                bufferedWriter.flush( );
            }

            catch (IOException e)
            {
                e.printStackTrace( );
            }

            finally
            {
                try
                {
                    if(fileWriter != null)
                        fileWriter.close( );
                    if(bufferedWriter != null)
                        bufferedWriter.close( );
                }

                catch (IOException e)
                {
                    e.printStackTrace( );
                }
            }
        }
    }

    // Return size of user table
    // O(1)
    public int getItems ( )
    {
        return items;
    }
    
    public User[] getBoard ( )
    {
    	User[] returnBoard = new User[10];
    	int stop = board.getItems( );

        // Print leaderboard
        for (int i = 0; i < stop && i < 10; i++)
        {
            User temp = board.pull( );
            returnBoard[i] = temp;
            
            // Temporary write to file TempBoard.bin for printing leaderboard
            try
            {
                fileWriter = new FileWriter(tempBoard,true);
                bufferedWriter = new BufferedWriter(fileWriter);

                if (i != 0)
                    bufferedWriter.newLine( );

                bufferedWriter.append(temp.username);
                bufferedWriter.newLine( );
                bufferedWriter.append(String.valueOf(temp.highScore));
                bufferedWriter.flush( );
            }

            catch (IOException e)
            {
                e.printStackTrace( );
            }

            finally
            {
                try
                {
                    if(fileWriter != null)
                        fileWriter.close( );
                    if(bufferedWriter != null)
                        bufferedWriter.close( );
                }

                catch (IOException e)
                {
                    e.printStackTrace( );
                }
            }
        }
        
     // Read data back into leaderboard from TempBoard.bin
        boolean fromTemp = true;
        readFile(tempBoard,fromTemp);

        // Clear TempBoard.bin file
        try
        {
            PrintWriter writer = new PrintWriter(tempBoard);
            writer.print("");
            writer.close( );
        }

        catch (IOException e)
        {
            e.printStackTrace( );
        }

        finally
        {
            try
            {
                if(fileWriter != null)
                    fileWriter.close( );
            }
            catch (IOException e)
            {
                e.printStackTrace( );
            }
        }
        
        return returnBoard;
    }

    // Print out existing user data to console
    // O(mlog(n)), where m = size of hash table
    public void printTable ( )
    {
        System.out.println("\nUserBank Contents");
        System.out.println("Items: " + items);

        // Print hash table
        if (items != 0)
        {
            System.out.println("\nUser Data:");
            for (int i = 0; i < size; i++)
            {
                // Run through table, print user data if there is an entry at index
                User entry = table[i];
                while (entry != null)
                {
                    if (i > 9)
                        System.out.println("( In Index [" + i + "] ) - " + entry.username + ": " + entry.highScore + " ");
                    else
                        System.out.println("( In Index [0" + i + "] ) - " + entry.username + ": " + entry.highScore + " ");
                    entry = entry.next;
                }
            }

            System.out.println("\nLeaderboard:");

            int stop = board.getItems( );

            // Print leaderboard
            for (int i = 0; i < stop && i < 10; i++)
            {
                User temp = board.pull( );
                if(i > 8)
                    System.out.println((i + 1) + ". " + temp.username + " " + temp.highScore);
                else
                    System.out.println("0" + (i + 1) + ". " + temp.username + " " + temp.highScore);

                // Temporary write to file TempBoard.bin for printing leaderboard
                try
                {
                    fileWriter = new FileWriter(tempBoard,true);
                    bufferedWriter = new BufferedWriter(fileWriter);

                    if (i != 0)
                        bufferedWriter.newLine( );

                    bufferedWriter.append(temp.username);
                    bufferedWriter.newLine( );
                    bufferedWriter.append(String.valueOf(temp.highScore));
                    bufferedWriter.flush( );
                }

                catch (IOException e)
                {
                    e.printStackTrace( );
                }

                finally
                {
                    try
                    {
                        if(fileWriter != null)
                            fileWriter.close( );
                        if(bufferedWriter != null)
                            bufferedWriter.close( );
                    }

                    catch (IOException e)
                    {
                        e.printStackTrace( );
                    }
                }
            }

            // Read data back into leaderboard from TempBoard.bin
            boolean fromTemp = true;
            readFile(tempBoard,fromTemp);

            // Clear TempBoard.bin file
            try
            {
                PrintWriter writer = new PrintWriter(tempBoard);
                writer.print("");
                writer.close( );
            }

            catch (IOException e)
            {
                e.printStackTrace( );
            }

            finally
            {
                try
                {
                    if(fileWriter != null)
                        fileWriter.close( );
                }
                catch (IOException e)
                {
                    e.printStackTrace( );
                }
            }
        }
    }

    // Hash function to produce index from given username
    // O(1)
    private int getIndex (String id)
    {
        int hash = 7;

        for (int i = 0; i < id.length( ); i++)
        {
            hash = (hash*31 + id.charAt(i))%size;
        }

        return(hash);
    }

    // Read data in from file
    // O(mlog(n))
    public void readFile (File file, boolean fromTemp)
    {
        try
        {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line1 = null;
            String line2 = null;

            while((line1=bufferedReader.readLine())!=null)
            {
                if(line1.equals(".del"))
                {
                    if((line2 = bufferedReader.readLine())!=null)
                        delete(line2);
                }
                else if((line2 = bufferedReader.readLine())!=null)
                {
                    int temp = Integer.valueOf(line2);
                    add(line1, temp, fromTemp);
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace( );
        }

        finally
        {
            try
            {
                if(fileReader != null)
                    fileReader.close( );
                if(bufferedReader != null)
                    bufferedReader.close( );
            }

            catch (IOException e)
            {
                e.printStackTrace( );
            }
        }
    }
}