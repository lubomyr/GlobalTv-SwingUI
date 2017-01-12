package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.SearchForm;
import atua.anddev.globaltv.models.TableModelWithIcons;
import atua.anddev.globaltv.runnables.GetIconRunnable;
import atua.anddev.globaltv.runnables.GetProgramTitleRunnable;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class SearchListActivity implements Services {
    private SearchForm searchForm;
    private String searchString;
    private List<Channel> searchlist;

    public SearchListActivity(String searchString) {
        searchForm = new SearchForm();
        searchlist = new ArrayList<>();
        this.searchString = searchString;
        applyLocals();
        showSearchResults();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        searchForm.openChannelButton.setText(tService.getString("openChannel"));
        searchForm.addToFavoritesButton.setText(tService.getString("addToFavorites"));
        searchForm.removeFromFavoritesButton.setText(tService.getString("removeFromFavorites"));
        searchForm.guideButton.setText(tService.getString("showProgramGuide"));
    }

    private void showSearchResults() {
        String chName;
        for (int i = 0; i < channelService.sizeOfChannelList(); i++) {
            chName = channelService.getChannelById(i).getName().toLowerCase();
            if (chName.contains(searchString.toLowerCase())) {
                Channel channel = channelService.getChannelById(i);
                channel.setProvider(playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName());
                searchlist.add(channel);
            }
        }

        String[] colNames;
        Object[][] data;
        int cols = 3;
        colNames = new String[]{"icon", "name", "program"};
        data = new Object[searchlist.size()][cols];
        for (int row = 0; row < searchlist.size(); row++) {
            data[row][1] = searchlist.get(row).getName();
        }
        DefaultTableModel model = new TableModelWithIcons(data, colNames);
        searchForm.table1.setModel(model);

        GetIconRunnable getIconRunnable = new GetIconRunnable(searchForm.table1, searchlist);
        Thread getIconThread = new Thread(getIconRunnable);
        getIconThread.start();

        GetProgramTitleRunnable getProgramTitleRunnable = new GetProgramTitleRunnable(searchForm.table1,
                2, searchlist);
        Thread getProgramTitleThread = new Thread(getProgramTitleRunnable);
        getProgramTitleThread.start();

        searchForm.searchLabel.setText(searchlist.size() + " - " + tService.getString("pcs"));
        searchForm.table1.setRowHeight(30);
        searchForm.table1.getColumnModel().getColumn(0).setMinWidth(100);
        searchForm.table1.getColumnModel().getColumn(1).setMinWidth(200);
        searchForm.table1.getColumnModel().getColumn(2).setMinWidth(250);
        searchForm.setMinimumSize(new Dimension(680, searchForm.getHeight()));
        searchForm.pack();
    }

    private void buttonActionListener() {
        searchForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = searchForm.table1.getSelectedRow();
                channelService.openURL(searchlist.get(index).getUrl());
            }
        });
        searchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.table1.getSelectedRow();
                Channel channel = searchlist.get(selected);
                favoriteService.addToFavoriteList(channel);
                favoriteService.saveFavorites();
                searchForm.removeFromFavoritesButton.setVisible(true);
                searchForm.addToFavoritesButton.setVisible(false);
            }
        });
        searchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = searchForm.table1.getSelectedRow();
                Channel channel = searchlist.get(selected);
                int index = favoriteService.indexOfFavoriteByChannel(channel);
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
                String selectedName = searchlist.get(selected).getName();
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
                Channel channel = searchlist.get(index);
                if (index != -1) {
                    searchForm.openChannelButton.setVisible(true);
                    searchForm.guideButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByChannel(channel) == -1) {
                        searchForm.addToFavoritesButton.setVisible(true);
                        searchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        searchForm.addToFavoritesButton.setVisible(false);
                        searchForm.removeFromFavoritesButton.setVisible(true);
                    }
                    String title = guideService.getProgramTitle(channel.getName());
                    if ((title != null) && !title.isEmpty()) {
                        searchForm.guidePanel.setVisible(true);
                        searchForm.guideTextArea.setText(title);
                    } else {
                        searchForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(channel.getName());
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
