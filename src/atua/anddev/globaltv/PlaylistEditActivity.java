package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.WarningDialog;
import atua.anddev.globaltv.form.PlaylistEditForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PlaylistEditActivity implements Services {
    private static int num = 0;
    private String editAction;
    private PlaylistEditForm playlistEditForm;

    PlaylistEditActivity(String mode) {
        editAction = mode;
        playlistEditForm = new PlaylistEditForm();
        applyLocals();
        playlistEditForm.standartRadioButton.setSelected(true);
        buttonActionListener();
    }

    PlaylistEditActivity(String mode, int num) {
        editAction = mode;
        PlaylistEditActivity.num = num;
        playlistEditForm = new PlaylistEditForm();
        applyLocals();
        showEdit();
        buttonActionListener();
    }

    private void applyLocals() {
        if (editAction.equals("addNew")) {
            playlistEditForm.editButton.setText(tService.getString("add"));
        }
        if (editAction.equals("modify")) {
            playlistEditForm.editButton.setText(tService.getString("modify"));
        }
    }

    private void showEdit() {
        if (editAction.equals("modify")) {
            playlistEditForm.textField1.setText(playlistService.getActivePlaylistById(num).getName());
            playlistEditForm.textField2.setText(playlistService.getActivePlaylistById(num).getUrl());
            if (playlistService.getActivePlaylistById(num).getType() == 0)
                playlistEditForm.standartRadioButton.setSelected(true);
            else
                playlistEditForm.torrentTvRadioButton.setSelected(true);
        }
        playlistEditForm.pack();
    }

    private void buttonActionListener() {
        ButtonGroup group = new ButtonGroup();
        group.add(playlistEditForm.standartRadioButton);
        group.add(playlistEditForm.torrentTvRadioButton);

        playlistEditForm.editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;
                String name, url;
                int type;
                name = playlistEditForm.textField1.getText();
                url = playlistEditForm.textField2.getText();
                if (playlistEditForm.standartRadioButton.isSelected())
                    type = 0;
                else
                    type = 1;
                if (name.length() == 0 || url.length() == 0) {
                    new WarningDialog(tService.getString("pleasefillallfields"));
                } else {
                    if (editAction.equals("modify")) {
                        playlistService.setActivePlaylistById(num, name, url, type);
                        PlaylistManagerActivity.model_a.setElementAt(name, num);
                        success = true;
                    } else if (editAction.equals("addNew") && playlistService.indexNameForActivePlaylist(name.toString()) == -1) {
                        playlistService.addToActivePlaylist(name, url, type, "", "");
                        PlaylistManagerActivity.model_a.addElement(name);
                        success = true;
                    } else {
                        new WarningDialog(tService.getString("playlistexist"));
                    }
                    if (success) {
                        playlistService.saveData();
                        playlistEditForm.dispose();
                    }
                }
            }
        });

    }

}
