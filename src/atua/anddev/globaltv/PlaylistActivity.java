package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.SearchDialog;
import atua.anddev.globaltv.entity.Playlist;
import atua.anddev.globaltv.form.PlaylistForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity implements Services {
    private PlaylistForm playlistForm;
    private List<String> playlist = new ArrayList<String>();
    private List<String> playlistUrl = new ArrayList<String>();
    private String selectedChannel;
    private String selectedLink;
    private Playlist selectedPlaylist;

    public PlaylistActivity() {
        playlistForm = new PlaylistForm();
        applyLocals();
        openCategory(Global.selectedCategory);
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        playlistForm.favoritesButton.setText(tService.local("favorites"));
        playlistForm.searchButton.setText(tService.local("search"));
        playlistForm.openChannelButton.setText(tService.local("openChannel"));
        playlistForm.addToFavoritesButton.setText(tService.local("addToFavorites"));
        playlistForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void openCategory(final String catName) {
        String plname = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
        playlist = catName.equals(tService.local("all")) ?
                channelService.getChannelsByPlist(plname) : channelService.getChannelsByCategory(plname, catName);
        playlistUrl = catName.equals(tService.local("all")) ?
                channelService.getChannelsUrlByPlist(plname) : channelService.getChannelsUrlByCategory(plname, catName);

        playlistForm.playlistInfoLabel.setText(playlist.size() + " - " + tService.local("channels"));

        DefaultListModel<String> model = new DefaultListModel<String>();
        for (String str : playlist) {
            model.addElement(str);
        }
        playlistForm.list1.setModel(model);
        playlistForm.pack();
    }

    private void actionSelector() {
        playlistForm.openChannelButton.setVisible(false);
        playlistForm.addToFavoritesButton.setVisible(false);
        playlistForm.removeFromFavoritesButton.setVisible(false);
        playlistForm.list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int index = playlistForm.list1.getSelectedIndex();
                if (index != -1) {
                    selectedChannel = playlist.get(index);
                    selectedLink = playlistUrl.get(index);
                    selectedPlaylist = playlistService.getActivePlaylistById(MainActivity.selectedProvider);
                    playlistForm.openChannelButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedChannel, selectedPlaylist.getName()) == -1) {
                        playlistForm.addToFavoritesButton.setVisible(true);
                        playlistForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        playlistForm.removeFromFavoritesButton.setVisible(true);
                        playlistForm.addToFavoritesButton.setVisible(false);
                    }
                } else {
                    playlistForm.openChannelButton.setVisible(false);
                }
            }
        });
    }

    private void buttonActionListener() {
        playlistForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                channelService.openURL(selectedLink);
            }
        });
        playlistForm.favoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FavlistActivity();
            }
        });
        playlistForm.searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchDialog("local");
            }
        });
        playlistForm.addToFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                favoriteService.addToFavoriteList(selectedChannel, selectedPlaylist.getName());
                playlistForm.removeFromFavoritesButton.setVisible(true);
                playlistForm.addToFavoritesButton.setVisible(false);
            }
        });
        playlistForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedChannel, selectedPlaylist.getName());
                favoriteService.deleteFromFavoritesById(index);
                playlistForm.removeFromFavoritesButton.setVisible(false);
                playlistForm.addToFavoritesButton.setVisible(true);
            }
        });
    }
}
