package atua.anddev.globaltv.form;

import javax.swing.*;

public class PlaylistManagerForm extends JFrame {
    public JButton resetButton;
    public JButton addNewPlaylistButton;
    public JList list1;
    public JList list2;
    public JButton removeButton;
    public JButton addToSelectedPlaylistsButton;
    public JButton editButton;
    private JPanel playlistmanagerPanel;
    public JButton addAllOfferedPlaylistsButton;
    public JLabel selectedPlaylistLabel;
    public JLabel offeredPlaylistLabel;
    public JButton sortedListByUpdateButton;

    public PlaylistManagerForm() {
        super("Playlist Manager");
        setContentPane(playlistmanagerPanel);
        pack();
        setVisible(true);
    }
}
