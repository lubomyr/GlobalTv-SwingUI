import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GlobalSearchActivity extends MainActivity {
    GlobalSearchActivity() {
        applyGlobalSearchLocals();
        prepare_globalSearch();
        showSearchResults();
    }

    public static void applyGlobalSearchLocals() {
        GlobalSearchGui.globalSearchBackButton.setText(local.$back);
        GlobalSearchGui.globalSearchOpenButton.setText(local.$openChannel);
        GlobalSearchGui.globalSearchAddFavButton.setText(local.$addToFavorites);
        GlobalSearchGui.globalSearchRemoveFavButton.setText(local.$removeFromFavorites);
    }

    public void prepare_globalSearch() {
        favoriteList.clear();
        favoriteProvList.clear();
        loadFavorites();
        for (int i = 0; i < ActivePlaylist.size(); i++) {
            readPlaylist(ActivePlaylist.getFile(i), ActivePlaylist.getType(i));

            String chName;
            for (int j = 0; j < channel.size(); j++) {
                chName = channel.getName(j).toLowerCase();
                if (chName.contains(searchString.toLowerCase())) {
                    globalSearchName.add(channel.getName(j));
                    globalSearchUrl.add(channel.getLink(j));
                    globalSearchProv.add(ActivePlaylist.getName(i));
                }
            }
        }
    }

    public void showSearchResults() {
        GlobalSearchGui.globalSearchLabel.setText(local.$searchFor + " '" + searchString + "' - " + globalSearchName.size() + local.$channels);
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (int i = 0; i < globalSearchName.size(); i++) {
            model.addElement(globalSearchName.get(i) + "            '" + globalSearchProv.get(i) + "'");
        }
        GlobalSearchGui.globalSearchList.setModel(model);
    }

    static class GlobalSearchGui extends MyGui {
        static JButton globalSearchBackButton;
        static JButton globalSearchOpenButton;
        static JButton globalSearchAddFavButton;
        static JButton globalSearchRemoveFavButton;
        static JLabel globalSearchLabel;
        static JList<String> globalSearchList;
        static JScrollPane globalSearchScrollBar;

        public static void SetGlobalSearchActivity() {
            MyGui.mainFrame.setVisible(false);
            MyGui.catlistFrame.setVisible(false);
            MyGui.playlistFrame.setVisible(false);
            MyGui.globalSearchFrame.setVisible(true);
            MyGui.globalFavoritesFrame.setVisible(false);
            globalSearchOpenButton.setVisible(false);
            globalSearchAddFavButton.setVisible(false);
            globalSearchRemoveFavButton.setVisible(false);
        }

        protected static void initialize() {
            globalSearchFrame = new JFrame();
            globalSearchFrame.setBounds(posx, posy, 450, 445);
            globalSearchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            globalSearchFrame.getContentPane().setLayout(null);

            globalSearchBackButton = new JButton("Back");
            globalSearchBackButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    globalsearchact = null;
                    MainActivity.MainGui.SetMainActivity();
                }
            });
            globalSearchBackButton.setBounds(310, 11, 114, 50);
            globalSearchFrame.getContentPane().add(globalSearchBackButton);

            globalSearchOpenButton = new JButton("Open Channel");
            globalSearchOpenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    runplayer rp = new runplayer();
                    rp.start();
                }
            });
            globalSearchOpenButton.setBounds(38, 351, 159, 50);
            globalSearchFrame.getContentPane().add(globalSearchOpenButton);

            globalSearchAddFavButton = new JButton("Add to Favorites");
            globalSearchAddFavButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    MainActivity.favoriteList.add(selectedChannel);
                    MainActivity.favoriteProvList.add(selectedPlaylist);
                    saveFavorites();
                    globalSearchRemoveFavButton.setVisible(true);
                    globalSearchAddFavButton.setVisible(false);
                }
            });
            globalSearchAddFavButton.setBounds(237, 351, 159, 50);
            globalSearchFrame.getContentPane().add(globalSearchAddFavButton);

            globalSearchRemoveFavButton = new JButton("Remove from Favorites");
            globalSearchRemoveFavButton.setBounds(237, 351, 159, 50);
            globalSearchFrame.getContentPane().add(globalSearchRemoveFavButton);

            globalSearchLabel = new JLabel("Select Channel");
            globalSearchLabel.setBounds(154, 69, 118, 23);
            globalSearchFrame.getContentPane().add(globalSearchLabel);

            globalSearchList = new JList<String>();
            globalSearchList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    int index = globalSearchList.getSelectedIndex();
                    if (index != -1) {
                        selectedChannel = globalSearchName.get(index);
                        selectedLink = globalSearchUrl.get(index);
                        selectedPlaylist = globalSearchProv.get(index);

                        globalSearchOpenButton.setVisible(true);

                        Boolean changesAllowed = true;
                        for (int i = 0; i < favoriteList.size(); i++) {
                            if (selectedChannel.equals(favoriteList.get(i)) && selectedPlaylist.equals(favoriteProvList.get(i)))
                                changesAllowed = false;
                        }

                        if (changesAllowed) {
                            globalSearchAddFavButton.setVisible(true);
                            globalSearchRemoveFavButton.setVisible(false);
                        } else {
                            globalSearchRemoveFavButton.setVisible(true);
                            globalSearchAddFavButton.setVisible(false);
                        }

                    }
                }
            });
            globalSearchList.setBounds(38, 103, 340, 237);

            globalSearchScrollBar = new JScrollPane();
            globalSearchScrollBar.setBounds(38, 103, 340, 237);
            globalSearchFrame.getContentPane().add(globalSearchScrollBar, BorderLayout.CENTER);
            globalSearchScrollBar.setViewportView(globalSearchList);
        }

    }

}