package atua.anddev.globaltv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBHelper {
    protected String dbDriver = "org.sqlite.JDBC";
    protected String dbConnection = "jdbc:sqlite:globaltv.db";

    public void createDb() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql1 = "CREATE TABLE IF NOT EXISTS channels (" +
                    " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " NAME           TEXT, " +
                    " URL            TEXT, " +
                    " CATEGORY       TEXT, " +
                    " PLIST          TEXT)";
            String sql2 = "CREATE TABLE IF NOT EXISTS favorites (" +
                    " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " NAME           TEXT, " +
                    " PLIST          TEXT)";
            String sql3 = "CREATE TABLE IF NOT EXISTS playlists (" +
                    " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " NAME           TEXT, " +
                    " URL            TEXT, " +
                    " FILE           TEXT, " +
                    " TYPE           INTEGER, " +
                    " MD5            TEXT, " +
                    " UPDATED        TEXT)";
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public void dropDb() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql1 = "DROP TABLE IF EXISTS channels";
            String sql2 = "DROP TABLE IF EXISTS favorites";
            String sql3 = "DROP TABLE IF EXISTS playlists";
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table droped successfully");
    }

}
