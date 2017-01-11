package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.WarningDialog;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.GlobalFavoritesForm;
import atua.anddev.globaltv.models.TableModelWithIcons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GlobalFavoriteActivity implements Services {
    private GlobalFavoritesForm globalFavoritesForm;
    private DefaultTableModel model;

    GlobalFavoriteActivity() {
        globalFavoritesForm = new GlobalFavoritesForm();
        applyLocals();
        showFavorites();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        globalFavoritesForm.openChannelButton.setText(tService.getString("openChannel"));
        globalFavoritesForm.removeFromFavoritesButton.setText(tService.getString("removeFromFavorites"));
        globalFavoritesForm.guideButton.setText(tService.getString("showProgramGuide"));
    }

    private void showFavorites() {
        String[] colNames;
        Object[][] data;
        int cols = 4;
        colNames = new String[]{"icon", "name", "playlist", "program"};
        data = new Object[favoriteService.sizeOfFavoriteList()][cols];
        for (int row = 0; row < favoriteService.sizeOfFavoriteList(); row++) {
            data[row][1] = favoriteService.getFavoriteById(row).getName();
            data[row][2] = favoriteService.getFavoriteById(row).getProv();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < favoriteService.sizeOfFavoriteList(); row++) {
                    ImageIcon imageIcon = logoService.getIcon(favoriteService.getFavoriteById(row).getName());
                    if (imageIcon == null)
                        imageIcon = new ImageIcon("");
                    globalFavoritesForm.table1.setValueAt(imageIcon, row, 0);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < favoriteService.sizeOfFavoriteList(); row++) {
                    globalFavoritesForm.table1.setValueAt(guideService.getProgramTitle(favoriteService.getFavoriteById(row).getName()),
                            row, 3);
                }
            }
        }).start();
        model = new TableModelWithIcons(data, colNames);
        globalFavoritesForm.table1.setModel(model);
        globalFavoritesForm.globalFavoritesLabel.setText(favoriteService.sizeOfFavoriteList() + " - " + tService.getString("pcs"));
        globalFavoritesForm.table1.setRowHeight(30);
        globalFavoritesForm.table1.getColumnModel().getColumn(0).setMinWidth(100);
        globalFavoritesForm.table1.getColumnModel().getColumn(1).setMinWidth(200);
        globalFavoritesForm.table1.getColumnModel().getColumn(2).setMinWidth(220);
        globalFavoritesForm.table1.getColumnModel().getColumn(3).setMinWidth(250);
        globalFavoritesForm.setMinimumSize(new Dimension(770, globalFavoritesForm.getHeight()));
        globalFavoritesForm.pack();
    }

    private void actionSelector() {
        globalFavoritesForm.openChannelButton.setVisible(false);
        globalFavoritesForm.removeFromFavoritesButton.setVisible(false);
        globalFavoritesForm.guidePanel.setVisible(false);
        globalFavoritesForm.guideButton.setVisible(false);
        globalFavoritesForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = globalFavoritesForm.table1.getSelectedRow();
                if (selected != -1) {
                    globalFavoritesForm.openChannelButton.setVisible(true);
                    globalFavoritesForm.removeFromFavoritesButton.setVisible(true);
                    globalFavoritesForm.guideButton.setVisible(true);

                    String selectedChannel = globalFavoritesForm.table1.getValueAt(selected,0).toString();
                    String title = guideService.getProgramTitle(selectedChannel);
                    if ((title != null) && !title.isEmpty()) {
                        globalFavoritesForm.guidePanel.setVisible(true);
                        globalFavoritesForm.guideTextArea.setText(title);
                    } else {
                        globalFavoritesForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(selectedChannel);
                    if ((desc != null) && !desc.isEmpty()) {
                        globalFavoritesForm.guideTextArea.append("\n" + desc);
                        globalFavoritesForm.guideTextArea.setLineWrap(true);
                        globalFavoritesForm.guideTextArea.setWrapStyleWord(true);
                    }
                    globalFavoritesForm.pack();
                } else {
                    globalFavoritesForm.openChannelButton.setVisible(false);
                    globalFavoritesForm.removeFromFavoritesButton.setVisible(false);
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
        globalFavoritesForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalFavoritesForm.table1.getSelectedRow();
                openFavorite(selected);
            }
        });
        globalFavoritesForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalFavoritesForm.table1.getSelectedRow();
                model.removeRow(selected);
                favoriteService.deleteFromFavoritesById(selected);
                favoriteService.saveFavorites();
            }
        });
        globalFavoritesForm.guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = globalFavoritesForm.table1.getSelectedRow();
                String selectedName = favoriteService.getFavoriteById(selected).getName();
                new GuideActivity(selectedName);
            }
        });
    }

    private void openFavorite(int itemNum) {
        String getProvName = favoriteService.getFavoriteById(itemNum).getProv();
        int numA = playlistService.indexNameForActivePlaylist(getProvName);
        if (numA == -1) {
            new WarningDialog(tService.getString("playlistnotexist"));
            return;
        }
        playlistService.readPlaylist(numA);
        for (Channel chn : channelService.getAllChannels()) {
            if (chn.getName().equals(favoriteService.getFavoriteById(itemNum).getName())) {
                channelService.openURL(chn.getUrl());
                break;
            }
        }
    }
}
