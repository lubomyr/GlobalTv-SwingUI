package atua.anddev.globaltv.form;

import javax.swing.*;

public class GlobalFavoritesForm extends JFrame {

    private JPanel globalFavoritesPanel;
    public JButton openChannelButton;
    public JButton removeFromFavoritesButton;
    public JTable table1;
    public JLabel globalFavoritesLabel;
    public JTextArea guideTextArea;
    public JScrollPane guidePanel;

    public GlobalFavoritesForm() {
        super("Favorites");
        setContentPane(globalFavoritesPanel);
        pack();
        setVisible(true);
    }
}
