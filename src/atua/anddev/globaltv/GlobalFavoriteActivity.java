package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.WarningDialog;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.form.GlobalFavoritesForm;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GlobalFavoriteActivity implements Services {
    private GlobalFavoritesForm globalFavoritesForm;
    private int selected;
    private DefaultTableModel model;

    public GlobalFavoriteActivity() {
        globalFavoritesForm = new GlobalFavoritesForm();
        applyLocals();
        showFavorites();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        globalFavoritesForm.openChannelButton.setText(tService.local("openChannel"));
        globalFavoritesForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void showFavorites() {
        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "playlist"};
        data = new Object[favoriteService.sizeOfFavoriteList()][cols];
        for (int row = 0; row < favoriteService.sizeOfFavoriteList(); row++) {
            data[row][0] = favoriteService.getFavoriteById(row).getName();
            data[row][1] = favoriteService.getFavoriteById(row).getProv();
        }
        model = new DefaultTableModel(data, colNames);
        globalFavoritesForm.table1.setModel(model);
        globalFavoritesForm.globalFavoritesLabel.setText(favoriteService.sizeOfFavoriteList() + " - " + tService.local("pcs"));
        globalFavoritesForm.pack();
    }

    private void actionSelector() {
        globalFavoritesForm.openChannelButton.setVisible(false);
        globalFavoritesForm.removeFromFavoritesButton.setVisible(false);
        globalFavoritesForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selected = globalFavoritesForm.table1.getSelectedRow();
                if (selected != -1) {
                    globalFavoritesForm.openChannelButton.setVisible(true);
                    globalFavoritesForm.removeFromFavoritesButton.setVisible(true);
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
                selected = globalFavoritesForm.table1.getSelectedRow();
                openFavorite(selected);
            }
        });
        globalFavoritesForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selected = globalFavoritesForm.table1.getSelectedRow();
                model.removeRow(selected);
                favoriteService.deleteFromFavoritesById(selected);
                favoriteService.saveFavorites();
            }
        });
    }

    private void openFavorite(int itemNum) {
        String getProvName = favoriteService.getFavoriteById(itemNum).getProv();
        int numA = playlistService.indexNameForActivePlaylist(getProvName);
        if (numA == -1) {
            new WarningDialog(tService.local("playlistnotexist"));
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