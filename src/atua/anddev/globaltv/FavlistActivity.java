package atua.anddev.globaltv;

import atua.anddev.globaltv.form.FavoritesForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

class FavlistActivity implements Services {
    private FavoritesForm favoritesForm;
    private DefaultListModel<String> model;

    FavlistActivity() {
        favoritesForm = new FavoritesForm();
        applyLocals();
        showFavlist();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        favoritesForm.openChannelButton.setText(tService.local("openChannel"));
        favoritesForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void showFavlist() {
        List<String> playlist = favoriteService.getFavoriteListForSelProv();
        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "program"};
        data = new Object[playlist.size()][cols];
        for (int row = 0; row < playlist.size(); row++) {
            data[row][0] = playlist.get(row);
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        favoritesForm.table1.setModel(model);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < playlist.size(); row++) {
                    favoritesForm.table1.setValueAt(guideService.getProgramTitle(playlist.get(row)), row , 1);
                }
            }
        }).start();
        favoritesForm.favoritesLabel.setText(favoriteService.getFavoriteListForSelProv().size() + " - " + tService.local("pcs"));
        favoritesForm.pack();
    }

    private void actionSelector() {
        favoritesForm.openChannelButton.setVisible(false);
        favoritesForm.removeFromFavoritesButton.setVisible(false);
        favoritesForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = favoritesForm.table1.getSelectedRow();
                if (index != -1) {
                    favoritesForm.openChannelButton.setVisible(true);
                    favoritesForm.removeFromFavoritesButton.setVisible(true);
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
                channelService.openChannel(favoriteService.getFavoriteListForSelProv().get(selected));
            }
        });
        favoritesForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.table1.getSelectedRow();
                String selectedName = favoriteService.getFavoriteListForSelProv().get(selected);
                String selectedProv = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
                model.removeElementAt(selected);
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv);
                favoriteService.deleteFromFavoritesById(index);
                favoriteService.saveFavorites();
            }
        });
    }

}
