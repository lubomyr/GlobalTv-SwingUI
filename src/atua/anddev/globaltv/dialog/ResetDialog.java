package atua.anddev.globaltv.dialog;

import atua.anddev.globaltv.Services;

import javax.swing.*;
import java.awt.event.*;

public class ResetDialog extends JDialog implements Services {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel resetLabel;

    public ResetDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        resetLabel.setText(tService.getString("resetwarn"));
        buttonOK.setText(tService.getString("reset"));
        buttonCancel.setText(tService.getString("cancel"));

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
        favoriteService.clearAllFavorites();
        favoriteService.saveFavorites();
        playlistService.clearActivePlaylist();
        playlistService.saveData();
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ResetDialog dialog = new ResetDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
