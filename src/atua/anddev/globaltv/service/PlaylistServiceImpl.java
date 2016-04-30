package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Global;
import atua.anddev.globaltv.Services;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Playlist;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistServiceImpl implements PlaylistService, Services {
    public static String LOG_TAG = "GlobalTv";

    @Override
    public List<Playlist> getSortedByDatePlaylists() {
        return playlistDb.getSortedByDatePlaylists();
    }

    @Override
    public void addNewActivePlaylist(Playlist plst) {
        String name = plst.getName();
        activePlaylistName.add(name);
        playlistDb.insertPlaylist(plst.getName(), plst.getUrl(), plst.getFile(), plst.getType());
    }

    @Override
    public void setMd5(int id, String md5) {
        List<Integer> idList = playlistDb.getPlaylistId();
        int iddb = idList.get(id);
        playlistDb.setPlaylistMd5ById(iddb, md5);
    }

    @Override
    public void setUpdateDate(int id, Long update) {
        List<Integer> idList = playlistDb.getPlaylistId();
        int iddb = idList.get(id);
        playlistDb.setPlaylistUpdatedById(iddb, update);
    }

    @Override
    public void deleteActivePlaylistById(int id) {
        List<Integer> idList = playlistDb.getPlaylistId();
        int iddb = idList.get(id);
        activePlaylistName.remove(id);
        playlistDb.deletePlaylist(iddb);
    }

    @Override
    public Playlist getActivePlaylistById(int id) {
        List<Integer> idList = playlistDb.getPlaylistId();
        int iddb = idList.get(id);
        return playlistDb.getPlaylistById(iddb);
    }

    @Override
    public Playlist getOfferedPlaylistById(int id) {
        return offeredPlaylist.get(id);
    }

    @Override
    public void setActivePlaylistById(int id, String name, String url, int type) {
        String file = getFileName(name);
        List<Integer> idList = playlistDb.getPlaylistId();
        int iddb = idList.get(id);
        activePlaylistName.set(id, name);
        playlistDb.updatePlaylist(iddb, name, url, file, type);
    }

    @Override
    public void clearActivePlaylist() {
        activePlaylistName.clear();
        playlistDb.deleteAllPlaylists();
    }

    @Override
    public void clearOfferedPlaylist() {
        offeredPlaylist.clear();
    }

    @Override
    public int sizeOfActivePlaylist() {
        return playlistDb.numberOfRows();
    }

    @Override
    public int sizeOfOfferedPlaylist() {
        return offeredPlaylist.size();
    }

    @Override
    public List<Playlist> getAllActivePlaylist() {
        return playlistDb.getPlaylists();
    }

    @Override
    public List<Playlist> getAllOfferedPlaylist() {
        return offeredPlaylist;
    }

    @Override
    public List<String> getAllNamesOfActivePlaylist() {
        return playlistDb.getPlaylistNames();
    }

    @Override
    public List<String> getAllNamesOfOfferedPlaylist() {
        List<String> arr = new ArrayList<String>();
        for (Playlist plst : offeredPlaylist) {
            arr.add(plst.getName());
        }
        return arr;
    }

    @Override
    public int indexNameForActivePlaylist(String name) {
        return activePlaylistName.indexOf(name);
    }

    @Override
    public int indexNameForOfferedPlaylist(String name) {
        int result = -1;
        for (int i = 0; i < offeredPlaylist.size(); i++) {
            if (offeredPlaylist.get(i).getName().equals(name)) {
                result = i;
            }
        }
        return result;
    }


    public String getFileName(String input) {
        String output = "playlist_" + input + ".m3u";
        output = output.replace(" ", "_");
        output = output.replace("(", "_");
        output = output.replace(")", "_");
        output = output.replace("@", "_");
        return output;
    }

    @Override
    public void addToActivePlaylist(String name, String url, int type, String md5, String update) {
        String file = getFileName(name);
        activePlaylistName.add(name);
        playlistDb.insertPlaylist(name, url, file, type);
    }

    @Override
    public void addToOfferedPlaylist(String name, String url, int type) {
        String file = getFileName(name);
        Playlist plst = new Playlist(name, url, file, type);
        offeredPlaylist.add(plst);
    }

    public void addAllOfferedPlaylist() {
        playlistDb.insertAllPlaylists(getAllOfferedPlaylist());
    }

    @Override
    public void setupProvider() {
        new Thread(new ParseTask()).start();
    }

    @Override
    public void fillNamesOfPlaylist() {
        activePlaylistName.clear();
        activePlaylistName.addAll(getAllNamesOfActivePlaylist());
    }

    public void readPlaylist(int num) {
        List<Channel> channels = new ArrayList<Channel>();
        String fname = getActivePlaylistById(num).getFile();
        int type = getActivePlaylistById(num).getType();
        Global.playlistWithGroup = false;
        String lineStr, chName = "", chCategory = "", chLink = "";
        String groupName = "", groupName2 = "";
        try {
            InputStream myfile = new FileInputStream(myPath + fname);
            Scanner myInputFile = new Scanner(myfile, "UTF8").useDelimiter("[\n]");
            while (myInputFile.hasNext()) {
                lineStr = myInputFile.next();
                if (lineStr.startsWith("acestream:") || lineStr.startsWith("http:") || lineStr.startsWith("https:")
                        || lineStr.startsWith("rtmp:") || lineStr.startsWith("rtsp:") || lineStr.startsWith("mmsh:")
                        || lineStr.startsWith("mms:") || lineStr.startsWith("rtmpt:")) {
                    chLink = lineStr;
                    if (chName.startsWith("ALLFON.TV")) {
                        chName = chName.substring(10, chName.length());
                    }
                    if (chName.startsWith("ALLFON.ORG")) {
                        chName = chName.substring(11, chName.length());
                    }
                    if (chName.startsWith(" ")) {
                        chName = chName.substring(1, chName.length());
                    }
                    if (chName.charAt(chName.length() - 1) == '\15') {
                        chName = chName.substring(0, chName.length() - 1);
                    }
                    chCategory = tService.translateCategory(chCategory);
                    channels.add(new Channel(chName, chLink, chCategory));
                    if (!chCategory.equals(""))
                        Global.playlistWithGroup = true;
                    chName = "";
                    chCategory = "";
                    chLink = "";
                    groupName = "";
                    groupName2 = "";
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") == lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.indexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                    Global.playlistWithGroup = true;
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") != lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.lastIndexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                    Global.playlistWithGroup = true;
                }
                if (lineStr.startsWith("#EXTINF:") && (type != 1)) {
                    chName = lineStr.substring(lineStr.lastIndexOf(",") + 1, lineStr.length());
                }
                if (lineStr.contains("group-title=") && lineStr.contains(",") && (lineStr.substring(lineStr.indexOf("group-title="), lineStr.indexOf("group-title=") + 12).equals("group-title="))) {
                    groupName = lineStr.substring(lineStr.indexOf("group-title=") + 13, lineStr.indexOf('"', lineStr.indexOf("group-title=") + 13));
                    Global.playlistWithGroup = true;
                }
                if (lineStr.contains("#EXTGRP:") && (lineStr.substring(lineStr.indexOf("#EXTGRP:"), lineStr.indexOf("#EXTGRP:") + 8).equals("#EXTGRP:"))) {
                    groupName2 = lineStr.substring(lineStr.indexOf("#EXTGRP:") + 8, lineStr.length());
                    Global.playlistWithGroup = true;
                }
                if (!groupName.equals("")) {
                    chCategory = groupName;
                } else if (!groupName2.equals("")) {
                    groupName2 = groupName2.replace('\15', ' ');
                    groupName2 = groupName2.replace(" ", "");
                    chCategory = groupName2;
                }
            }
            myInputFile.close();
            String plist = getActivePlaylistById(num).getName();
            channelService.deleteChannelbyPlist(plist);
            channelService.insertAllChannels(channels, plist);
        } catch (Exception e) {
//            Log.i("GlobalTV", "Error: " + e.toString());
        }
    }

    private class ParseTask extends Thread {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        public void run() {
            String json = doInBackground();
            onPostExecute(json);
        }

        protected String doInBackground() {
            try {
                URL url = new URL("https://dl.dropboxusercontent.com/u/47797448/playlist/playlists.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {
            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray playlists = dataJsonObj.getJSONArray("playlist");

                for (int i = 0; i < playlists.length(); i++) {
                    JSONObject playlist = playlists.getJSONObject(i);
                    String name = playlist.getString("name");
                    String url = playlist.getString("url");
                    int type = playlist.getInt("type");
                    addToOfferedPlaylist(name, url, type);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
