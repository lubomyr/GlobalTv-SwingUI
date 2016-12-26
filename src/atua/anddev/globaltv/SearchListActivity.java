package atua.anddev.globaltv;

import atua.anddev.globaltv.form.SearchForm;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
        searchForm.guideButton.setText(tService.local("showProgramGuide"));
    }

    private void showSearchResults() {
        String chName;
        for (int i = 0; i < channelService.sizeOfChannelList(); i++) {
            chName = channelService.getChannelById(i).getName().toLowerCase();
            if (chName.contains(searchString.toLowerCase())) {
                playlist.add(channelService.getChannelById(i).getName());
                playlistUrl.add(channelService.getChannelById(i).getUrl());
            }
        }

        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "program"};
        data = new Object[playlist.size()][cols];
        for (int row = 0; row < playlist.size(); row++) {
            data[row][0] = playlist.get(row);
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        searchForm.table1.setModel(model);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < playlist.size(); row++) {
                    searchForm.table1.setValueAt(guideService.getProgramTitle(playlist.get(row)), row , 1);
                }
            }
        }).start();
        searchForm.searchLabel.setText(playlist.size() + " - " + tService.local("pcs"));
        searchForm.pack();
    }

    private void buttonActionListener() {
        searchForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = searchForm.table1.getSelectedRow();
                channelService.openURL(playlistUrl.get(index));
            }
        });
        searchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.table1.getSelectedRow();
                String selectedName = playlist.get(selected);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                favoriteService.addToFavoriteList(selectedName, selectedProv);
                favoriteService.saveFavorites();
                searchForm.removeFromFavoritesButton.setVisible(true);
                searchForm.addToFavoritesButton.setVisible(false);
            }
        });
        searchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.table1.getSelectedRow();
                String selectedName = playlist.get(selected);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv);
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
                searchForm.removeFromFavoritesButton.setVisible(false);
                searchForm.addToFavoritesButton.setVisible(true);
            }
        });
        searchForm.guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.table1.getSelectedRow();
                String selectedName = playlist.get(selected);
                new GuideActivity(selectedName);
            }
        });
    }

    private void actionSelector() {
        searchForm.openChannelButton.setVisible(false);
        searchForm.addToFavoritesButton.setVisible(false);
        searchForm.removeFromFavoritesButton.setVisible(false);
        searchForm.guidePanel.setVisible(false);
        searchForm.guideButton.setVisible(false);
        searchForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = searchForm.table1.getSelectedRow();
                String selectedName = playlist.get(index);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                if (index != -1) {
                    searchForm.openChannelButton.setVisible(true);
                    searchForm.guideButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv) == -1) {
                        searchForm.addToFavoritesButton.setVisible(true);
                        searchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        searchForm.addToFavoritesButton.setVisible(false);
                        searchForm.removeFromFavoritesButton.setVisible(true);
                    }
                    String selectedChannel = searchForm.table1.getValueAt(index,0).toString();
                    String title = guideService.getProgramTitle(selectedChannel);
                    if ((title != null) && !title.isEmpty()) {
                        searchForm.guidePanel.setVisible(true);
                        searchForm.guideTextArea.setText(title);
                    } else {
                        searchForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(selectedChannel);
                    if ((desc != null) && !desc.isEmpty()) {
                        searchForm.guideTextArea.append("\n" + desc);
                        searchForm.guideTextArea.setLineWrap(true);
                        searchForm.guideTextArea.setWrapStyleWord(true);
                    }
                    searchForm.pack();
                } else {
                    searchForm.openChannelButton.setVisible(false);
                    searchForm.addToFavoritesButton.setVisible(false);
                    searchForm.removeFromFavoritesButton.setVisible(false);
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

}
