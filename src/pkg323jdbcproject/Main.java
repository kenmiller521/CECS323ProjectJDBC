/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg323jdbcproject;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Ken Miller 013068183
 */
public class Main {
    
     //public static SQLDatabase db;
    //public static GUI gui; 
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        Scanner in = new Scanner(System.in);
        SQLDatabase db = new SQLDatabase();
        db.connect();
        
        if(!db.dbExists())
        {
            System.out.println("ONE OR MORE TABLES DOES NOT EXIST");
            System.out.println("MAKE TABLES USING THE .sql FILE");
        }
        else
        {     
            System.out.println("*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_");
            System.out.println("All available albums: ");
            System.out.println("--------------------------------------------");
            db.listAlbumTitles();
            System.out.println("*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_");        
            db.listAllAlbumData();
            System.out.println("*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_"); 
            db.insertAlbum();
            System.out.println("*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_"); 
            System.out.println("Enter a studio to replace another: " );
            System.out.println("Insert the following information...");
            System.out.println("Studio name: " );
            String tempstr = in.nextLine();
            db.insertStudio(tempstr);
            System.out.println("Choose a studio to replace: ");
            String[] temp = db.gatherStudioNamesToReplace();
            for(int i = 0; i < temp.length;i++)
            {
                if(temp[i] != null)
                    System.out.println(i+1 +") " + temp[i]);
            }
            int choice = in.nextInt();
            String sName = temp[choice-1];
            db.replaceStudio(sName,tempstr);
            System.out.println("*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_");
            db.deleteAlbum();
        }
    }    
}
