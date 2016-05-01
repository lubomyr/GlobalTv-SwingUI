package atua.anddev.globaltv.dao;

import atua.anddev.globaltv.entity.Favorites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteDb extends DBHelper {
    public static final String FAVORITES_TABLE_NAME = "favorites";
    public static final String FAVORITES_COLUMN_ID = "id";
    public static final String FAVORITES_COLUMN_NAME = "name";
    public static final String FAVORITES_COLUMN_PLIST = "plist";
    private HashMap hp;

    public FavoriteDb() {
        super();
    }

    public boolean insertIntoFavorites(String name, String plist) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "INSERT INTO " + FAVORITES_TABLE_NAME + " (NAME,PLIST) " +
                    "VALUES ('" + name + "', '" + plist + "' );";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public int numberOfRows() {
        Connection c = null;
        Statement stmt = null;
        int numRows = 0;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + FAVORITES_TABLE_NAME + ";");
            numRows = rs.getInt(1);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return numRows;
    }

    public Integer deleteFromFavoritesById(Integer id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE from " + FAVORITES_TABLE_NAME + " where ID=" + id + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return 1;
    }

    public List<Favorites> getAllFavorites() {
        List<Favorites> array_list = new ArrayList<Favorites>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + FAVORITES_TABLE_NAME + ";");
            while (rs.next()) {
                int id = rs.getInt(FAVORITES_COLUMN_ID);
                String name = rs.getString(FAVORITES_COLUMN_NAME);
                String plist = rs.getString(FAVORITES_COLUMN_PLIST);
                array_list.add(new Favorites(name, plist));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<Favorites> getFavoritesByPlist(String plist) {
        List<Favorites> array_list = new ArrayList<Favorites>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + FAVORITES_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                int id = rs.getInt(FAVORITES_COLUMN_ID);
                String name = rs.getString(FAVORITES_COLUMN_NAME);
                array_list.add(new Favorites(name, plist));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<String> getAllFavoritesName() {
        List<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_NAME + " FROM " + FAVORITES_TABLE_NAME + ";");
            while (rs.next()) {
                String name = rs.getString(FAVORITES_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<String> getAllFavoritesProv() {
        List<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_PLIST + " FROM " + FAVORITES_TABLE_NAME + ";");
            while (rs.next()) {
                String plist = rs.getString(FAVORITES_COLUMN_PLIST);
                array_list.add(plist);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<Integer> getAllFavoritesID() {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_ID + " FROM " + FAVORITES_TABLE_NAME + ";");
            while (rs.next()) {
                int id = rs.getInt(FAVORITES_COLUMN_ID);
                array_list.add(id);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<String> getFavoritesNameByPlist(String plist) {
        List<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_NAME + " FROM " + FAVORITES_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                String name = rs.getString(FAVORITES_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public List<Integer> getFavoritesIdByPlist(String plist) {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_ID + " FROM " + FAVORITES_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                int id = rs.getInt(FAVORITES_COLUMN_ID);
                array_list.add(id);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

    public Favorites getFavoriteById(int id) {
        Favorites favorites = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + FAVORITES_TABLE_NAME + " where ID=" + id + ";");
            String name = rs.getString(FAVORITES_COLUMN_NAME);
            String plist = rs.getString(FAVORITES_COLUMN_PLIST);
            favorites = new Favorites(name, plist);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return favorites;
    }

    public void deleteAllFavorites() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE from " + FAVORITES_TABLE_NAME + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public List<Integer> getFavoritesIdByPlistAndName(String plist, String name) {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + FAVORITES_COLUMN_ID + " FROM " + FAVORITES_TABLE_NAME
                    + " WHERE PLIST='" + plist + "' AND NAME='" + name + "';");
            while (rs.next()) {
                int id = rs.getInt(FAVORITES_COLUMN_ID);
                array_list.add(id);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return array_list;
    }

}
