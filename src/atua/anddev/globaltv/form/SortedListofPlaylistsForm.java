package atua.anddev.globaltv.form;

import javax.swing.*;

public class SortedListofPlaylistsForm extends JFrame {
    public JTable table1;
    private JPanel sortedListPanel;
    public JLabel sortedlistLabel;

    public SortedListofPlaylistsForm() {
        super("Sorted List of Playlists");
        setContentPane(sortedListPanel);
        setVisible(true);
    }
}
