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

public class PlaylistActivity extends MainActivity {
    PlaylistActivity() {
        playlist.clear();
        playlistUrl.clear();
        PlaylistGui.SetPlaylistActivity();
        applyPlaylistLocals();
        createplaylist(selectedCategory);
        showPlaylist();
    }

    public static void applyPlaylistLocals() {
        PlaylistGui.PlayBackButton.setText(local.$back);
        PlaylistGui.PlaySearchButton.setText(local.$search);
        PlaylistGui.PlayFavoriteButton.setText(local.$favorites);
        PlaylistGui.PlayOpenButton.setText(local.$openChannel);
        PlaylistGui.PlayAddFavButton.setText(local.$addToFavorites);
        PlaylistGui.PlayRemoveFavButton.setText(local.$removeFromFavorites);
    }

    private void createplaylist(final String catName) {
        for (int i = 0; i < channel.size(); i++) {
            if (catName.equals(local.$all)) {
                playlist.add(channel.getName(i));
                playlistUrl.add(channel.getLink(i));
            } else if (catName.equals(channel.getCategory(i))) {
                playlist.add(channel.getName(i));
                playlistUrl.add(channel.getLink(i));
            }
        }
    }

    private void showPlaylist() {
        PlaylistGui.PlaylistLabel.setText(playlist.size() + local.$channels);
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (int i = 0; i < playlist.size(); i++) {
            model.addElement(playlist.get(i));
        }
        PlaylistGui.playList.setModel(model);
    }

    static class PlaylistGui extends MyGui {
        static JButton PlayFavoriteButton;
        static JButton PlayBackButton;
        static JButton PlayOpenButton;
        static JButton PlayAddFavButton;
        static JButton PlayRemoveFavButton;
        static JList<String> playList;
        static JScrollPane playScrollBar;
        static JButton PlaySearchButton;
        static JLabel PlaylistLabel;

        public static void SetPlaylistActivity() {
            MyGui.mainFrame.setVisible(false);
            MyGui.catlistFrame.setVisible(false);
            MyGui.playlistFrame.setVisible(true);
            MyGui.globalSearchFrame.setVisible(false);
            MyGui.globalFavoritesFrame.setVisible(false);
            MyGui.localFavoritesFrame.setVisible(false);
            PlayOpenButton.setVisible(false);
            PlayAddFavButton.setVisible(false);
            PlayRemoveFavButton.setVisible(false);
        }

        protected static void initialize() {
            playlistFrame = new JFrame();
            playlistFrame.setBounds(posx, posy, 450, 445);
            playlistFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            playlistFrame.getContentPane().setLayout(null);

            PlayFavoriteButton = new JButton("Favorites");
            PlayFavoriteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    FavlistActivity.FavlistGui.SetFavlistActivity();
                    favoritesact = new FavlistActivity();
                }
            });
            PlayFavoriteButton.setBounds(38, 10, 106, 50);
            playlistFrame.getContentPane().add(PlayFavoriteButton);

            PlayBackButton = new JButton("Back");
            PlayBackButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    playlistact = null;
                    if (playlistWithGroup)
                        CatlistActivity.CatlistGui.SetCatlistActivity();
                    else
                        MainActivity.MainGui.SetMainActivity();
                }
            });
            PlayBackButton.setBounds(282, 10, 114, 50);
            playlistFrame.getContentPane().add(PlayBackButton);

            PlayOpenButton = new JButton("Open Channel");
            PlayOpenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    runplayer rp = new runplayer();
                    rp.start();
                }
            });
            PlayOpenButton.setBounds(38, 351, 159, 50);
            playlistFrame.getContentPane().add(PlayOpenButton);

            PlayAddFavButton = new JButton("Add to Favorites");
            PlayAddFavButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    MainActivity.favoriteList.add(selectedChannel);
                    MainActivity.favoriteProvList.add(selectedPlaylist);
                    saveFavorites();
                    PlayRemoveFavButton.setVisible(true);
                    PlayAddFavButton.setVisible(false);
                }
            });
            PlayAddFavButton.setBounds(237, 351, 159, 50);
            playlistFrame.getContentPane().add(PlayAddFavButton);

            PlayRemoveFavButton = new JButton("Remove from Favorites");
            PlayRemoveFavButton.setBounds(237, 351, 159, 50);
            playlistFrame.getContentPane().add(PlayRemoveFavButton);

            playList = new JList<String>();
            playList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    int index = playList.getSelectedIndex();
                    if (index != -1) {
                        selectedChannel = playlist.get(index);
                        selectedLink = playlistUrl.get(index);
                        selectedPlaylist = ActivePlaylist.getName(selectedProvider);
                        PlayOpenButton.setVisible(true);
                        if (!(favoriteList.contains(selectedChannel) && ActivePlaylist.getName(selectedProvider).equals(favoriteProvList.get(favoriteList.indexOf(selectedChannel))))) {
                            PlayAddFavButton.setVisible(true);
                            PlayRemoveFavButton.setVisible(false);
                        } else {
                            PlayRemoveFavButton.setVisible(true);
                            PlayAddFavButton.setVisible(false);
                        }
                    }
                }
            });
            playList.setBounds(38, 103, 340, 237);

            playScrollBar = new JScrollPane();
            playScrollBar.setBounds(38, 103, 340, 237);
            playlistFrame.getContentPane().add(playScrollBar, BorderLayout.CENTER);
            playScrollBar.setViewportView(playList);

            PlaySearchButton = new JButton("Search");
            PlaySearchButton.setBounds(154, 10, 118, 50);
            playlistFrame.getContentPane().add(PlaySearchButton);

            PlaylistLabel = new JLabel("Select Channel");
            PlaylistLabel.setBounds(154, 69, 118, 23);
            playlistFrame.getContentPane().add(PlaylistLabel);
        }
    }

}