package atua.anddev.globaltv.form;

import javax.swing.*;

public class SearchForm extends JFrame {
    public JButton openChannelButton;
    public JButton addToFavoritesButton;
    public JButton removeFromFavoritesButton;
    private JPanel searchPanel;
    public JLabel searchLabel;
    public JTable table1;
    public JTextArea guideTextArea;
    public JScrollPane guidePanel;

    public SearchForm() {
        super("Search");
        setContentPane(searchPanel);
        pack();
        setVisible(true);
    }
}
