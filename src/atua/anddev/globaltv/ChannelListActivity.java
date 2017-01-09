package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.SearchDialog;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Playlist;
import atua.anddev.globaltv.form.ChannelListForm;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ChannelListActivity implements Services {
    private ChannelListForm channelListForm;
    private List<Channel> channellist;
    private String selectedChannel;
    private String selectedLink;
    private Playlist selectedPlaylist;

    ChannelListActivity() {
        channelListForm = new ChannelListForm();
        channellist = new ArrayList<>();
        applyLocals();
        openCategory(Global.selectedCategory);
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        channelListForm.favoritesButton.setText(tService.getString("favorites"));
        channelListForm.searchButton.setText(tService.getString("search"));
        channelListForm.openChannelButton.setText(tService.getString("openChannel"));
        channelListForm.addToFavoritesButton.setText(tService.getString("addToFavorites"));
        channelListForm.removeFromFavoritesButton.setText(tService.getString("removeFromFavorites"));
        channelListForm.guideButton.setText(tService.getString("showProgramGuide"));
    }

    private void openCategory(final String catName) {
        for (Channel chn : channelService.getAllChannels()) {
            if (catName.equals(tService.getString("all"))) {
                channellist.add(chn);
            } else if (catName.equals(chn.getCategory())) {
                channellist.add(chn);
            }
        }

        channelListForm.playlistInfoLabel.setText(channellist.size() + " - " + tService.getString("channels"));
        String[] colNames;
        Object[][] data;
        int cols = 3;
        colNames = new String[]{"icon", "name", "program"};
        data = new Object[channellist.size()][cols];
        for (int row = 0; row < channellist.size(); row++) {
            Channel channel = channellist.get(row);
            data[row][1] = channel.getName();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < channellist.size(); row++) {
                    Channel channel = channellist.get(row);
                    ImageIcon imageIcon = getIcon(channel.getName());
                    if (imageIcon == null)
                        imageIcon = new ImageIcon("");
                    channelListForm.table1.setValueAt(imageIcon, row, 0);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < channellist.size(); row++) {
                    if (Global.guideLoaded)
                        channelListForm.table1.setValueAt(guideService.getProgramTitle(channellist.get(row).getName()),
                                row, 2);
                }
            }
        }).start();
        DefaultTableModel model = new DefaultTableModel(data, colNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() > 0) {
                    Object value = getValueAt(0, column);
                    if (value != null) {
                        return getValueAt(0, column).getClass();
                    }
                }

                return super.getColumnClass(column);
            }
        };
        channelListForm.table1.setModel(model);
        channelListForm.table1.setRowHeight(30);
        channelListForm.table1.getColumnModel().getColumn(0).setMinWidth(100);
        channelListForm.table1.getColumnModel().getColumn(1).setMinWidth(200);
        channelListForm.table1.getColumnModel().getColumn(2).setMinWidth(250);
        channelListForm.setMinimumSize(new Dimension(680, channelListForm.getHeight()));
        channelListForm.pack();
    }

    private ImageIcon getIcon(String name) {
        ImageIcon imageIcon = null;
        try {
            String urlPath = logoService.getLogoByName(name);
            if (urlPath != null) {
                URL url = new URL(urlPath);
                BufferedImage image = ImageIO.read(url);
                imageIcon = new ImageIcon(image);
                //if (imageIcon.getIconHeight() > 25)
                imageIcon = logoService.scaleImage(imageIcon, 100,
                        25);
            }
        } catch (IOException ignored) {
        }
        return imageIcon;
    }

    private void actionSelector() {
        channelListForm.openChannelButton.setVisible(false);
        channelListForm.addToFavoritesButton.setVisible(false);
        channelListForm.removeFromFavoritesButton.setVisible(false);
        channelListForm.guideButton.setVisible(false);
        channelListForm.guidePanel.setVisible(false);
        channelListForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = channelListForm.table1.getSelectedRow();
                if (index != -1) {
                    selectedChannel = channellist.get(index).getName();
                    selectedLink = channellist.get(index).getUrl();
                    selectedPlaylist = playlistService.getActivePlaylistById(MainActivity.selectedProvider);
                    channelListForm.openChannelButton.setVisible(true);
                    channelListForm.guideButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedChannel, selectedPlaylist.getName()) == -1) {
                        channelListForm.addToFavoritesButton.setVisible(true);
                        channelListForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        channelListForm.removeFromFavoritesButton.setVisible(true);
                        channelListForm.addToFavoritesButton.setVisible(false);
                    }

                    String title = guideService.getProgramTitle(selectedChannel);
                    if ((title != null) && !title.isEmpty()) {
                        channelListForm.guidePanel.setVisible(true);
                        channelListForm.guideTextArea.setText(title);
                    } else {
                        channelListForm.guidePanel.setVisible(false);
                    }

                    String desc = guideService.getProgramDesc(selectedChannel);
                    if ((desc != null) && !desc.isEmpty()) {
                        channelListForm.guideTextArea.append("\n" + desc);
                        channelListForm.guideTextArea.setLineWrap(true);
                        channelListForm.guideTextArea.setWrapStyleWord(true);
                    }
                    channelListForm.pack();
                } else {
                    channelListForm.openChannelButton.setVisible(false);
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
        channelListForm.openChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                channelService.openURL(selectedLink);
            }
        });
        channelListForm.favoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FavlistActivity();
            }
        });
        channelListForm.searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchDialog("getString");
            }
        });
        channelListForm.addToFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                favoriteService.addToFavoriteList(selectedChannel, selectedPlaylist.getName());
                favoriteService.saveFavorites();
                channelListForm.removeFromFavoritesButton.setVisible(true);
                channelListForm.addToFavoritesButton.setVisible(false);
            }
        });
        channelListForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedChannel, selectedPlaylist.getName());
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
                channelListForm.removeFromFavoritesButton.setVisible(false);
                channelListForm.addToFavoritesButton.setVisible(true);
            }
        });
        channelListForm.guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GuideActivity(selectedChannel);
            }
        });
    }

}
