package atua.anddev.globaltv.form;

import javax.swing.*;

public class FavoritesForm extends JFrame {
    public JButton openChannelButton;
    public JButton removeFromFavoritesButton;
    private JPanel favoritesPanel;
    public JLabel favoritesLabel;
    public JTable table1;
    public JTextArea guideTextArea;
    public JScrollPane guidePanel;

    public FavoritesForm() {
        super("Favorites");
        setContentPane(favoritesPanel);
        pack();
        setVisible(true);
    }
}
