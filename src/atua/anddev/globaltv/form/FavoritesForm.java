package atua.anddev.globaltv.form;

import javax.swing.*;

public class FavoritesForm extends JFrame {
    public JList list1;
    public JButton openChannelButton;
    public JButton removeFromFavoritesButton;
    private JPanel favoritesPanel;
    public JLabel favoritesLabel;

    public FavoritesForm() {
        super("Favorites");
        setContentPane(favoritesPanel);
        pack();
        setVisible(true);
    }
}
