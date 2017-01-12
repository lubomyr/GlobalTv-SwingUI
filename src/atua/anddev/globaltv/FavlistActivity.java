package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.FavoritesForm;
import atua.anddev.globaltv.models.TableModelWithIcons;
import atua.anddev.globaltv.runnables.GetIconRunnable;
import atua.anddev.globaltv.runnables.GetProgramTitleRunnable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

class FavlistActivity implements Services {
    private FavoritesForm favoritesForm;
    private DefaultListModel<String> model;
    private List<Channel> playlist;

    FavlistActivity() {
        favoritesForm = new FavoritesForm();
        playlist = favoriteService.getFavoriteListForSelProv();
        applyLocals();
        showFavlist();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        favoritesForm.openChannelButton.setText(tService.getString("openChannel"));
        favoritesForm.removeFromFavoritesButton.setText(tService.getString("removeFromFavorites"));
        favoritesForm.guideButton.setText(tService.getString("showProgramGuide"));
    }

    private void showFavlist() {
        String[] colNames;
        Object[][] data;
        int cols = 3;
        colNames = new String[]{"icon", "name", "program"};
        data = new Object[playlist.size()][cols];
        for (int row = 0; row < playlist.size(); row++) {
            data[row][1] = playlist.get(row).getName();
        }

        GetIconRunnable getIconRunnable = new GetIconRunnable(favoritesForm.table1, playlist);
        Thread getIconThread = new Thread(getIconRunnable);
        getIconThread.start();

        GetProgramTitleRunnable getProgramTitleRunnable = new GetProgramTitleRunnable(favoritesForm.table1,
                2, playlist);
        Thread getProgramTitleThread = new Thread(getProgramTitleRunnable);
        getProgramTitleThread.start();

        DefaultTableModel model = new TableModelWithIcons(data, colNames);
        favoritesForm.table1.setModel(model);
        favoritesForm.favoritesLabel.setText(favoriteService.getFavoriteListForSelProv().size() + " - " + tService.getString("pcs"));
        favoritesForm.table1.setRowHeight(30);
        favoritesForm.table1.getColumnModel().getColumn(0).setMinWidth(100);
        favoritesForm.table1.getColumnModel().getColumn(1).setMinWidth(200);
        favoritesForm.table1.getColumnModel().getColumn(2).setMinWidth(250);
        favoritesForm.setMinimumSize(new Dimension(680, favoritesForm.getHeight()));
        favoritesForm.pack();
    }

    private void actionSelector() {
        favoritesForm.openChannelButton.setVisible(false);
        favoritesForm.removeFromFavoritesButton.setVisible(false);
        favoritesForm.guidePanel.setVisible(false);
        favoritesForm.guideButton.setVisible(false);
        favoritesForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = favoritesForm.table1.getSelectedRow();
                if (index != -1) {
                    Channel channel = playlist.get(index);
                    favoritesForm.openChannelButton.setVisible(true);
                    favoritesForm.removeFromFavoritesButton.setVisible(true);
                    favoritesForm.guideButton.setVisible(true);

                    String title = guideService.getProgramTitle(channel.getName());
                    if ((title != null) && !title.isEmpty()) {
                        favoritesForm.guidePanel.setVisible(true);
                        favoritesForm.guideTextArea.setText(title);
                    } else {
                        favoritesForm.guidePanel.setVisible(false);
                    }
                    String desc = guideService.getProgramDesc(channel.getName());
                    if ((desc != null) && !desc.isEmpty()) {
                        favoritesForm.guideTextArea.append("\n" + desc);
                        favoritesForm.guideTextArea.setLineWrap(true);
                        favoritesForm.guideTextArea.setWrapStyleWord(true);
                    }
                    favoritesForm.pack();
                } else {
                    favoritesForm.openChannelButton.setVisible(false);
                    favoritesForm.removeFromFavoritesButton.setVisible(false);
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
        favoritesForm.openChannelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.table1.getSelectedRow();
                Channel channel = playlist.get(selected);
                channelService.openChannel(channel);
            }
        });
        favoritesForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.table1.getSelectedRow();
                Channel channel = playlist.get(selected);
                model.removeElementAt(selected);
                int index = favoriteService.indexOfFavoriteByChannel(channel);
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
            }
        });
        favoritesForm.guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.table1.getSelectedRow();
                Channel channel = playlist.get(selected);
                new GuideActivity(channel.getName());
            }
        });
    }

}
