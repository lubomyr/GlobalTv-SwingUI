package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Services;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Playlist;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class PlaylistServiceImpl implements PlaylistService, Services {

    /*Comparator for sorting the list by Playlist date*/
    private static Comparator<Playlist> PlstDateComparator = new Comparator<Playlist>() {

        public int compare(Playlist s1, Playlist s2) {
            String PlaylistDate1 = s1.getUpdate().toUpperCase();
            String PlaylistDate2 = s2.getUpdate().toUpperCase();

            //ascending order
            //return PlaylistDate1.compareTo(PlaylistDate2);

            //descending order
            return PlaylistDate2.compareTo(PlaylistDate1);
        }
    };

    @Override
    public List<Playlist> getSortedByDatePlaylists() {
        List<Playlist> sortedList = new ArrayList<>();
        sortedList.addAll(activePlaylist);
        Collections.sort(sortedList, PlstDateComparator);
        return sortedList;
    }

    @Override
    public void addNewActivePlaylist(Playlist plst) {
        String name = plst.getName();
        File file = new File(myPath + "/" + plst.getFile());
        long fileDate = file.lastModified();
        plst.setUpdate(String.valueOf(fileDate));
        activePlaylist.add(plst);
    }

    @Override
    public void setMd5(int id, String md5) {
        Playlist plst = getActivePlaylistById(id);
        activePlaylist.set(id, plst).setMd5(md5);
    }

    @Override
    public void setUpdateDate(int id, Long update) {
        Playlist plst = getActivePlaylistById(id);
        String updateStr = update.toString();
        activePlaylist.set(id, plst).setUpdate(updateStr);
    }

    @Override
    public void deleteActivePlaylistById(int id) {
        activePlaylist.remove(id);
    }

    @Override
    public Playlist getActivePlaylistById(int id) {
        return activePlaylist.get(id);
    }

    @Override
    public Playlist getOfferedPlaylistById(int id) {
        return offeredPlaylist.get(id);
    }

    @Override
    public void setActivePlaylistById(int id, String name, String url, int type) {
        String file = getFileName(name);
        activePlaylist.set(id, new Playlist(name, url, file, type));
    }

    @Override
    public void clearActivePlaylist() {
        activePlaylist.clear();
    }

    @Override
    public void clearOfferedPlaylist() {
        offeredPlaylist.clear();
    }

    @Override
    public int sizeOfActivePlaylist() {
        return activePlaylist.size();
    }

    @Override
    public int sizeOfOfferedPlaylist() {
        return offeredPlaylist.size();
    }

    @Override
    public List<Playlist> getAllActivePlaylist() {
        return activePlaylist;
    }

    @Override
    public List<Playlist> getAllOfferedPlaylist() {
        return offeredPlaylist;
    }

    @Override
    public int indexNameForActivePlaylist(String name) {
        int result = -1;
        for (int i = 0; i < activePlaylist.size(); i++) {
            if (name.equals(activePlaylist.get(i).getName()))
                result = i;
        }
        return result;
    }

    private String getFileName(String input) {
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
        Playlist plst = new Playlist(name, url, file, type, md5, update);
        activePlaylist.add(plst);
    }

    @Override
    public void addToOfferedPlaylist(String name, String url, int type) {
        String file = getFileName(name);
        Playlist plst = new Playlist(name, url, file, type);
        offeredPlaylist.add(plst);
    }

    public void addAllOfferedPlaylist() {
        for (Playlist plst : offeredPlaylist) {
            if (indexNameForActivePlaylist(plst.getName()) != -1) {
                activePlaylist.add(plst);
            }
        }
        saveData();
    }

    @Override
    public void saveData() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

//            Element torkey = doc.createElement("torrentkey");
//            torkey.appendChild(doc.createTextNode(Global.torrentKey));
//            rootElement.appendChild(torkey);

            for (Playlist plst : activePlaylist) {
                // favorites elements
                Element provider = doc.createElement("provider");
                rootElement.appendChild(provider);

                // name elements
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(plst.getName()));
                provider.appendChild(name);

                // url elements
                Element url = doc.createElement("url");
                url.appendChild(doc.createTextNode(plst.getUrl()));
                provider.appendChild(url);

                // type elements
                Element type = doc.createElement("type");
                type.appendChild(doc.createTextNode(plst.getTypeString()));
                provider.appendChild(type);

                // md5 elements
                Element md5 = doc.createElement("md5");
                md5.appendChild(doc.createTextNode(plst.getMd5()));
                provider.appendChild(md5);

                // update elements
                Element update = doc.createElement("update");
                update.appendChild(doc.createTextNode(plst.getUpdate()));
                provider.appendChild(update);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("userdata.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            // System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }

    }

    @Override
    public void setupProvider(String opt) {
        String name = null, url = null, type = null, md5 = "", update = String.valueOf(new Date().getTime());
        String endTag, text = null;
        if (opt.equals("default")) {
            new Thread(new ParseTask()).start();
        } else {
            try {
                File fXmlFile;
                fXmlFile = new File(myPath + "userdata.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);

                doc.getDocumentElement().normalize();

//                Global.torrentKey = doc.getElementsByTagName("torrentkey").item(0).getTextContent();
                NodeList nList = doc.getElementsByTagName("provider");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        name = eElement.getElementsByTagName("name").item(0).getTextContent();
                        url = eElement.getElementsByTagName("url").item(0).getTextContent();
                        type = eElement.getElementsByTagName("type").item(0).getTextContent();
                        md5 = eElement.getElementsByTagName("md5").item(0).getTextContent();
                        update = eElement.getElementsByTagName("update").item(0).getTextContent();
                        addToActivePlaylist(name, url, Integer.parseInt(type), md5, update);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void readPlaylist(int num) {
        String fname = getActivePlaylistById(num).getFile();
        String provName = getActivePlaylistById(num).getName();
        int type = getActivePlaylistById(num).getType();
        String lineStr, chName = "", chCategory = "", chLink = "", chIcon = "";
        String groupName = "", groupName2 = "";
        channelService.clearAllChannel();
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
                    if ((chName.length() > 0) && (chName.charAt(chName.length() - 1) == '\15')) {
                        chName = chName.substring(0, chName.length() - 1);
                    }
                    chCategory = tService.translateCategory(chCategory);
                    Channel channel = new Channel(chName, chLink, chCategory, chIcon, provName);
                    channelService.addToChannelList(channel);
                    chName = "";
                    chCategory = "";
                    chLink = "";
                    chIcon = "";
                    groupName = "";
                    groupName2 = "";
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") == lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.indexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") != lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.lastIndexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                }
                if (lineStr.startsWith("#EXTINF:") && (type != 1)) {
                    chName = lineStr.substring(lineStr.lastIndexOf(",") + 1, lineStr.length());
                }
                if (lineStr.contains("group-title=") && lineStr.contains(",") && (lineStr.substring(lineStr.indexOf("group-title="), lineStr.indexOf("group-title=") + 12).equals("group-title="))) {
                    groupName = lineStr.substring(lineStr.indexOf("group-title=") + 13, lineStr.indexOf('"', lineStr.indexOf("group-title=") + 13));
                }
                if (lineStr.contains("#EXTGRP:") && (lineStr.substring(lineStr.indexOf("#EXTGRP:"), lineStr.indexOf("#EXTGRP:") + 8).equals("#EXTGRP:"))) {
                    groupName2 = lineStr.substring(lineStr.indexOf("#EXTGRP:") + 8, lineStr.length());
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
        } catch (Exception e) {
            System.out.println(e);
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

        String doInBackground() {
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

        void onPostExecute(String strJson) {
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
