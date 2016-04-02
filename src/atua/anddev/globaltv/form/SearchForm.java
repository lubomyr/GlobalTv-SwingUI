package atua.anddev.globaltv.form;

import javax.swing.*;

public class SearchForm extends JFrame {
    public JList list1;
    public JButton openChannelButton;
    public JButton addToFavoritesButton;
    public JButton removeFromFavoritesButton;
    private JPanel searchPanel;
    public JLabel searchLabel;

    public SearchForm() {
        super("Search");
        setContentPane(searchPanel);
        pack();
        setVisible(true);
    }
}
