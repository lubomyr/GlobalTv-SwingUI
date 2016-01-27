import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PlaylistEditActivity extends PlaylistManagerActivity {
    private static String mode;
    private static int num=0;

    PlaylistEditActivity(String mode) {
        this.mode = mode;
        PlaylistEditGui.SetPlaylistEditActivity();
        applyLocals();
        viewButtons();
    }

    PlaylistEditActivity(String mode, int num) {
        this.mode = mode;
        this.num = num;
        PlaylistEditGui.SetPlaylistEditActivity();
        applyLocals();
        viewButtons();
    }

    public static void applyLocals() {
        PlaylistEditGui.lblNewLabel.setText(local.$name);
        PlaylistEditGui.lblNewLabel_1.setText(local.$path);
        PlaylistEditGui.rdbtnNewRadioButton.setText(local.$standardPlaylist);
        PlaylistEditGui.rdbtnNewRadioButton_1.setText(local.$torrenttvPlaylist);
        PlaylistEditGui.btnNewButton.setText(local.$delete);
        PlaylistEditGui.btnNewButton_1.setText(local.$modify);
        PlaylistEditGui.btnNewButton_2.setText(local.$add);
        PlaylistEditGui.btnNewButton_3.setText(local.$cancel);
    }

    public static void viewButtons() {
        if (mode == "add") {
            PlaylistEditGui.btnNewButton_1.setVisible(false);
            PlaylistEditGui.btnNewButton_2.setVisible(true);
        } else {
            PlaylistEditGui.btnNewButton_1.setVisible(true);
            PlaylistEditGui.btnNewButton_2.setVisible(false);
            PlaylistEditGui.textField.setText(ActivePlaylist.getName(num));
            PlaylistEditGui.textField_1.setText(ActivePlaylist.getUrl(num));
            if (ActivePlaylist.getType(num)==0)
                PlaylistEditGui.rdbtnNewRadioButton.setSelected(true);
            else
                PlaylistEditGui.rdbtnNewRadioButton_1.setSelected(true);
        }
    }

    static class PlaylistEditGui extends MyGui {
        static JLabel lblNewLabel;
        static JTextField textField;
        static JLabel lblNewLabel_1;
        static JTextField textField_1;
        static JRadioButton rdbtnNewRadioButton;
        static JRadioButton rdbtnNewRadioButton_1;
        static JButton btnNewButton;
        static JButton btnNewButton_1;
        static JButton btnNewButton_2;
        static JButton btnNewButton_3;

        public static void SetPlaylistEditActivity() {
            MyGui.playlistManagerFrame.setVisible(false);
            MyGui.playlistEditFrame.setVisible(true);
        }

        protected static void initialize() {
            playlistEditFrame = new JFrame();
            playlistEditFrame.setBounds(posx, posy, 450, 460);
            playlistEditFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            playlistEditFrame.getContentPane().setLayout(null);

            lblNewLabel = new JLabel("Name");
            lblNewLabel.setBounds(10, 30, 46, 14);
            playlistEditFrame.getContentPane().add(lblNewLabel);

            textField = new JTextField();
            textField.setBounds(10, 43, 414, 20);
            playlistEditFrame.getContentPane().add(textField);
            textField.setColumns(10);

            lblNewLabel_1 = new JLabel("Path");
            lblNewLabel_1.setBounds(10, 92, 46, 14);
            playlistEditFrame.getContentPane().add(lblNewLabel_1);

            textField_1 = new JTextField();
            textField_1.setBounds(10, 106, 414, 20);
            playlistEditFrame.getContentPane().add(textField_1);
            textField_1.setColumns(10);

            rdbtnNewRadioButton = new JRadioButton("Standard playlist");
            rdbtnNewRadioButton.setBounds(84, 161, 125, 23);
            playlistEditFrame.getContentPane().add(rdbtnNewRadioButton);
            rdbtnNewRadioButton.setSelected(true);

            rdbtnNewRadioButton_1 = new JRadioButton("TorrentTv playlist");
            rdbtnNewRadioButton_1.setBounds(205, 161, 125, 23);
            playlistEditFrame.getContentPane().add(rdbtnNewRadioButton_1);

            ButtonGroup group = new ButtonGroup();
            group.add(rdbtnNewRadioButton);
            group.add(rdbtnNewRadioButton_1);

            btnNewButton = new JButton("Delete");
            btnNewButton.setBounds(31, 216, 115, 35);
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    ActivePlaylist.remove(num);
                    saveData();
                    playeditact = null;
                    PlaylistManagerActivity.PlaylistManagerGui.SetPlaylistManagerActivity();
                }
            });
            playlistEditFrame.getContentPane().add(btnNewButton);

            btnNewButton_1 = new JButton("Modify");
            btnNewButton_1.setBounds(278, 216, 115, 35);
            btnNewButton_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    String name, url, file;
                    int type;
                    name = textField.getText();
                    url = textField_1.getText();
                    file = getFileName(name);
                    if (rdbtnNewRadioButton.isSelected())
                        type = 0;
                    else
                        type = 1;
                    ActivePlaylist.set(num, name, url, file, type);
                    saveData();
                    playeditact = null;
                    PlaylistManagerActivity.PlaylistManagerGui.SetPlaylistManagerActivity();
                }
            });
            playlistEditFrame.getContentPane().add(btnNewButton_1);

            btnNewButton_2 = new JButton("Add");
            btnNewButton_2.setBounds(153, 216, 115, 35);
            btnNewButton_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    String name, url, file;
                    int type;
                    name = textField.getText();
                    url = textField_1.getText();
                    file = getFileName(name);
                    if (rdbtnNewRadioButton.isSelected())
                        type = 0;
                    else
                        type = 1;
                    ActivePlaylist.add(name, url, file, type);
                    saveData();
                    playeditact = null;
                    PlaylistManagerActivity.PlaylistManagerGui.SetPlaylistManagerActivity();
                }
            });
            playlistEditFrame.getContentPane().add(btnNewButton_2);

            btnNewButton_3 = new JButton("Cancel");
            btnNewButton_3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    playeditact = null;
                    PlaylistManagerActivity.PlaylistManagerGui.SetPlaylistManagerActivity();
                }
            });
            btnNewButton_3.setBounds(159, 287, 109, 35);
            playlistEditFrame.getContentPane().add(btnNewButton_3);
        }
    }
}