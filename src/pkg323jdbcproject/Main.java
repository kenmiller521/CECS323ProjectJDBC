/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg323jdbcproject;

import java.sql.SQLException;

/**
 *
 * @author Zil
 */
public class Main {

     //public static SQLDatabase db;
    //public static GUI gui; 
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        SQLDatabase db = new SQLDatabase();
        db.connect();
        if(!db.dbExists())
        {
            db.resetDatabase();
        }
        db.listAlbumTitles();
        db.insertAlbum();
       
        
        
        db.listAllAlbumData();
    }
    
}
