package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.ResetDialog;
import atua.anddev.globaltv.entity.Playlist;
import atua.anddev.globaltv.form.PlaylistManagerForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaylistManagerActivity implements Services {
    static DefaultListModel<String> model_a;
    private DefaultListModel<String> model_d;
    private PlaylistManagerForm playlistManagerForm;


    public PlaylistManagerActivity() {
        playlistManagerForm = new PlaylistManagerForm();
        applyLocals();
        createProvlist();
        showProvlist();
        actionSelector();
        buttonActionListener();
    }

    private void applyLocals() {
        playlistManagerForm.resetButton.setText(tService.getString("reset"));
        playlistManagerForm.addToSelectedPlaylistsButton.setText(tService.getString("addToSelectedPlaylists"));
        playlistManagerForm.editButton.setText(tService.getString("modify"));
        playlistManagerForm.removeButton.setText(tService.getString("remove"));
        playlistManagerForm.addNewPlaylistButton.setText(tService.getString("addnewplaylist"));
        playlistManagerForm.addAllOfferedPlaylistsButton.setText(tService.getString("addAllOfferedPlaylist"));
        playlistManagerForm.sortedListByUpdateButton.setText(tService.getString("sortedListButton"));
    }

    private void createProvlist() {
        model_a = new DefaultListModel<String>();
        for (Playlist plst : playlistService.getAllActivePlaylist()) {
            model_a.addElement(plst.getName());
        }
        model_d = new DefaultListModel<String>();
        for (Playlist plst : playlistService.getAllOfferedPlaylist()) {
            model_d.addElement(plst.getName());
        }
    }

    private void showProvlist() {
        playlistManagerForm.list1.setModel(model_a);
        playlistManagerForm.list2.setModel(model_d);
        playlistManagerForm.selectedPlaylistLabel.setText(tService.getString("selected") + " - " + playlistService.sizeOfActivePlaylist()
                + " " + tService.getString("pcs"));
        playlistManagerForm.offeredPlaylistLabel.setText(tService.getString("offered") + " - " + playlistService.sizeOfOfferedPlaylist()
                + " " + tService.getString("pcs"));
        playlistManagerForm.pack();
    }

    private void actionSelector() {
        playlistManagerForm.addToSelectedPlaylistsButton.setVisible(false);
        playlistManagerForm.removeButton.setVisible(false);
        playlistManagerForm.editButton.setVisible(false);
        playlistManagerForm.list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = playlistManagerForm.list1.getSelectedIndex();
                if (index != -1) {
                    playlistManagerForm.removeButton.setVisible(true);
                    playlistManagerForm.editButton.setVisible(true);
                } else {
                    playlistManagerForm.removeButton.setVisible(false);
                    playlistManagerForm.editButton.setVisible(false);
                }
            }
        });
        playlistManagerForm.list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = playlistManagerForm.list2.getSelectedIndex();
                if (index != -1) {
                    playlistManagerForm.addToSelectedPlaylistsButton.setVisible(true);
                } else {
                    playlistManagerForm.addToSelectedPlaylistsButton.setVisible(false);
                }
            }
        });
    }

    private void buttonActionListener() {
        playlistManagerForm.addToSelectedPlaylistsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = playlistManagerForm.list2.getSelectedIndex();
                playlistService.addNewActivePlaylist(playlistService.getOfferedPlaylistById(index));
                model_a.addElement(playlistService.getOfferedPlaylistById(index).getName());
                MainActivity.mainForm.comboBox1.addItem(playlistService.getOfferedPlaylistById(index).getName());
                playlistService.saveData();
            }
        });
        playlistManagerForm.editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = playlistManagerForm.list1.getSelectedIndex();
                new PlaylistEditActivity("modify", index);
            }
        });
        playlistManagerForm.removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = playlistManagerForm.list1.getSelectedIndex();
                model_a.removeElement(playlistService.getActivePlaylistById(index).getName());
                MainActivity.mainForm.comboBox1.removeItem(playlistService.getActivePlaylistById(index).getName());
                playlistService.deleteActivePlaylistById(index);
                playlistService.saveData();
            }
        });
        playlistManagerForm.addAllOfferedPlaylistsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playlistService.addAllOfferedPlaylist();
                model_a.clear();
                model_d.clear();
                createProvlist();
                showProvlist();
                playlistService.saveData();
            }
        });
        playlistManagerForm.resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResetDialog.main(null);
            }
        });
        playlistManagerForm.addNewPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PlaylistEditActivity("addNew");
            }
        });
        playlistManagerForm.sortedListByUpdateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SortedListOfPlaylists();
            }
        });
    }

}
