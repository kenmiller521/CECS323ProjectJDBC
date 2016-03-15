/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg323jdbcproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Ken Miller, 013068183
 */
public class SQLDatabase 
{
    static String USER = "kenmiller";
    static String PASS = "013068183";
    static String DBNAME = "323JDBCProject";
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    static boolean DBExists;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData rsmd;
    private PreparedStatement pstmt;
    private String sql;
    private Scanner in = new Scanner(System.in);
    private String studioName;
    private String studioAddress;
    private String studioOwner;
    private String studioPhone;
    private String groupName;
    private String groupSinger;
    private int groupYear;
    private String groupGenre;
    private boolean studioExists;
    private boolean groupExists;
    private String buffer;
    private String[] tempArray;
    
    public SQLDatabase()
    {
        DBExists = false;
        USER = "kenmiller";
        PASS = "013068183";
        DBNAME = "323JDBCProject";
    }
    public void connect() throws ClassNotFoundException, SQLException
    {
        
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        System.out.println(DB_URL);
        conn = null; //initialize the connection
        stmt = null;  //initialize the statement that we're using
        try 
        {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
           // Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            //createTable();
            //dropTable();
        }
        catch (SQLException se) 
        {
            se.printStackTrace();
        } 
        catch (Exception e) 
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                if (stmt != null) 
                {
                    stmt.close();
                }
            } 
            catch (SQLException se2) 
            {
            }// nothing we can do
            try 
            {
                if (conn != null) 
                {
                    conn.close();
                }
            } 
            catch (SQLException se) 
            {
                se.printStackTrace();
            }//end finally try
        }//end try
    }
    public boolean dbExists() throws SQLException
    {
        try
        {
        conn = DriverManager.getConnection(DB_URL);
        DatabaseMetaData md = conn.getMetaData();
            rs = md.getTables(null, null, "%", null);
            while (rs.next()) 
            {
                //System.out.println(rs.getString(3));
                if("ALBUMS".equals(rs.getString(3)) || 
                        "RECORDINGSTUDIOS".equals(rs.getString(3)) || 
                        "RECORDINGGROUPS".equals(rs.getString(3)))
                    DBExists = true;
                else
                    DBExists = false;
                if(DBExists == true)
                    break;
            }
           
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
         return DBExists;
    }
    public void listAlbumTitles() throws SQLException
    {
        try
        {
           conn = DriverManager.getConnection(DB_URL);
           stmt = conn.createStatement(); 
           sql = "SELECT * FROM albums";
           pstmt = conn.prepareStatement(sql);
           rs = pstmt.executeQuery();
           while(rs.next())
           {
               System.out.println(rs.getString(1));
           }
           
           stmt.close();
           conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    public void listAllAlbumData() throws SQLException
    {
        try{
        System.out.println("Type album name for more information: " );
        String albumName = in.nextLine();
        conn = DriverManager.getConnection(DB_URL);
        stmt = conn.createStatement(); 
        sql = "SELECT * from albums natural join recordingstudios natural join recordinggroups where albumtitle = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, albumName);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        System.out.println(rsmd.getColumnName(1)+ "\t\t" + 
                rsmd.getColumnName(2)+ "\t\t" + 
                rsmd.getColumnName(3)+ "\t\t" + 
                rsmd.getColumnName(4)+ "\t\t" + 
                rsmd.getColumnName(5)+ "\t\t" + 
                rsmd.getColumnName(6)+ "\t\t" + 
                rsmd.getColumnName(7)+ "\t\t" + 
                rsmd.getColumnName(8)+ "\t\t" + 
                rsmd.getColumnName(9)+ "\t\t" + 
                rsmd.getColumnName(10)+ "\t\t" + 
                rsmd.getColumnName(11)+ "\t\t" + 
                rsmd.getColumnName(12)+ "\t\t" 
                );

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        while(rs.next())
        {       
            System.out.println(rs.getString(1)+ "\t\t" +
                    rs.getString(2)+ "\t\t" +
                    rs.getString(3)+ "\t\t" +
                    rs.getString(4)+ "\t\t" +
                    rs.getString(5)+ "\t\t" +
                    rs.getString(6)+ "\t\t" +
                    rs.getString(7)+ "\t\t" +
                    rs.getString(8)+ "\t\t" +
                    rs.getString(9)+ "\t\t" +
                    rs.getString(10)+ "\t\t" +
                    rs.getString(11)+ "\t\t" +
                    rs.getString(12)+ "\t\t" );
        }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void insertAlbum() throws SQLException
    {
        studioExists = false;
        groupExists = false;
        System.out.println("Insert the following information for a new album...");
        System.out.println("Album: " );
        String album = in.nextLine();
        System.out.println("Group: " );
        groupName = in.nextLine();
        System.out.println("Studio: " );
        studioName = in.nextLine();
        System.out.println("Date Recorded... " );
        System.out.println("Year YYYY: " );
        int year = in.nextInt();
        if(year > 1900)
            year-=1900;
        System.out.println("Month MM: ");
        int month = in.nextInt();
        System.out.println("Day DD: " );
        int day = in.nextInt();
        System.out.println("Length: " );
        int length = in.nextInt();
        System.out.println("Number of songs: " );
        int numbSongs = in.nextInt();
        buffer = in.nextLine();
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            //check to see if the studio already exists
            sql = "SELECT studioName FROM recordingstudios";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            while(rs.next())
            {   
                //System.out.println("StudioName: " + studioName);
                //System.out.println("RS: " + rs.getString(1));
                String temp = rs.getString(1);
                if(studioName.equals(temp))
                {
                    studioExists = true;
                    break;
                }
                else
                    studioExists = false;                
            }
            sql = "SELECT groupName FROM recordinggroups";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            while(rs.next())
            {   
                //System.out.println("groupName: " + groupName);
                //System.out.println("RS: " + rs.getString(1));
                String temp = rs.getString(1);
                if(groupName.equals(temp))
                {
                    groupExists = true;
                    break;
                }
                else
                    groupExists = false;
            }
            String[] groupNames = gatherGroupNames();
            String[] studioNames = gatherStudioNames();
            int temp;
            if(groupExists == false && studioExists == false)
            {
                System.out.println("Inputed group and studio do not exist.");
                System.out.println("Please select action:" );
                System.out.println("1) Choose pre-existing group and studio names");
                System.out.println("2) Create new group and studio");
                int choice = in.nextInt();
                buffer = in.nextLine();
                
                
                switch(choice){
                    case 1:
                        {                            
                            System.out.println("Choose from these group names: ");
                            for(int i = 0; i < groupNames.length; i++)
                                System.out.println((i+1)+") " + groupNames[i]);
                            temp = in.nextInt();
                            buffer = in.nextLine();
                            groupName = groupNames[temp-1];
                            System.out.println("Choose from these studio names: ");
                            for(int i = 0; i < studioNames.length; i++)
                                System.out.println((i+1)+") " + studioNames[i]);
                            temp = in.nextInt();
                            buffer = in.nextLine();
                            studioName = studioNames[temp-1];                            
                        }                        
                        break;
                    case 2:
                        {
                            insertStudio();
                            insertGroup();
                        }
                        break;
                }
                    
            }
            else if(groupExists == false)
            {
                System.out.println("Inputed group does not exist.");
                System.out.println("Please select action:" );
                System.out.println("1) Choose pre-existing group names");
                System.out.println("2) Create new group");
                int choice = in.nextInt();
                buffer = in.nextLine();
                switch(choice){
                    case 1:
                        {
                            System.out.println("Choose from these group names: ");
                            for(int i = 0; i < groupNames.length; i++)
                                System.out.println((i+1)+") " + groupNames[i]);
                            temp = in.nextInt();
                            buffer = in.nextLine();
                            groupName = groupNames[temp-1];
                        }                        
                        break;
                    case 2:
                        {
                            insertGroup();
                        }
                        break;
                }
            }
            else if(studioExists == false)
            {
                System.out.println("Inputed studio does not exist.");
                System.out.println("Please select action:" );
                System.out.println("1) Choose pre-existing studio names");
                System.out.println("2) Create new studio");
                int choice = in.nextInt();
                buffer = in.nextLine();
                switch(choice){
                    case 1:
                        {
                            System.out.println("Choose from these studio names: ");
                            for(int i = 0; i < studioNames.length; i++)
                                System.out.println((i+1)+") " + studioNames[i]);
                            temp = in.nextInt();
                            buffer = in.nextLine();
                            studioName = studioNames[temp-1]; 
                        }                        
                        break;
                    case 2:
                        {
                           insertStudio();
                        }
                        break;
                }
            }
            //else they both exist
            else
            {
                
            }
            //create insert statement
            conn = DriverManager.getConnection(DB_URL);
            sql = "INSERT INTO albums VALUES(?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, album);
            pstmt.setString(2, groupName);
            pstmt.setInt(3, numbSongs);
            pstmt.setString(4, studioName);
            pstmt.setDate(5, new Date(year,month,day));
            pstmt.setInt(6, length);
            pstmt.executeUpdate();
            groupExists = false;
            studioExists = false;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    public String[] gatherStudioNames() throws SQLException
    {
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            //check to see if the studio already exists
            sql = "SELECT COUNT(*) AS COUNT FROM recordingstudios";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int numbRows = 0;
            while(rs.next()) 
            {
                   numbRows = rs.getInt("COUNT");
            }
            tempArray = new String[numbRows];
            
            sql = "SELECT studioname FROM recordingstudios";

            //sql = "SELECT DISTINCT studioname FROM recordingstudios NATURAL JOIN albums";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int i = 0;
            while(rs.next())
            {
                tempArray[i]= rs.getString(1);
                
                i++;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return tempArray;
    }
    public String[] gatherStudioNamesToReplace() throws SQLException
    {
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            //check to see if the studio already exists
            sql = "SELECT COUNT(*) AS COUNT FROM recordingstudios";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int numbRows = 0;
            while(rs.next()) 
            {
                   numbRows = rs.getInt("COUNT");
            }
            tempArray = new String[numbRows];
            /*
            sql = "SELECT studioname FROM recordingstudios";
*/
            sql = "SELECT DISTINCT studioname FROM recordingstudios NATURAL JOIN albums";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int i = 0;
            while(rs.next())
            {
                tempArray[i]= rs.getString(1);
                
                i++;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return tempArray;
    }
    public String[] gatherGroupNames() throws SQLException
    {        
        try{
        conn = DriverManager.getConnection(DB_URL);
        stmt = conn.createStatement(); 
        //check to see if the studio already exists
        sql = "SELECT COUNT(*) AS COUNT FROM recordinggroups";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        int numbRows = 0;
        while(rs.next()) 
        {
               numbRows = rs.getInt("COUNT");
        }
        tempArray = new String[numbRows];
        sql = "SELECT groupname FROM recordinggroups";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        int i = 0;
        while(rs.next())
        {
            tempArray[i]= rs.getString(1);
            i++;
        }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return tempArray;
    }
    public void insertStudio() throws SQLException
    {
        System.out.println("Insert the following information...");
        System.out.println("Studio name: " );
        studioName = in.nextLine();
            
        System.out.println("Address: " );
        studioAddress = in.nextLine();
        System.out.println("Owner: " );
        studioOwner = in.nextLine();
        System.out.println("Phone: " );
        studioPhone = in.nextLine();
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            sql = "INSERT INTO recordingstudios VALUES(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studioName);
            pstmt.setString(2, studioAddress);
            pstmt.setString(3, studioOwner);
            pstmt.setString(4, studioPhone);
            pstmt.executeUpdate();
            stmt.close();
            conn.close();
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    public void insertStudio(String sName) throws SQLException
    {
        
        studioName = sName;            
        System.out.println("Address: " );
        studioAddress = in.nextLine();
        System.out.println("Owner: " );
        studioOwner = in.nextLine();
        System.out.println("Phone: " );
        studioPhone = in.nextLine();
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            sql = "INSERT INTO recordingstudios VALUES(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studioName);
            pstmt.setString(2, studioAddress);
            pstmt.setString(3, studioOwner);
            pstmt.setString(4, studioPhone);
            pstmt.executeUpdate();
            stmt.close();
            conn.close();
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    public void insertGroup() throws SQLException
    {
        System.out.println("Insert the following information...");
        System.out.println("Group name: " );
        groupName = in.nextLine();
        System.out.println("Lead Singer: " );
        groupSinger = in.nextLine();
        System.out.println("Genre: " );
        groupGenre = in.nextLine();
        System.out.println("Year Formed: " );
        groupYear = in.nextInt();
        buffer = in.nextLine();
        
        try
        {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            sql = "INSERT INTO recordinggroups VALUES(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, groupName);
            pstmt.setString(2, groupSinger);
            pstmt.setInt(3, groupYear);
            pstmt.setString(4, groupGenre);
            pstmt.executeUpdate();
            stmt.close();
            conn.close();            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void replaceStudio(String nameToReplace, String newStudioName) throws SQLException
    {
        try{
        conn = DriverManager.getConnection(DB_URL);
        stmt = conn.createStatement(); 
        sql = "UPDATE albums SET studioName = ? WHERE studioName =?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newStudioName);
        pstmt.setString(2, nameToReplace);
        pstmt.executeUpdate();
        //Print out albums with new studios
        sql = "SELECT * FROM albums WHERE studioname = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newStudioName);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        System.out.println(rsmd.getColumnName(1)+ "\t" + 
                rsmd.getColumnName(2)+ "\t" + 
                rsmd.getColumnName(3)+ "\t" + 
                rsmd.getColumnName(4)+ "\t" + 
                rsmd.getColumnName(5)+ "\t" + 
                rsmd.getColumnName(6)
                );
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        
        while(rs.next())
        {
            System.out.println(rs.getString(1)+ "\t" +
                    rs.getString(2)+ "\t" +
                    rs.getInt(3)+ "\t" +
                    rs.getString(4)+ "\t" +
                    rs.getDate(5)+ "\t" +
                    rs.getInt(6));
        }
        stmt.close();
        conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void deleteAlbum() throws SQLException
    {
        try
        {
            System.out.println("----------------------------");
            listAlbumTitles();
            System.out.println("----------------------------");
            System.out.println("Type album to delete from above: ");
            String aName = in.nextLine();
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(); 
            sql = "DELETE FROM albums WHERE albumTitle = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, aName);
            pstmt.executeUpdate();
            System.out.println("Album Deleted - Printing all albums");
            System.out.println("---------------------------------------");
            listAlbumTitles();     
            stmt.close();
            conn.close();            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }        
    }    
}