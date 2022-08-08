package data;

import java.util.Scanner;
import java.io.File;

public class Testerama
{
    public static void main (String args[])
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("- - - - - UserBank Testerama - - - - -\n");
        UserBank testerama = new UserBank(100);

        //File userData = new File("UserData.bin");
        //testerama.readFile(userData,false);

        char ch;
        /*  Perform HashTable operations  */
        do    
        {
            System.out.println("\nMenu\n");
            System.out.println("1. Get High Score");
            System.out.println("2. Insert/Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Get # of Users");
            System.out.println("5. Print User Data\n");
            System.out.println("Please enter selection:");

            int choice = scan.nextInt( );            
            switch (choice)
            {
                case 1 : 
                System.out.println("\nEnter username: ");
                System.out.println("High Score: "+ testerama.getHighScore(scan.next( ))); 
                break; 
                case 2 : 
                System.out.println("\nEnter username and score: ");
                testerama.add(scan.next( ), scan.nextInt( )); 
                break;                          
                case 3 :                 
                System.out.println("\nEnter username: ");
                testerama.delete(scan.next( )); 
                break;                                                          
                case 4 : 
                System.out.println("\nSize: "+ testerama.getItems( ));
                break;
                case 5 :
                testerama.printTable( );
                default : 
                break;   
            }

            System.out.println("\nWould you like to continue? (Type Y or N)");
            ch = scan.next( ).charAt(0);                        
        } while (ch == 'Y'|| ch == 'y');
    }
}