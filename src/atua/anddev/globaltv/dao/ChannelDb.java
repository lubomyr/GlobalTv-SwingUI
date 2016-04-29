package atua.anddev.globaltv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

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

/*    public Integer deleteChannelbyPlist(String plist) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("channels",
                "plist = ? ",
                new String[]{plist});
    }

    public Channel getChannelById(int id) {
        Channel channel;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where id = " + id, null);
        res.moveToFirst();
        channel = new Channel(res.getString(res.getColumnIndex(CHANNELS_COLUMN_NAME)),
                res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL)),
                res.getString(res.getColumnIndex(CHANNELS_COLUMN_GROUP)),
                res.getString(res.getColumnIndex(CHANNELS_COLUMN_PLIST)));
        return channel;
    }

    public ArrayList<String> getChannelsByPlist(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getChannelsUrlByPlist(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getChannelsByCategory(String plist, String category) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "' and category = '" + category + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getChannelsUrlByCategory(String plist, String category) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "' and category = '" + category + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL)));
            res.moveToNext();
        }
        return array_list;
    }

    public String getChannelsUrlByPlistAndName(String plist, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "' and name ='" + name + "'", null);
        res.moveToFirst();
        String result = res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL));
        return result;
    }

    public ArrayList<String> searchChannelsByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where name like '%" + search + "%'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> searchChannelsProvByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where name like '%" + search + "%'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_PLIST)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> searchChannelsUrlByName(String search) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where name like '%" + search + "%'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> searchChannelsByPlistAndName(String plist, String search) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "' and name like '%" + search + "%'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> searchChannelsUrlByPlistAndName(String plist, String search) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels where plist = '" + plist + "' and name like '%" + search + "%'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_URL)));
            res.moveToNext();
        }
        return array_list;
    }

    public void deleteAllChannels() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from channels");
    }

    public ArrayList<String> getCategoriesList(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select distinct category from channels where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_GROUP)));
            res.moveToNext();
        }
        return array_list;
    }

    public int getCategoriesNumber(String plist) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select distinct category from channels where plist = '" + plist + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CHANNELS_COLUMN_GROUP)));
            res.moveToNext();
        }
        return array_list.size();
    }

    public List<Integer> getAllChannelId() {
        List<Integer> array_list = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from channels", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getInt(res.getColumnIndex(CHANNELS_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    public void insertAllChannels(List<Channel> channels, String plist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        db.beginTransaction();

        try {
            for (Channel s : channels) {
                cv.put("name", s.getName());
                cv.put("url", s.getUrl());
                cv.put("category", s.getCategory());
                cv.put("plist", plist);

                db.insertOrThrow("channels", null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }*/

}
