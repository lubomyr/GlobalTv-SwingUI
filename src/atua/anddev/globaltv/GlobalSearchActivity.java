package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.GlobalSearchForm;
import atua.anddev.globaltv.models.TableModelWithIcons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        globalSearchForm.openChannelButton.setText(tService.getString("openChannel"));
        globalSearchForm.addToFavoritesButton.setText(tService.getString("addToFavorites"));
        globalSearchForm.removeFromFavoritesButton.setText(tService.getString("removeFromFavorites"));
        globalSearchForm.guideButton.setText(tService.getString("showProgramGuide"));
    }

    private void prepare_globalSearch() {
        for (int i = 0; i < playlistService.sizeOfActivePlaylist(); i++) {
            playlistService.readPlaylist(i);

            String chName;
            for (Channel chn : channelService.getAllChannels()) {
                chName = chn.getName().toLowerCase();
                if (chName.contains(searchString.toLowerCase())) {
                    searchService.addToSearchList(chn);
                }
            }
        }
    }

    private void showSearchResults() {
        String[] colNames;
        Object[][] data;
        int cols = 4;
        colNames = new String[]{"icon", "name", "playlist", "program"};
        data = new Object[searchService.sizeOfSearchList()][cols];
        for (int row = 0; row < searchService.sizeOfSearchList(); row++) {
            data[row][1] = searchService.getSearchChannelById(row).getName();
            data[row][2] = searchService.getSearchChannelById(row).getProvider();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < searchService.sizeOfSearchList(); row++) {
                    ImageIcon imageIcon = logoService.getIcon(searchService.getSearchChannelById(row).getName());
                    if (imageIcon == null)
                        imageIcon = new ImageIcon("");
                    globalSearchForm.table1.setValueAt(imageIcon, row, 0);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < searchService.sizeOfSearchList(); row++) {
                    globalSearchForm.table1.setValueAt(guideService.getProgramTitle(searchService.getSearchChannelById(row).getName()),
                            row, 3);
                }
            }
        }).start();
        DefaultTableModel model = new TableModelWithIcons(data, colNames);
        globalSearchForm.table1.setModel(model);
        globalSearchForm.globalSearchLabel.setText(searchService.sizeOfSearchList() + " - " + tService.getString("pcs"));
        globalSearchForm.table1.setRowHeight(30);
        globalSearchForm.table1.getColumnModel().getColumn(0).setMinWidth(100);
        globalSearchForm.table1.getColumnModel().getColumn(1).setMinWidth(200);
        globalSearchForm.table1.getColumnModel().getColumn(2).setMinWidth(220);
        globalSearchForm.table1.getColumnModel().getColumn(3).setMinWidth(250);
        globalSearchForm.setMinimumSize(new Dimension(770, globalSearchForm.getHeight()));
        globalSearchForm.pack();
    }

    private void actionSelector() {
        globalSearchForm.openChannelButton.setVisible(false);
        globalSearchForm.removeFromFavoritesButton.setVisible(false);
        globalSearchForm.addToFavoritesButton.setVisible(false);
        globalSearchForm.guidePanel.setVisible(false);
        globalSearchForm.guideButton.setVisible(false);
        globalSearchForm.table1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                Channel channel = searchService.getSearchChannelById(selected);
                if (selected != -1) {
                    globalSearchForm.openChannelButton.setVisible(true);
                    globalSearchForm.guideButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(channel.getName(), channel.getProvider()) == -1) {
                        globalSearchForm.addToFavoritesButton.setVisible(true);
                        globalSearchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        globalSearchForm.addToFavoritesButton.setVisible(false);
                        globalSearchForm.removeFromFavoritesButton.setVisible(true);
                    }
                    String title = guideService.getProgramTitle(channel.getName());
                    if ((title != null) && !title.isEmpty()) {
                        globalSearchForm.guidePanel.setVisible(true);
                        globalSearchForm.guideTextArea.setText(title);
                    } else {
                        globalSearchForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(channel.getName());
                    if ((desc != null) && !desc.isEmpty()) {
                        globalSearchForm.guideTextArea.append("\n" + desc);
                        globalSearchForm.guideTextArea.setLineWrap(true);
                        globalSearchForm.guideTextArea.setWrapStyleWord(true);
                    }
                    globalSearchForm.pack();
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
                channelService.openURL(searchService.getSearchChannelById(selected).getUrl());
            }
        });
        globalSearchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                Channel channel = searchService.getSearchChannelById(selected);
                favoriteService.addToFavoriteList(channel.getName(), channel.getProvider());
                favoriteService.saveFavorites();
                globalSearchForm.removeFromFavoritesButton.setVisible(true);
                globalSearchForm.addToFavoritesButton.setVisible(false);
            }
        });
        globalSearchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                Channel channel = searchService.getSearchChannelById(selected);
                int index = favoriteService.indexOfFavoriteByNameAndProv(channel.getName(), channel.getProvider());
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
                globalSearchForm.removeFromFavoritesButton.setVisible(false);
                globalSearchForm.addToFavoritesButton.setVisible(true);
            }
        });
        globalSearchForm.guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.getSearchChannelById(selected).getName();
                new GuideActivity(selectedName);
            }
        });
    }

}
