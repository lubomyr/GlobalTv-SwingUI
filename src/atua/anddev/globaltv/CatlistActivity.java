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
        catlistForm.favoritesButton.setText(tService.getString("favorites"));
        catlistForm.searchButton.setText(tService.getString("search"));
        catlistForm.catlistInfoLabel.setText(tService.getString("selectCategory"));
    }

    private void createCatlist() {
        categoryList.add(tService.getString("all"));
        categoryList.addAll(channelService.getCategoriesList());
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
                new SearchDialog("getString");
            }
        });
    }

}
