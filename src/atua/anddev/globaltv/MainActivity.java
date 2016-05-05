package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.AddPlaylistDialog;
import atua.anddev.globaltv.dialog.SearchDialog;
import atua.anddev.globaltv.form.MainForm;
import atua.anddev.globaltv.service.PlaylistService;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lubomyr on 31.03.16.
 */
public class MainActivity implements Services {
    public static int selectedProvider;
    static MainForm mainForm;
    static Boolean needUpdate;
    private static String lang;
    private static List<String> localsList = new ArrayList<String>();

    public static void main(String[] args) {
        if (lang == null)
            lang = Locale.getDefault().getISO3Language();

        dbHelper.createDb();

        //guideService.doInBackground("http://api.torrent-tv.ru/ttv.xmltv.xml.gz");

        if (playlistService.sizeOfOfferedPlaylist() == 0) {
            playlistService.setupProvider();
            favoriteService.favoriteListName.addAll(favoriteService.getAllFavoritesName());
            favoriteService.favoriteProvList.addAll(favoriteService.getAllFavoritesProv());
        }

        if (Global.path_aceplayer == null)
            loadSettings();

        setUIFont(new FontUIResource(new Font("Dialog", 0, Integer.valueOf(Global.selectedFontSize))));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (Global.selectedTheme.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        mainForm = new MainForm();

        showLocals();
        setupProviderView();
        applyLocals();
        try {
            checkPlaylistFile(selectedProvider);
        } catch (Exception ignored) {
        }
        LanguageListActionAdapter();
        ProviderListActionAdapter();
        mainButtonsActionListener();
    }

    private static void applyLocals() {
        mainForm.updatePlaylistButton.setText(tService.local("updatePlaylistButton"));
        mainForm.updateOutdatedPlaylistButton.setText(tService.local("updateOutdatedPlaylists"));
        mainForm.openPlaylistButton.setText(tService.local("openPlaylistButton"));
        mainForm.searchButton.setText(tService.local("search"));
        mainForm.favoritesButton.setText(tService.local("favorites"));
        mainForm.settingsButton.setText(tService.local("settings"));
        mainForm.playlistManagerButton.setText(tService.local("playlistsManagerButton"));
        mainForm.playlistLabel.setText(tService.local("playlist"));
    }

    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), f.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

    private static void showLocals() {
        localsList = new ArrayList<String>();
        localsList.add("English");
        localsList.add("Українська");
        localsList.add("Русский");

        for (int i = 0; i < localsList.size(); i++)
            mainForm.comboBox2.addItem(localsList.get(i));

        String translationFile = null;

        if (lang.equals("eng")) {
            mainForm.comboBox2.setSelectedItem(localsList.get(0));
            translationFile = "values/strings.xml";
        }
        if (lang.equals("ukr")) {
            mainForm.comboBox2.setSelectedItem(localsList.get(1));
            translationFile = "values-uk/strings.xml";
        }
        if (lang.equals("rus")) {
            mainForm.comboBox2.setSelectedItem(localsList.get(2));
            translationFile = "values-ru/strings.xml";
        }
        tService.getTranslationData(translationFile);

        if (playlistService.sizeOfActivePlaylist() == 0) {
            AddPlaylistDialog.main(null);
        }

    }

    private static void setupProviderView() {
        playlistService.fillNamesOfPlaylist();
        for (String str : PlaylistService.activePlaylistName) {
            mainForm.comboBox1.addItem(str);
        }
    }

