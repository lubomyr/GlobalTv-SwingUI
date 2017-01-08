package atua.anddev.globaltv.form;

import javax.swing.*;

/**
 * Created by lubomyr on 29.03.16.
 */
public class ChannelListForm extends JFrame {
    public JButton favoritesButton;
    public JButton searchButton;
    private JPanel playlistPanel;
    public JButton openChannelButton;
    public JButton addToFavoritesButton;
    public JButton removeFromFavoritesButton;
    public JLabel playlistInfoLabel;
    public JTable table1;
    public JTextArea guideTextArea;
    public JScrollPane guidePanel;
    public JButton guideButton;

    public ChannelListForm() {
        super("Channel list");
        setContentPane(playlistPanel);
        pack();
        setVisible(true);
    }
}
