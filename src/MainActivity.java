import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class MainActivity {
    static ArrayList<String> globalSearchName = new ArrayList<String>();
    static ArrayList<String> globalSearchUrl = new ArrayList<String>();
    static ArrayList<String> globalSearchProv = new ArrayList<String>();
    static Channel channel = new Channel();
    static Playlist ActivePlaylist = new Playlist();
    static Playlist DisabledPlaylist = new Playlist();
    static ArrayList<String> categoryList = new ArrayList<String>();
    static String selectedCategory;
    static String selectedChannel;
    static String selectedLink;
    static ArrayList<String> favoriteList = new ArrayList<String>();
    static ArrayList<String> favoriteProvList = new ArrayList<String>();
    static int selectedProvider;
    static String selectedPlaylist;
    static String myPath;
    static Boolean needUpdate;
    static String lang;
    static String searchString;
    static MainActivity act;
    static ArrayList<String> origNames = new ArrayList<String>();
    static ArrayList<String> translatedNames = new ArrayList<String>();
    static String torrentKey;
    static Boolean playlistWithGroup;
    static ArrayList<String> localsList = new ArrayList<String>();
    static ArrayList<String> playlist = new ArrayList<String>();
    static ArrayList<String> playlistUrl = new ArrayList<String>();
    static String path_aceplayer, path_vlc, path_other;
    static Locals local;
    static MyGui gui = new MyGui();
    static boolean otherplayer;

    public static void main(String[] args) {
        MyGui.main(args);
        myPath = "";
        if (lang == null)
            lang = Locale.getDefault().getISO3Language();
        local = new Locals();
        if (ActivePlaylist.size() == 0) {
            if (checkFile("userdata.xml"))
                setupProvider("user");
            else
                setupProvider("default");
        }
        if (path_aceplayer == null)
            loadSettings();
        showLocals();
        downloadAllPlaylist();
        applyLocals();

        MainGui.ProviderListActionAdapter();
        MainGui.mainButtonsActionListener();
    }

    public static void setupProvider(String opt) {
        ActivePlaylist.clear();
        DisabledPlaylist.clear();
        String name = null, url = null, file = null, type = null, enable = null;

        try {
            File fXmlFile;
            if (opt.equals("user"))
                fXmlFile = new File(myPath + "userdata.xml");
            else
                fXmlFile = new File(myPath + "data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            torrentKey = doc.getElementsByTagName("torrentkey").item(0).getTextContent();
            NodeList nList = doc.getElementsByTagName("provider");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    url = eElement.getElementsByTagName("url").item(0).getTextContent();
                    file = getFileName(name);
                    type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    enable = eElement.getElementsByTagName("enable").item(0).getTextContent();
                    if (enable.equals("true")) {
                        ActivePlaylist.add(name, url, file, Integer.parseInt(type));
                    }
                    if (enable.equals("false")) {
                        DisabledPlaylist.add(name, url, file, Integer.parseInt(type));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ActivePlaylist.size(); i++) {
            MainGui.comboBox.addItem(ActivePlaylist.getName(i));
        }

    }

    public static void prepareStrings(String filename) {
        origNames.clear();
        translatedNames.clear();
        String item = null;
        try {

            File fXmlFile = new File(myPath + filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            NodeList clo = doc.getElementsByTagName("categories_list_orig");
            Node nNode = clo.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                for (int i = 0; i < eElement.getElementsByTagName("item").getLength(); i++) {
                    item = eElement.getElementsByTagName("item").item(i).getTextContent();
                    origNames.add(item);
                }
            }
            NodeList clt = doc.getElementsByTagName("categories_list_translated");
            nNode = clt.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                for (int i = 0; i < eElement.getElementsByTagName("item").getLength(); i++) {
                    item = eElement.getElementsByTagName("item").item(i).getTextContent();
                    translatedNames.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLocals() {
        localsList.add("English");
        localsList.add("Українська");
        localsList.add("Русский");

        for (int i = 0; i < localsList.size(); i++)
            MainGui.comboBox_1.addItem(localsList.get(i));

        String translationFile = null;

        if (lang.equals("eng")) {
            MainGui.comboBox_1.setSelectedItem(localsList.get(0));
            translationFile = "strings-en.xml";
        }
        if (lang.equals("ukr")) {
            MainGui.comboBox_1.setSelectedItem(localsList.get(1));
            translationFile = "strings-uk.xml";
        }
        if (lang.equals("rus")) {
            MainGui.comboBox_1.setSelectedItem(localsList.get(2));
            translationFile = "strings-ru.xml";
        }
        prepareStrings(translationFile);
        MainGui.LanguageListActionAdapter();
    }

    public static void downloadAllPlaylist() {
        for (int i = 0; i < ActivePlaylist.size(); i++) {
            if (!checkPlaylistFile(ActivePlaylist.getFile(i))) {
                //downloadPlaylist(i);
            }
        }
    }

    public static void downloadPlaylist(final int num) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String path = myPath + ActivePlaylist.getFile(num);
                    saveUrl(path, ActivePlaylist.getUrl(num));

                    needUpdate = false;
                    checkPlaylistFile(ActivePlaylist.getFile(selectedProvider));
                    MainGui.MainWarningLabel.setText(local.$playlistupdated);
                } catch (Exception e) {
                    MainGui.MainWarningLabel.setText(e.toString());
                    // Log.i("SDL", "Error: " + e.toString());
                }
            }
        }).start();
    }

    public static void saveUrl(final String filename, final String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URL url = new URL(urlString);
            URLConnection uc;
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            uc.connect();
            in = new BufferedInputStream(uc.getInputStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    @SuppressWarnings({"deprecation", "resource"})
    public static boolean checkPlaylistFile(String fname) {
        try {
            File file = new File(myPath + fname);
            long fileDate = file.lastModified();
            long currDate = (new Date()).getTime();
            long upDate = currDate - fileDate;
            String tmpText;
            switch (new Date(upDate).getDate()) {
                case 1:
                    tmpText = local.$updated + new Date(fileDate).toLocaleString();
                    needUpdate = false;
                    break;
                case 2:
                    tmpText = local.$updated + "1" + local.$dayago;
                    needUpdate = true;
                    break;
                case 3:
                case 4:
                case 5:
                    tmpText = local.$updated + (new Date(upDate).getDate() - 1) + local.$daysago;
                    needUpdate = true;
                    break;
                default:
                    tmpText = local.$updated + (new Date(upDate).getDate() - 1) + local.$5daysago;
                    needUpdate = true;
                    break;

            }

            MainGui.MainPlaylistInfoLabel.setText(tmpText);
            new FileInputStream(myPath + fname);
        } catch (FileNotFoundException e) {
            MainGui.MainPlaylistInfoLabel.setText(local.$playlistnotexist);
            needUpdate = true;
            return false;
        }
        return true;
    }

    @SuppressWarnings("resource")
    public static boolean checkFile(String fname) {
        try {
            new FileInputStream(myPath + fname);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void readPlaylist(String fname, int type) {
        playlistWithGroup = false;
        String lineStr, chName = "", chCategory = "", chLink = "", totalString = "";
        String groupName = "", groupName2 = "";
        channel.clear();
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
                    if (chName.startsWith(" ")) {
                        chName = chName.substring(1, chName.length());
                    }
                    chCategory = translate(chCategory);
                    channel.add(chName, chCategory, chLink);
                    if (!chCategory.equals(""))
                        playlistWithGroup = true;
                    chName = "";
                    chCategory = "";
                    chLink = "";
                    groupName = "";
                    groupName2 = "";
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") == lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.indexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                    playlistWithGroup = true;
                }
                if ((type == 1) && lineStr.startsWith("#EXTINF:-1,") && (lineStr.indexOf("(") != lineStr.lastIndexOf("("))) {
                    chName = lineStr.substring(11, lineStr.lastIndexOf("(") - 1);
                    chCategory = lineStr.substring(lineStr.lastIndexOf("(") + 1, lineStr.lastIndexOf(")"));
                    playlistWithGroup = true;
                }
                if (lineStr.startsWith("#EXTINF:") && (type != 1)) {
                    chName = lineStr.substring(lineStr.indexOf(",") + 1, lineStr.length());
                }
                if (lineStr.contains("group-title=") && lineStr.contains(",") && (lineStr.substring(lineStr.indexOf("group-title="), lineStr.indexOf("group-title=") + 12).equals("group-title="))) {
                    groupName = lineStr.substring(lineStr.indexOf("group-title=") + 13, lineStr.indexOf('"', lineStr.indexOf("group-title=") + 13));
                    playlistWithGroup = true;
                }
                if (lineStr.contains("#EXTGRP:") && (lineStr.substring(lineStr.indexOf("#EXTGRP:"), lineStr.indexOf("#EXTGRP:") + 8).equals("#EXTGRP:"))) {
                    groupName2 = lineStr.substring(lineStr.indexOf("#EXTGRP:") + 8, lineStr.length());
                    playlistWithGroup = true;
                }
                if (!groupName.equals("")) {
                    chCategory = groupName;
                } else if (!groupName2.equals("")) {
                    groupName2 = groupName2.replace(" ", "");
                    chCategory = groupName2;
                }
            }
            myInputFile.close();
        } catch (Exception E1) {
            MainGui.MainWarningLabel.setText("openning playlist - failed");
        }
    }

    static String fixKey(String str) {
        String output;
        output = str;
        String startStr = "http://content.torrent-tv.ru/";
        String endStr;
        if (str.startsWith(startStr) && str.contains("/cdn/")) {
            endStr = str.substring(str.indexOf("/cdn/"), str.length());
            output = startStr + torrentKey + endStr;
        }
        return output;
    }

    public static void saveFavorites() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            for (int j = 0; j < favoriteList.size(); j++) {
                // favorites elements
                Element favorites = doc.createElement("favorites");
                rootElement.appendChild(favorites);

                // channel elements
                Element channel = doc.createElement("channel");
                channel.appendChild(doc.createTextNode(favoriteList.get(j)));
                favorites.appendChild(channel);

                // playlist elements
                Element playlist = doc.createElement("playlist");
                playlist.appendChild(doc.createTextNode(favoriteProvList.get(j)));
                favorites.appendChild(playlist);
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("favorites.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            // System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    public static void loadFavorites() {
        favoriteList.clear();
        favoriteProvList.clear();

        String channel = null, playlist = null;

        try {
            File fXmlFile;
            fXmlFile = new File(myPath + "favorites.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("favorites");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    channel = eElement.getElementsByTagName("channel").item(0).getTextContent();
                    playlist = eElement.getElementsByTagName("playlist").item(0).getTextContent();

                    favoriteList.add(channel);
                    favoriteProvList.add(playlist);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadSettings() {
        try {
            File fXmlFile;
            fXmlFile = new File(myPath + "settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            path_aceplayer = doc.getElementsByTagName("aceplayer").item(0).getTextContent();
            path_vlc = doc.getElementsByTagName("vlcplayer").item(0).getTextContent();
            path_other = doc.getElementsByTagName("otherplayer").item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void applyLocals() {
        MainGui.MainUpdateButton.setText(local.$updatePlaylistButton);
        MainGui.MainOpenButton.setText(local.$openPlaylistButton);
        MainGui.MainPlaylistLabel.setText(local.$playlist);
        MainGui.MainUpdateAllButton.setText(local.$updateoutdatedplaylists);
        MainGui.MainFavoritesButton.setText(local.$favorites);
        MainGui.MainSearchButton.setText(local.$search);
        MainGui.MainSettingsButton.setText(local.$settings);
        MainGui.MainPlaylistManagerButton.setText(local.$playlistManager);
    }

    public static String translate(String input) {
        String output;
        output = input;

        if (origNames.size() > 0) {
            for (int i = 0; i < origNames.size(); i++) {
                if (origNames.get(i).equalsIgnoreCase(input)) {
                    output = translatedNames.get(i);
                    break;
                }
            }
        }
        return output;
    }

    public static String getFileName(String input) {
        String output = "playlist_" + input + ".m3u";
        output = output.replace(" ", "_");
        output = output.replace("(", "_");
        output = output.replace(")", "_");
        output = output.replace("@", "_");
        return output;
    }

    static class runplayer extends Thread {

        @Override
        public void run() {
            new Player();
        }
    }

    static class MainGui extends MyGui {
        public static void SetMainActivity() {
            MyGui.mainFrame.setVisible(true);
            MyGui.catlistFrame.setVisible(false);
            MyGui.playlistFrame.setVisible(false);
            MyGui.globalSearchFrame.setVisible(false);
            MyGui.globalFavoritesFrame.setVisible(false);
            MyGui.settingsFrame.setVisible(false);
            MyGui.playlistManagerFrame.setVisible(false);
        }

        public static void ProviderListActionAdapter() {
            ItemListener itemListener = new ItemListener() {
                public void itemStateChanged(ItemEvent itemEvent) {
                    int index = ActivePlaylist.indexOfName(itemEvent.getItem().toString());
                    selectedProvider = index;
                    checkPlaylistFile(ActivePlaylist.getFile(index));
                }
            };
            comboBox.addItemListener(itemListener);
        }

        public static void LanguageListActionAdapter() {
            ItemListener itemListener = new ItemListener() {
                public void itemStateChanged(ItemEvent itemEvent) {
                    int index = localsList.indexOf(itemEvent.getItem());
                    String translationFile = null;
                    switch (index) {
                        case 0:
                            MainActivity.lang = "eng";
                            translationFile = "strings-en.xml";
                            break;
                        case 1:
                            MainActivity.lang = "ukr";
                            translationFile = "strings-uk.xml";
                            break;
                        case 2:
                            MainActivity.lang = "rus";
                            translationFile = "strings-ru.xml";
                            break;
                    }
                    local = new Locals();
                    applyLocals();
                    prepareStrings(translationFile);
                    checkPlaylistFile(ActivePlaylist.getFile(selectedProvider));
                }
            };
            comboBox_1.addItemListener(itemListener);
        }

        public static void mainButtonsActionListener() {
            MainUpdateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    downloadPlaylist(selectedProvider);
                }
            });

            MainUpdateAllButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    updateoutdatedplaylistact = new UpdateOutdatedPlaylistActivity();
                    //updateAll();
                }
            });

            MainOpenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    if (needUpdate) {
                        downloadPlaylist(selectedProvider);
                    }
                    readPlaylist(ActivePlaylist.getFile(selectedProvider), ActivePlaylist.getType(selectedProvider));
                    loadFavorites();
                    if (playlistWithGroup) {
                        catlistact = new CatlistActivity();
                    } else {
                        selectedCategory = local.$all;
                        playlistact = new PlaylistActivity();
                    }
                }
            });

            MainSearchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    globalSearchName.clear();
                    globalSearchUrl.clear();
                    globalSearchProv.clear();
                    searchString = MainSearchtextField.getText();
                    GlobalSearchActivity.GlobalSearchGui.SetGlobalSearchActivity();
                    globalsearchact = new GlobalSearchActivity();
                }
            });

            MainFavoritesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    favoriteList.clear();
                    favoriteProvList.clear();
                    loadFavorites();
                    GlobalFavoritesActivity.GlobalFavoritesGui.SetGlobalFavoritesActivity();
                    globalfavoritesact = new GlobalFavoritesActivity();
                }
            });

            MainSettingsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    settingsact = new SettingsActivity();
                }
            });

            MainPlaylistManagerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    playmanact = new PlaylistManagerActivity();
                }
            });

        }
    }

}