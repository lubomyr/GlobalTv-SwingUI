package atua.anddev.globaltv.form;

import javax.swing.*;

public class PlaylistEditForm extends JFrame {
    public JTextField textField1;
    public JTextField textField2;
    public JRadioButton standartRadioButton;
    public JRadioButton torrentTvRadioButton;
    public JButton editButton;
    private JPanel playlistEditPanel;

    public PlaylistEditForm() {
        super("Playlist Add/Edit");
        setContentPane(playlistEditPanel);
        pack();
        setVisible(true);
    }
}
