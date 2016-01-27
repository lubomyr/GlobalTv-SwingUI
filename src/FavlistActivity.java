import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FavlistActivity extends MainActivity {
    private static ArrayList<String> favlist;
    private static DefaultListModel<String> model;

    FavlistActivity() {
        applyFavoritesLocals();
        showLocalFavorites();
    }

    public static void applyFavoritesLocals() {
        FavlistGui.localFavoritesBackButton.setText(local.$back);
        FavlistGui.localFavoritesOpenButton.setText(local.$openChannel);
        FavlistGui.localFavoritesRemoveFavButton.setText(local.$removeFromFavorites);
    }

    public void showLocalFavorites() {
        favlist = new ArrayList<String>();
        model = new DefaultListModel<String>();
        for (int i = 0; i < favoriteList.size(); i++) {
            for (int j = 0; j < channel.size(); j++) {
                if (favoriteList.get(i).equals(channel.getName(j)) && !favlist.contains(favoriteList.get(i))) {
                    favlist.add(favoriteList.get(i));
                    model.addElement(favoriteList.get(i));
                }
            }
        }
        FavlistGui.localFavoritesList.setModel(model);
        FavlistGui.localFavoritesLabel.setText(playlist.size() + local.$channels);
    }

    static class FavlistGui extends MyGui {
        static JButton localFavoritesBackButton;
        static JLabel localFavoritesLabel;
        static JButton localFavoritesOpenButton;
        static JButton localFavoritesRemoveFavButton;
        static JList<String> localFavoritesList;
        static JScrollPane localFavoritesScrollBar;

        public static void SetFavlistActivity() {
            MyGui.mainFrame.setVisible(false);
            MyGui.catlistFrame.setVisible(false);
            MyGui.playlistFrame.setVisible(false);
            MyGui.localFavoritesFrame.setVisible(true);
            localFavoritesOpenButton.setVisible(false);
            localFavoritesRemoveFavButton.setVisible(false);
        }

        protected static void initialize() {
            localFavoritesFrame = new JFrame();
            localFavoritesFrame.setBounds(posx, posy, 450, 445);
            localFavoritesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            localFavoritesFrame.getContentPane().setLayout(null);

            localFavoritesBackButton = new JButton("Back");
            localFavoritesBackButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    favoritesact = null;
                    if (playlistact != null)
                        PlaylistActivity.PlaylistGui.SetPlaylistActivity();
                    else
                        CatlistActivity.CatlistGui.SetCatlistActivity();
                }
            });
            localFavoritesBackButton.setBounds(310, 11, 114, 50);
            localFavoritesFrame.getContentPane().add(localFavoritesBackButton);

            localFavoritesOpenButton = new JButton("Open Channel");
            localFavoritesOpenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    runplayer rp = new runplayer();
                    rp.start();
                }
            });
            localFavoritesOpenButton.setBounds(38, 351, 159, 50);
            localFavoritesFrame.getContentPane().add(localFavoritesOpenButton);

            localFavoritesRemoveFavButton = new JButton("Remove from Favorites");
            localFavoritesRemoveFavButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    favoriteProvList.remove(favoriteList.indexOf(selectedChannel));
                    favoriteList.remove(selectedChannel);
                    model.removeElement(selectedChannel);
                    saveFavorites();
                }
            });
            localFavoritesRemoveFavButton.setBounds(237, 351, 159, 50);
            localFavoritesFrame.getContentPane().add(localFavoritesRemoveFavButton);

            localFavoritesLabel = new JLabel("Select Channel");
            localFavoritesLabel.setBounds(154, 69, 118, 23);
            localFavoritesFrame.getContentPane().add(localFavoritesLabel);

            localFavoritesList = new JList<String>();
            localFavoritesList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    int index = localFavoritesList.getSelectedIndex();
                    if (index != -1) {
                        selectedItem = index;
                        selectedChannel = favlist.get(index);
                        for (int j = 0; j < channel.size(); j++) {
                            if (channel.getName(j).equals(favlist.get(index))) {
                                selectedLink = channel.getLink(j);
                                break;
                            }
                        }
                        localFavoritesOpenButton.setVisible(true);
                        localFavoritesRemoveFavButton.setVisible(true);
                    }
                }
            });
            localFavoritesList.setBounds(38, 103, 340, 237);

            localFavoritesScrollBar = new JScrollPane();
            localFavoritesScrollBar.setBounds(38, 103, 340, 237);
            localFavoritesFrame.getContentPane().add(localFavoritesScrollBar, BorderLayout.CENTER);
            localFavoritesScrollBar.setViewportView(localFavoritesList);
        }
    }
}
