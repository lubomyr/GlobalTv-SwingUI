import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CatlistActivity extends MainActivity {
    CatlistActivity() {
        CatlistGui.SetCatlistActivity();
        applyCatlistLocals();
        createCatlist();
        showCatlist();
    }

    public static void applyCatlistLocals() {
        CatlistGui.CategoryLabel.setText(local.$selectCategory);
        CatlistGui.CatBackButton.setText(local.$back);
        CatlistGui.CatSearchButton.setText(local.$search);
        CatlistGui.CatFavoriteButton.setText(local.$favorites);
    }

    public void createCatlist() {
        boolean cat_exist = false;

        categoryList.clear();

        categoryList.add(local.$all);

        for (int i = 0; i < channel.size() - 1; i++) {
            cat_exist = false;
            for (int j = 0; j <= categoryList.size() - 1; j++)
                if (channel.getCategory(i).equalsIgnoreCase(categoryList.get(j)))
                    cat_exist = true;
            if (cat_exist == false && !channel.getCategory(i).equals(""))
                categoryList.add(channel.getCategory(i));
        }
    }

    public void showCatlist() {
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (int i = 0; i < categoryList.size(); i++) {
            model.addElement(categoryList.get(i));
        }
        CatlistGui.catList.setModel(model);
    }

    static class CatlistGui extends MyGui {
        static JButton CatFavoriteButton;
        static JButton CatBackButton;
        static JList<String> catList;
        static JScrollPane catScrollBar;
        static JButton CatSearchButton;
        static JLabel CategoryLabel;

        public static void SetCatlistActivity() {
            MyGui.mainFrame.setVisible(false);
            MyGui.catlistFrame.setVisible(true);
            MyGui.playlistFrame.setVisible(false);
            MyGui.globalSearchFrame.setVisible(false);
            MyGui.globalFavoritesFrame.setVisible(false);
            //System.out.println("posx="+pos.x+" posy="+pos.y);
            //window.catlistFrame.setLocation(posx, posy);
        }

        protected static void initialize() {
            catlistFrame = new JFrame();
            catlistFrame.setBounds(posx, posy, 450, 400);
            catlistFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            catlistFrame.getContentPane().setLayout(null);

            CatFavoriteButton = new JButton("Favorites");
            CatFavoriteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    FavlistActivity.FavlistGui.SetFavlistActivity();
                    favoritesact = new FavlistActivity();
                }
            });
            CatFavoriteButton.setBounds(38, 10, 106, 50);
            catlistFrame.getContentPane().add(CatFavoriteButton);

            CatBackButton = new JButton("Back");
            CatBackButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    catlistact = null;
                    MainActivity.MainGui.SetMainActivity();
                }
            });
            CatBackButton.setBounds(282, 10, 114, 50);
            catlistFrame.getContentPane().add(CatBackButton);

            catList = new JList<String>();
            catList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    int index = catList.getSelectedIndex();
                    if (index != -1) {
                        selectedCategory = categoryList.get(index);
                        playlistact = new PlaylistActivity();
                    }
                }
            });
            catList.setBounds(38, 103, 340, 237);

            catScrollBar = new JScrollPane(catList);
            catScrollBar.setBounds(38, 103, 340, 237);
            catlistFrame.getContentPane().add(catScrollBar, BorderLayout.CENTER);
            catScrollBar.setViewportView(catList);

            CatSearchButton = new JButton("Search");
            CatSearchButton.setBounds(154, 10, 118, 50);
            catlistFrame.getContentPane().add(CatSearchButton);

            CategoryLabel = new JLabel("Select Category");
            CategoryLabel.setBounds(154, 69, 118, 23);
            catlistFrame.getContentPane().add(CategoryLabel);
        }
    }

}
