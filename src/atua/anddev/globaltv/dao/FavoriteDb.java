package atua.anddev.globaltv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

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
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:globaltv.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO " + FAVORITES_TABLE_NAME + " (NAME,PLIST) " +
                    "VALUES ('" + name + "', '" + plist + " );";
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
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + FAVORITES_TABLE_NAME + ";");
            numRows = rs.getRow();
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

/*    public Integer deleteFromFavoritesById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("favorites",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public List<Favorites> getAllFavorites() {
        List<Favorites> array_list = new ArrayList<Favorites>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(new Favorites(res.getString(res.getColumnIndex(FAVORITES_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(FAVORITES_COLUMN_PLIST))));
            res.moveToNext();
        }
        return array_list;
    }

    public List<Favorites> getFavoritesByPlist(String plist) {
        List<Favorites> array_list = new ArrayList<Favorites>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(new Favorites(res.getString(res.getColumnIndex(FAVORITES_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(FAVORITES_COLUMN_PLIST))));
            res.moveToNext();
        }
        return array_list;
    }

    public List<String> getAllFavoritesName() {
        List<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(FAVORITES_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<String> getAllFavoritesProv() {
        List<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(FAVORITES_COLUMN_PLIST)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<Integer> getAllFavoritesID() {
        List<Integer> array_list = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getInt(res.getColumnIndex(FAVORITES_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<String> getFavoritesNameByPlist(String plist) {
        List<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(FAVORITES_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<Integer> getFavoritesIdByPlist(String plist) {
        List<Integer> array_list = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getInt(res.getColumnIndex(FAVORITES_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    public Favorites getFavoriteById(int id) {
        Favorites favorites;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorites where id = " + id, null);
        res.moveToFirst();
        favorites = new Favorites(res.getString(res.getColumnIndex(FAVORITES_COLUMN_NAME)),
                res.getString(res.getColumnIndex(FAVORITES_COLUMN_PLIST)));
        return favorites;
    }

    public void deleteAllFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from favorites");
    }*/


}