    private static boolean checkFile(String fname) {
        try {
            InputStream myfile = new FileInputStream(fname);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean checkPlaylistFile(int num) {
        String fname = playlistService.getActivePlaylistById(num).getFile();
        try {
            String updateDateStr = playlistService.getActivePlaylistById(num).getUpdate();
            long currDate = (new Date()).getTime();
            long diffDate, updateDate = 0;
            String tmpText;
            try {
                updateDate = Long.parseLong(updateDateStr);
                diffDate = currDate - updateDate;
            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
                return false;
            }
            int hoursPassed = (int) TimeUnit.MILLISECONDS.toHours(diffDate);
            if (hoursPassed > 12)
                needUpdate = true;
            else
                needUpdate = false;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(diffDate);
            int daysPassed = cal.get(Calendar.DAY_OF_YEAR);
            switch (daysPassed) {
                case 1:
                    tmpText = tService.local("updated") + " " + new Date(updateDate).toLocaleString();
                    break;
                case 2:
                    tmpText = tService.local("updated") + " 1 " + tService.local("dayago");
                    break;
                case 3:
                case 4:
                case 5:
                    tmpText = tService.local("updated") + " " + (daysPassed - 1) + " " + tService.local("daysago");
                    break;
                default:
                    tmpText = tService.local("updated") + " " + (daysPassed - 1) + " " + tService.local("fivedaysago");
                    break;

            }
            mainForm.mainPlaylistInfoLabel.setText(tmpText);
            InputStream myfile = new FileInputStream(myPath + fname);
        } catch (FileNotFoundException e) {
            mainForm.mainPlaylistInfoLabel.setText(tService.local("playlistnotexist"));
            needUpdate = true;
            return false;
        } finally {
            mainForm.pack();
        }
        return true;
    }

    private static void downloadPlaylist(final int num, boolean waitforfinish) {
        DownloadPlaylist dplst = new DownloadPlaylist(num);
        Thread threadDp = new Thread(dplst);
        threadDp.start();
        if (waitforfinish) {
            try {
                threadDp.join();
            } catch (InterruptedException ignored) {
            }
        }
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

    public static String getMd5OfFile(String filePath) {
        String returnVal = "";
        try {
            InputStream input = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            MessageDigest md5Hash = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = input.read(buffer);
                if (numRead > 0) {
                    md5Hash.update(buffer, 0, numRead);
                }
            }
            input.close();

            byte[] md5Bytes = md5Hash.digest();
            for (byte md5Byte : md5Bytes) {
                returnVal += Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return returnVal.toUpperCase();
    }

    public static void loadSettings() {
        try {
            File fXmlFile;
            fXmlFile = new File(myPath + "settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            Global.path_aceplayer = doc.getElementsByTagName("aceplayer").item(0).getTextContent();
            Global.path_vlc = doc.getElementsByTagName("vlcplayer").item(0).getTextContent();
            Global.path_other = doc.getElementsByTagName("otherplayer").item(0).getTextContent();
            Global.selectedTheme = doc.getElementsByTagName("theme").item(0).getTextContent();
            Global.selectedFontSize = doc.getElementsByTagName("fontsize").item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ProviderListActionAdapter() {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                int index = playlistService.indexNameForActivePlaylist(itemEvent.getItem().toString());
                selectedProvider = index;
                checkPlaylistFile(index);
            }
        };
        mainForm.comboBox1.addItemListener(itemListener);
    }

    private static void LanguageListActionAdapter() {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                int index = localsList.indexOf(itemEvent.getItem());
                String translationFile = null;
                switch (index) {
                    case 0:
                        MainActivity.lang = "eng";
                        translationFile = "values/strings.xml";
                        break;
                    case 1:
                        MainActivity.lang = "ukr";
                        translationFile = "values-uk/strings.xml";
                        break;
                    case 2:
                        MainActivity.lang = "rus";
                        translationFile = "values-ru/strings.xml";
                        break;
                }
                tService.getTranslationData(translationFile);
                applyLocals();
                checkPlaylistFile(selectedProvider);
            }
        };
        mainForm.comboBox2.addItemListener(itemListener);
    }

    public static void mainButtonsActionListener() {
        mainForm.updatePlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (playlistService.sizeOfActivePlaylist() == 0) {
                    mainForm.mainPlaylistInfoLabel.setText(tService.local("no_selected_playlist"));
                    return;
                }
                downloadPlaylist(selectedProvider, false);
            }
        });

        mainForm.updateOutdatedPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new UpdateOutdatedPlaylistActivity();
            }
        });

        mainForm.openPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (playlistService.sizeOfActivePlaylist() == 0) {
                    mainForm.mainPlaylistInfoLabel.setText(tService.local("no_selected_playlist"));
                    return;
                }
                if (!checkPlaylistFile(selectedProvider) || needUpdate) {
                    downloadPlaylist(selectedProvider, true);
                }
                int grpcnt = channelService.getCategoriesNumber(playlistService.getActivePlaylistById(selectedProvider).getName());

                if (grpcnt > 1) {
                    new CatlistActivity();
                } else {
                    Global.selectedCategory = tService.local("all");
                    new PlaylistActivity();
                }
            }

        });

        mainForm.searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //searchService.clearSearchList();
                new SearchDialog("global");
            }
        });

        mainForm.favoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new GlobalFavoriteActivity();
            }
        });

        mainForm.settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new SettingsActivity();
            }
        });

        mainForm.playlistManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new PlaylistManagerActivity();
            }
        });

    }

    private static class DownloadPlaylist implements Runnable {
        private int num;

        DownloadPlaylist(int num) {
            this.num = num;
        }

        public void run() {
            try {
                String path = myPath + playlistService.getActivePlaylistById(num).getFile();
                saveUrl(path, playlistService.getActivePlaylistById(num).getUrl());

                needUpdate = false;
                String oldMd5 = playlistService.getActivePlaylistById(num).getMd5();
                String newMd5 = getMd5OfFile(path);
                if (!newMd5.equals(oldMd5)) {
                    playlistService.setMd5(num, newMd5);
                    playlistService.setUpdateDate(num, new Date().getTime());
                    playlistService.readPlaylist(num);
                    checkPlaylistFile(selectedProvider);
                    mainForm.mainWarningLabel.setText(playlistService.getActivePlaylistById(num).getName() + " - " + tService.local("updated"));

                }
                ;
            } catch (Exception e) {
                mainForm.mainWarningLabel.setText(playlistService.getActivePlaylistById(num).getName() + " - " + tService.local("failed"));
                System.out.println("Error: " + e.toString());
            } finally {
                mainForm.pack();
            }
        }
    }

}
