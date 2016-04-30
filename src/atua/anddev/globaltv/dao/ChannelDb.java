package atua.anddev.globaltv.dao;

import atua.anddev.globaltv.entity.Channel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChannelDb extends DBHelper {
    public static final String CHANNELS_TABLE_NAME = "channels";
    public static final String CHANNELS_COLUMN_ID = "id";
    public static final String CHANNELS_COLUMN_NAME = "name";
    public static final String CHANNELS_COLUMN_URL = "url";
    public static final String CHANNELS_COLUMN_GROUP = "category";
    public static final String CHANNELS_COLUMN_PLIST = "plist";

    private HashMap hp;

    public ChannelDb() {
        super();
    }

    public int numberOfRows() {
        Connection c = null;
        Statement stmt = null;
        int numRows = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + CHANNELS_TABLE_NAME + ";");
            numRows = rs.getInt(1);
            System.out.println("numRows = " + numRows);

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return numRows;
    }

    public Integer deleteChannelbyPlist(String plist) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "DELETE from " + CHANNELS_TABLE_NAME + " where PLIST='" + plist + "';";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return 1;
    }

    public Channel getChannelById(int id) {
        Channel channel = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + CHANNELS_TABLE_NAME + " where ID=" + id + ";");
            String name = rs.getString(CHANNELS_COLUMN_NAME);
            String url = rs.getString(CHANNELS_COLUMN_URL);
            String group = rs.getString(CHANNELS_COLUMN_GROUP);
            String plist = rs.getString(CHANNELS_COLUMN_PLIST);
            channel = new Channel(name, url, group, plist);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return channel;
    }

    public ArrayList<String> getChannelsByPlist(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_NAME + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                String name = rs.getString(CHANNELS_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> getChannelsUrlByPlist(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_URL + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                String url = rs.getString(CHANNELS_COLUMN_URL);
                array_list.add(url);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> getChannelsByCategory(String plist, String category) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_NAME + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='"
                    + plist + "' AND CATEGORY='" + category + "';");
            while (rs.next()) {
                String name = rs.getString(CHANNELS_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> getChannelsUrlByCategory(String plist, String category) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_URL + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='"
                    + plist + "' AND CATEGORY='" + category + "';");
            while (rs.next()) {
                String url = rs.getString(CHANNELS_COLUMN_URL);
                array_list.add(url);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public String getChannelsUrlByPlistAndName(String plist, String name) {
        String result = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_URL + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='"
                    + plist + "' AND NAME='" + name + "';");
            result = rs.getString(CHANNELS_COLUMN_URL);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return result;
    }

    public ArrayList<String> searchChannelsByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_NAME + " FROM " + CHANNELS_TABLE_NAME
                    + " WHERE NAME LIKE '%" + search + "%';");
            while (rs.next()) {
                String name = rs.getString(CHANNELS_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> searchChannelsProvByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_PLIST + " FROM " + CHANNELS_TABLE_NAME
                    + " WHERE NAME LIKE '%" + search + "%';");
            while (rs.next()) {
                String plist = rs.getString(CHANNELS_COLUMN_PLIST);
                array_list.add(plist);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> searchChannelsUrlByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_URL + " FROM " + CHANNELS_TABLE_NAME
                    + " WHERE NAME LIKE '%" + search + "%';");
            while (rs.next()) {
                String url = rs.getString(CHANNELS_COLUMN_URL);
                array_list.add(url);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> searchChannelsByPlistAndName(String plist, String search) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_NAME + " FROM " + CHANNELS_TABLE_NAME
                    + " WHERE PLIST='" + plist + "' AND NAME LIKE '%" + search + "%';");
            while (rs.next()) {
                String name = rs.getString(CHANNELS_COLUMN_NAME);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public ArrayList<String> searchChannelsUrlByPlistAndName(String plist, String search) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_URL + " FROM " + CHANNELS_TABLE_NAME
                    + " WHERE PLIST='" + plist + "' AND NAME LIKE '%" + search + "%';");
            while (rs.next()) {
                String url = rs.getString(CHANNELS_COLUMN_URL);
                array_list.add(url);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public void deleteAllChannels() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "DELETE from " + CHANNELS_TABLE_NAME + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    public ArrayList<String> getCategoriesList(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT " + CHANNELS_COLUMN_GROUP + " FROM " + CHANNELS_TABLE_NAME + " WHERE PLIST='" + plist + "';");
            while (rs.next()) {
                String name = rs.getString(CHANNELS_COLUMN_GROUP);
                array_list.add(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public List<Integer> getAllChannelId() {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + CHANNELS_COLUMN_ID + " FROM " + CHANNELS_TABLE_NAME + ";");
            while (rs.next()) {
                int id = rs.getInt(CHANNELS_COLUMN_ID);
                array_list.add(id);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return array_list;
    }

    public void insertAllChannels(List<Channel> channels, String plist) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            for (Channel s : channels) {
                String sql = "INSERT INTO " + CHANNELS_TABLE_NAME + " (NAME,URL,CATEGORY,PLIST) " +
                        "VALUES ('" + (s.getName().contains("\'") ? s.getName().replace("\'", "''") : s.getName()) + "', '" + s.getUrl() + "', '" + s.getCategory() + "', '" + plist + "' );";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

}
