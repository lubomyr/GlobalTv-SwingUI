package atua.anddev.globaltv.form;

import javax.swing.*;

/**
 * Created by lubomyr on 29.03.16.
 */
public class PlaylistForm extends JFrame {
    public JButton favoritesButton;
    public JButton searchButton;
    public JList list1;
    private JPanel playlistPanel;
    public JButton openChannelButton;
    public JButton addToFavoritesButton;
    public JButton removeFromFavoritesButton;
    public JLabel playlistInfoLabel;

    public PlaylistForm() {
        super("Channel list");
        setContentPane(playlistPanel);
        pack();
        setVisible(true);
    }
}
