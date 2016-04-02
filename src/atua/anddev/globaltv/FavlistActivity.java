package atua.anddev.globaltv;

import atua.anddev.globaltv.form.FavoritesForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FavlistActivity implements Services {
    private FavoritesForm favoritesForm;
    private DefaultListModel<String> model;

    public FavlistActivity() {
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
        model = new DefaultListModel<String>();
        for (String str : favoriteService.getFavoriteListForSelProv()) {
            model.addElement(str);
        }
        favoritesForm.list1.setModel(model);
        favoritesForm.favoritesLabel.setText(favoriteService.getFavoriteListForSelProv().size() + " - " + tService.local("pcs"));
        favoritesForm.pack();
    }

    private void actionSelector() {
        favoritesForm.openChannelButton.setVisible(false);
        favoritesForm.removeFromFavoritesButton.setVisible(false);
        favoritesForm.list1.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                int index = favoritesForm.list1.getSelectedIndex();
                if (index != -1) {
                    favoritesForm.openChannelButton.setVisible(true);
                    favoritesForm.removeFromFavoritesButton.setVisible(true);
                } else {
                    favoritesForm.openChannelButton.setVisible(false);
                    favoritesForm.removeFromFavoritesButton.setVisible(false);
                }
            }
        });
    }

    private void buttonActionListener() {
        favoritesForm.openChannelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.list1.getSelectedIndex();
                channelService.openChannel(favoriteService.getFavoriteListForSelProv().get(selected));
            }
        });
        favoritesForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = favoritesForm.list1.getSelectedIndex();
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
