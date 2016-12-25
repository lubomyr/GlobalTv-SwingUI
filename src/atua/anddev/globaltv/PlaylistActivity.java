package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.SearchDialog;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Playlist;
import atua.anddev.globaltv.form.PlaylistForm;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

class PlaylistActivity implements Services {
    private PlaylistForm playlistForm;
    private List<String> playlist = new ArrayList<String>();
    private List<String> playlistUrl = new ArrayList<String>();
    private String selectedChannel;
    private String selectedLink;
    private Playlist selectedPlaylist;

    PlaylistActivity() {
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
        for (Channel chn : channelService.getAllChannels()) {
            if (catName.equals(tService.local("all"))) {
                playlist.add(chn.getName());
                playlistUrl.add(chn.getUrl());
            } else if (catName.equals(chn.getCategory())) {
                playlist.add(chn.getName());
                playlistUrl.add(chn.getUrl());
            }
        }

        playlistForm.playlistInfoLabel.setText(playlist.size() + " - " + tService.local("channels"));

        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "program"};
        data = new Object[playlist.size()][cols];
        for (int row = 0; row < playlist.size(); row++) {
            data[row][0] = playlist.get(row);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < playlist.size(); row++) {
                    playlistForm.table1.setValueAt(guideService.getProgramTitle(playlist.get(row)), row, 1);
                }
            }
        }).start();
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        playlistForm.table1.setModel(model);
        playlistForm.pack();
    }

    private void actionSelector() {
        playlistForm.openChannelButton.setVisible(false);
        playlistForm.addToFavoritesButton.setVisible(false);
        playlistForm.removeFromFavoritesButton.setVisible(false);
        playlistForm.guidePanel.setVisible(false);
        playlistForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = playlistForm.table1.getSelectedRow();
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

                    String title = guideService.getProgramTitle(selectedChannel);
                    if ((title != null) && !title.isEmpty()) {
                        playlistForm.guidePanel.setVisible(true);
                        playlistForm.guideTextArea.setText(title);
                    } else {
                        playlistForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(selectedChannel);
                    if ((desc != null) && !desc.isEmpty()) {
                        playlistForm.guideTextArea.append("\n" + desc);
                        playlistForm.guideTextArea.setLineWrap(true);
                        playlistForm.guideTextArea.setWrapStyleWord(true);
                    }

                    playlistForm.pack();
                } else {
                    playlistForm.openChannelButton.setVisible(false);
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
                favoriteService.saveFavorites();
                playlistForm.removeFromFavoritesButton.setVisible(true);
                playlistForm.addToFavoritesButton.setVisible(false);
            }
        });
        playlistForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedChannel, selectedPlaylist.getName());
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
                playlistForm.removeFromFavoritesButton.setVisible(false);
                playlistForm.addToFavoritesButton.setVisible(true);
            }
        });
    }

}
