package atua.anddev.globaltv.form;

import javax.swing.*;

public class GlobalSearchForm extends JFrame {
    public JTable table1;
    public JButton openChannelButton;
    public JButton addToFavoritesButton;
    public JButton removeFromFavoritesButton;
    private JPanel globalSearchPanel;
    public JLabel globalSearchLabel;
    public JTextArea guideTextArea;
    public JScrollPane guidePanel;
    public JButton guideButton;

    public GlobalSearchForm() {
        super("Search");
        setContentPane(globalSearchPanel);
        pack();
        setVisible(true);
    }
}
