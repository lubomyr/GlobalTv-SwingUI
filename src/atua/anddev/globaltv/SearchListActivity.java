package atua.anddev.globaltv;

import atua.anddev.globaltv.form.SearchForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchListActivity implements Services {
    private SearchForm searchForm;
    private String searchString;
    private List<String> playlist = new ArrayList<String>();
    private List<String> playlistUrl = new ArrayList<String>();

    public SearchListActivity(String searchString) {
        searchForm = new SearchForm();
        this.searchString = searchString;
        applyLocals();
        showSearchResults();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        searchForm.openChannelButton.setText(tService.local("openChannel"));
        searchForm.addToFavoritesButton.setText(tService.local("addToFavorites"));
        searchForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void showSearchResults() {
        String plist = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
        playlist = searchService.searchChannelsByPlistAndName(plist, searchString.toLowerCase());
        playlistUrl = searchService.searchChannelsUrlByPlistAndName(plist, searchString.toLowerCase());

        DefaultListModel<String> model = new DefaultListModel<String>();
        for (String str : playlist) {
            model.addElement(str);
        }
        searchForm.list1.setModel(model);
        searchForm.searchLabel.setText(playlist.size() + " - " + tService.local("pcs"));
        searchForm.pack();
    }

    private void buttonActionListener() {
        searchForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = searchForm.list1.getSelectedIndex();
                channelService.openURL(playlistUrl.get(index));
            }
        });
        searchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.list1.getSelectedIndex();
                String selectedName = playlist.get(selected);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                favoriteService.addToFavoriteList(selectedName, selectedProv);
                searchForm.removeFromFavoritesButton.setVisible(true);
                searchForm.addToFavoritesButton.setVisible(false);
            }
        });
        searchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.list1.getSelectedIndex();
                String selectedName = playlist.get(selected);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv);
                favoriteService.deleteFromFavoritesById(index);
                searchForm.removeFromFavoritesButton.setVisible(false);
                searchForm.addToFavoritesButton.setVisible(true);
            }
        });
    }

    private void actionSelector() {
        searchForm.openChannelButton.setVisible(false);
        searchForm.addToFavoritesButton.setVisible(false);
        searchForm.removeFromFavoritesButton.setVisible(false);
        searchForm.list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int index = searchForm.list1.getSelectedIndex();
                String selectedName = playlist.get(index);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                if (index != -1) {
                    searchForm.openChannelButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv) == -1) {
                        searchForm.addToFavoritesButton.setVisible(true);
                        searchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        searchForm.addToFavoritesButton.setVisible(false);
                        searchForm.removeFromFavoritesButton.setVisible(true);
                    }
                } else {
                    searchForm.openChannelButton.setVisible(false);
                    searchForm.addToFavoritesButton.setVisible(false);
                    searchForm.removeFromFavoritesButton.setVisible(false);
                }
            }
        });
    }

}
