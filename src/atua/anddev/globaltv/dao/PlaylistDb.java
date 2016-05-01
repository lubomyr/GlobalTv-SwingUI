package atua.anddev.globaltv.dao;

import atua.anddev.globaltv.entity.Playlist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaylistDb extends DBHelper {
    public static final String PLAYLISTS_TABLE_NAME = "playlists";
    public static final String PLAYLISTS_COLUMN_ID = "id";
    public static final String PLAYLISTS_COLUMN_NAME = "name";
    public static final String PLAYLISTS_COLUMN_URL = "url";
    public static final String PLAYLISTS_COLUMN_FILE = "file";
    public static final String PLAYLISTS_COLUMN_TYPE = "type";
    public static final String PLAYLISTS_COLUMN_MD5 = "md5";
    public static final String PLAYLISTS_COLUMN_UPDATED = "updated";
    private HashMap hp;

    public PlaylistDb() {
        super();
    }

    public boolean insertPlaylist(String name, String url, String file, Integer type) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "INSERT INTO " + PLAYLISTS_TABLE_NAME + " (NAME,URL,FILE,TYPE) " +
                    "VALUES ('" + name + "', '" + url + "', '" + file + "', " + type + " );";
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
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + PLAYLISTS_TABLE_NAME + ";");
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

    public boolean updatePlaylist(Integer id, String name, String url, String file, Integer type) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "UPDATE " + PLAYLISTS_TABLE_NAME + " set NAME='" + name + "', URL='" + url + "', FILE='"
                    + file + "', TYPE='" + type + " where ID=" + id + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public Integer deletePlaylist(Integer id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE from " + PLAYLISTS_TABLE_NAME + " where ID=" + id + ";";
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

    public List<Playlist> getPlaylists() {
        List<Playlist> list = new ArrayList<Playlist>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + PLAYLISTS_TABLE_NAME + ";");
            while (rs.next()) {
                int id = rs.getInt(PLAYLISTS_COLUMN_ID);
                String name = rs.getString(PLAYLISTS_COLUMN_NAME);
                String url = rs.getString(PLAYLISTS_COLUMN_URL);
                String file = rs.getString(PLAYLISTS_COLUMN_FILE);
                int type = rs.getInt(PLAYLISTS_COLUMN_TYPE);
                String md5 = rs.getString(PLAYLISTS_COLUMN_MD5);
                String updated = rs.getString(PLAYLISTS_COLUMN_UPDATED);
                list.add(new Playlist(name, url, file, type, md5, updated));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return list;
    }

    public Playlist getPlaylistById(Integer id) {
        Playlist playlist = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + PLAYLISTS_TABLE_NAME + " where ID=" + id + ";");
            String name = rs.getString(PLAYLISTS_COLUMN_NAME);
            String url = rs.getString(PLAYLISTS_COLUMN_URL);
            String file = rs.getString(PLAYLISTS_COLUMN_FILE);
            int type = rs.getInt(PLAYLISTS_COLUMN_TYPE);
            String md5 = rs.getString(PLAYLISTS_COLUMN_MD5);
            String updated = rs.getString(PLAYLISTS_COLUMN_UPDATED);
            playlist = new Playlist(name, url, file, type, md5, updated);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return playlist;
    }

    public List<Integer> getPlaylistId() {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + PLAYLISTS_COLUMN_ID + " FROM " + PLAYLISTS_TABLE_NAME + ";");
            while (rs.next()) {
                int id = rs.getInt(PLAYLISTS_COLUMN_ID);
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

    public List<String> getPlaylistNames() {
        List<String> array_list = new ArrayList<String>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + PLAYLISTS_COLUMN_NAME + " FROM " + PLAYLISTS_TABLE_NAME + ";");
            while (rs.next()) {
                String name = rs.getString(PLAYLISTS_COLUMN_NAME);
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

    public void deleteAllPlaylists() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE from " + PLAYLISTS_TABLE_NAME + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public boolean setPlaylistMd5ById(Integer id, String md5) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "UPDATE " + PLAYLISTS_TABLE_NAME + " set MD5='" + md5 + "' where ID=" + id + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public boolean setPlaylistUpdatedById(Integer id, long updated) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "UPDATE " + PLAYLISTS_TABLE_NAME + " set UPDATED='" + updated + "' where ID=" + id + ";";
            stmt.executeUpdate(sql);
            c.commit();

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public String getPlaylistMd5ById(Integer id) {
        String result = null;
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + PLAYLISTS_COLUMN_MD5 + " FROM " + PLAYLISTS_TABLE_NAME + ";");
            result = rs.getString(PLAYLISTS_COLUMN_MD5);

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public long getUpdatedDateById(Integer id) {
        long result = 0;
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + PLAYLISTS_COLUMN_UPDATED + " FROM " + PLAYLISTS_TABLE_NAME + ";");
            result = rs.getLong(PLAYLISTS_COLUMN_UPDATED);

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public List<Playlist> getSortedByDatePlaylists() {
        List<Playlist> list = new ArrayList<Playlist>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + PLAYLISTS_TABLE_NAME + " ORDER BY " + PLAYLISTS_COLUMN_UPDATED + " desc;");
            while (rs.next()) {
                int id = rs.getInt(PLAYLISTS_COLUMN_ID);
                String name = rs.getString(PLAYLISTS_COLUMN_NAME);
                String url = rs.getString(PLAYLISTS_COLUMN_URL);
                String file = rs.getString(PLAYLISTS_COLUMN_FILE);
                int type = rs.getInt(PLAYLISTS_COLUMN_TYPE);
                String md5 = rs.getString(PLAYLISTS_COLUMN_MD5);
                String updated = rs.getString(PLAYLISTS_COLUMN_UPDATED);
                list.add(new Playlist(name, url, file, type, md5, updated));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return list;
    }

    public void insertAllPlaylists(List<Playlist> playlists) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(dbDriver);
            c = DriverManager.getConnection(dbConnection);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            for (Playlist s : playlists) {
                String sql = "INSERT INTO " + PLAYLISTS_TABLE_NAME + " (NAME,URL,FILE,TYPE) " +
                        "VALUES ('" + s.getName() + "', '" + s.getUrl() + "', '" + s.getFile() + "', " + s.getType() + " );";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
