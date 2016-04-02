package atua.anddev.globaltv.form;

import javax.swing.*;

/**
 * Created by lubomyr on 29.03.16.
 */
public class MainForm extends JFrame{
    public JComboBox comboBox1;
    public JButton openPlaylistButton;
    public JButton updatePlaylistButton;
    public JButton updateOutdatedPlaylistButton;
    public JButton favoritesButton;
    public JButton searchButton;
    public JButton playlistManagerButton;
    public JButton settingsButton;
    private JPanel mainPanel;
    public JComboBox comboBox2;
    public JLabel mainPlaylistInfoLabel;
    public JLabel mainWarningLabel;
    public JLabel playlistLabel;

    public MainForm() {
        super("Global Tv");
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


}