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
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Records created successfully");
        return true;
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
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + PLAYLISTS_TABLE_NAME + ";");
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

    public boolean updatePlaylist(Integer id, String name, String url, String file, Integer type) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return true;
    }

    public Integer deletePlaylist(Integer id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return 1;
    }

    public List<Playlist> getPlaylists() {
        List<Playlist> list = new ArrayList<Playlist>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return list;
    }

    public Playlist getPlaylistById(Integer id) {
        Playlist playlist = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return playlist;
    }

    public List<Integer> getPlaylistId() {
        List<Integer> array_list = new ArrayList<Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return array_list;
    }

    public List<String> getPlaylistNames() {
        List<String> array_list = new ArrayList<String>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
        return array_list;
    }

    public void deleteAllPlaylists() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
        System.out.println("Operation done successfully");
    }

/*    public boolean setPlaylistMd5ById(Integer id, String md5) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("md5", md5);
        db.update("playlists", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean setPlaylistUpdatedById(Integer id, long updated) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("updated", updated);
        db.update("playlists", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public String getPlaylistMd5ById(Integer id) {
        String result;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from playlists where id = " + id, null);
        res.moveToFirst();
        result = res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_MD5));
        return result;
    }

    public long getUpdatedDateById(Integer id) {
        long result;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from playlists where id = " + id, null);
        res.moveToFirst();
        result = res.getLong(res.getColumnIndex(PLAYLISTS_COLUMN_UPDATED));
        return result;
    }

    public List<Playlist> getSortedByDatePlaylists() {
        List<Playlist> list = new ArrayList<Playlist>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from playlists order by updated desc", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            list.add(new Playlist(res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_URL)),
                    res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_FILE)),
                    res.getInt(res.getColumnIndex(PLAYLISTS_COLUMN_TYPE)),
                    res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_MD5)),
                    res.getString(res.getColumnIndex(PLAYLISTS_COLUMN_UPDATED))));
            res.moveToNext();
        }
        return list;
    }*/
}
