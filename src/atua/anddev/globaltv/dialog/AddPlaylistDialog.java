package atua.anddev.globaltv.dialog;

import atua.anddev.globaltv.PlaylistManagerActivity;
import atua.anddev.globaltv.Services;

import javax.swing.*;
import java.awt.event.*;

public class AddPlaylistDialog extends JDialog implements Services {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    public JLabel addPlaylistDialogLabel;

    public AddPlaylistDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        addPlaylistDialogLabel.setText(tService.getString("provider_list_is_empty"));
        buttonOK.setText(tService.getString("addAllOfferedPlaylist"));
        buttonCancel.setText(tService.getString("playlistsManagerButton"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        playlistService.addAllOfferedPlaylist();
        dispose();
    }

    private void onCancel() {
        new PlaylistManagerActivity();
        dispose();
    }

    public static void main(String[] args) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
