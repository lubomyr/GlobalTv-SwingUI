package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.GlobalSearchForm;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GlobalSearchActivity implements Services {
    private GlobalSearchForm globalSearchForm;
    private String searchString;

    public GlobalSearchActivity(String search) {
        this.searchString = search;
        globalSearchForm = new GlobalSearchForm();
        applyLocals();
        prepare_globalSearch();
        showSearchResults();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        globalSearchForm.openChannelButton.setText(tService.local("openChannel"));
        globalSearchForm.addToFavoritesButton.setText(tService.local("addToFavorites"));
        globalSearchForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void prepare_globalSearch() {
        for (int i = 0; i < playlistService.sizeOfActivePlaylist(); i++) {
            playlistService.readPlaylist(i);

            String chName;
            for (Channel chn : channelService.getAllChannels()) {
                chName = chn.getName().toLowerCase();
                if (chName.contains(searchString.toLowerCase())) {
                    searchService.addToSearchList(chn.getName(),
                            chn.getUrl(), playlistService.getActivePlaylistById(i).getName());
                }
            }
        }
    }

    private void showSearchResults() {
        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "playlist"};
        data = new Object[searchService.sizeOfSearchList()][cols];
        for (int row = 0; row < searchService.sizeOfSearchList(); row++) {
            data[row][0] = searchService.getSearchListById(row).getName();
            data[row][1] = searchService.getSearchListById(row).getProv();
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        globalSearchForm.table1.setModel(model);
        globalSearchForm.globalSearchLabel.setText(searchService.sizeOfSearchList() + " - " + tService.local("pcs"));
        globalSearchForm.pack();
    }

    private void actionSelector() {
        globalSearchForm.openChannelButton.setVisible(false);
        globalSearchForm.removeFromFavoritesButton.setVisible(false);
        globalSearchForm.addToFavoritesButton.setVisible(false);
        globalSearchForm.table1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.getSearchListById(selected).getName();
                String selectedProv = searchService.getSearchListById(selected).getProv();
                if (selected != -1) {
                    globalSearchForm.openChannelButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv) == -1) {
                        globalSearchForm.addToFavoritesButton.setVisible(true);
                        globalSearchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        globalSearchForm.addToFavoritesButton.setVisible(false);
                        globalSearchForm.removeFromFavoritesButton.setVisible(true);
                    }
                } else {
                    globalSearchForm.openChannelButton.setVisible(false);
                    globalSearchForm.removeFromFavoritesButton.setVisible(false);
                    globalSearchForm.addToFavoritesButton.setVisible(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void buttonActionListener() {
        globalSearchForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                channelService.openURL(searchService.getSearchListById(selected).getUrl());
            }
        });
        globalSearchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.getSearchListById(selected).getName();
                String selectedProv = searchService.getSearchListById(selected).getProv();
                favoriteService.addToFavoriteList(selectedName, selectedProv);
                favoriteService.saveFavorites();
                globalSearchForm.removeFromFavoritesButton.setVisible(true);
                globalSearchForm.addToFavoritesButton.setVisible(false);
            }
        });
        globalSearchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.getSearchListById(selected).getName();
                String selectedProv = searchService.getSearchListById(selected).getProv();
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv);
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
                globalSearchForm.removeFromFavoritesButton.setVisible(false);
                globalSearchForm.addToFavoritesButton.setVisible(true);
            }
        });
    }

}
