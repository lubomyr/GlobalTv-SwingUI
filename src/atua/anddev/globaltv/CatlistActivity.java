package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.SearchDialog;
import atua.anddev.globaltv.form.CategoryForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CatlistActivity implements Services {
    CategoryForm catlistForm;
    private List<String> categoryList = new ArrayList<String>();

    public CatlistActivity() {
        catlistForm = new CategoryForm();
        applyLocals();
        createCatlist();
        showCatlist();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        catlistForm.favoritesButton.setText(tService.local("favorites"));
        catlistForm.searchButton.setText(tService.local("search"));
        catlistForm.catlistInfoLabel.setText(tService.local("selectCategory"));
    }

    private void createCatlist() {
        String pname = playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName();
        categoryList.add(tService.local("all"));
        categoryList.addAll(channelService.getCategoriesList(pname));
    }

    private void showCatlist() {
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (String str : categoryList) {
            model.addElement(str);
        }
        catlistForm.list1.setModel(model);
        catlistForm.pack();
    }

    private void actionSelector() {
        catlistForm.list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int index = catlistForm.list1.getSelectedIndex();
                if (!arg0.getValueIsAdjusting() && index != -1) {
                    Global.selectedCategory = categoryList.get(index);
                    new PlaylistActivity();
                }
            }
        });
    }

    private void buttonActionListener() {
        catlistForm.favoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FavlistActivity();
            }
        });
        catlistForm.searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchDialog("local");
            }
        });
    }

}
