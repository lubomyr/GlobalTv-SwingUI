package atua.anddev.globaltv;

import atua.anddev.globaltv.form.GlobalSearchForm;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class GlobalSearchActivity implements Services {
    private GlobalSearchForm globalSearchForm;
    private String searchString;

    public GlobalSearchActivity(String search) {
        this.searchString = search;
        globalSearchForm = new GlobalSearchForm();
        applyLocals();
        showSearchResults();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        globalSearchForm.openChannelButton.setText(tService.local("openChannel"));
        globalSearchForm.addToFavoritesButton.setText(tService.local("addToFavorites"));
        globalSearchForm.removeFromFavoritesButton.setText(tService.local("removeFromFavorites"));
    }

    private void showSearchResults() {
        final List<String> globalSearchName = searchService.searchChannelsByName(searchString);
        final List<String> globalSearchProv = searchService.searchChannelsProvByName(searchString);

        String[] colNames;
        Object[][] data;
        int cols = 2;
        colNames = new String[]{"name", "playlist"};
        data = new Object[globalSearchName.size()][cols];
        for (int row = 0; row < globalSearchName.size(); row++) {
            data[row][0] = globalSearchName.get(row);
            data[row][1] = globalSearchProv.get(row);
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        globalSearchForm.table1.setModel(model);
        globalSearchForm.globalSearchLabel.setText(globalSearchName.size() + " - " + tService.local("pcs"));
        globalSearchForm.pack();
    }

    private void actionSelector() {
        globalSearchForm.openChannelButton.setVisible(false);
        globalSearchForm.removeFromFavoritesButton.setVisible(false);
        globalSearchForm.addToFavoritesButton.setVisible(false);
        globalSearchForm.table1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.searchChannelsByName(searchString).get(selected);
                String selectedProv = searchService.searchChannelsProvByName(searchString).get(selected);
                if (selected != -1) {
                    globalSearchForm.openChannelButton.setVisible(true);
                    if (favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv) == -1) {
                        globalSearchForm.addToFavoritesButton.setVisible(true);
                        globalSearchForm.removeFromFavoritesButton.setVisible(false);
                    } else {
                        globalSearchForm.addToFavoritesButton.setVisible(false);
                        globalSearchForm.removeFromFavoritesButton.setVisible(true);
                    }
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
                channelService.openURL(searchService.searchChannelsUrlByName(searchString).get(selected));
            }
        });
        globalSearchForm.addToFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.searchChannelsByName(searchString).get(selected);
                String selectedProv = searchService.searchChannelsProvByName(searchString).get(selected);
                favoriteService.addToFavoriteList(selectedName, selectedProv);
                globalSearchForm.removeFromFavoritesButton.setVisible(true);
                globalSearchForm.addToFavoritesButton.setVisible(false);
            }
        });
        globalSearchForm.removeFromFavoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = globalSearchForm.table1.getSelectedRow();
                String selectedName = searchService.searchChannelsByName(searchString).get(selected);
                String selectedProv = searchService.searchChannelsProvByName(searchString).get(selected);
                int index = favoriteService.indexOfFavoriteByNameAndProv(selectedName, selectedProv);
                favoriteService.deleteFromFavoritesById(index);
                globalSearchForm.removeFromFavoritesButton.setVisible(false);
                globalSearchForm.addToFavoritesButton.setVisible(true);
            }
        });
    }

}
