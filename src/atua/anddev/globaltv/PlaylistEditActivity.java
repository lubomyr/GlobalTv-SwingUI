package atua.anddev.globaltv;

import atua.anddev.globaltv.dialog.WarningDialog;
import atua.anddev.globaltv.form.PlaylistEditForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PlaylistEditActivity implements Services {
    private int num = 0;
    private String editAction;
    private PlaylistEditForm playlistEditForm;
    private AddEditListener addEditListener;

    PlaylistEditActivity(AddEditListener addEditListener, String mode) {
        this.addEditListener = addEditListener;
        editAction = mode;
        playlistEditForm = new PlaylistEditForm();
        applyLocals();
        playlistEditForm.standartRadioButton.setSelected(true);
        buttonActionListener();
    }

    PlaylistEditActivity(AddEditListener addEditListener, String mode, int num) {
        this.addEditListener = addEditListener;
        editAction = mode;
        this.num = num;
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
            switch (playlistService.getActivePlaylistById(num).getType()) {
                case 0:
                    playlistEditForm.standartRadioButton.setSelected(true);
                    break;
                case 1:
                    playlistEditForm.torrentTvRadioButton.setSelected(true);
                    break;
                case 2:
                    playlistEditForm.w3uRadioButton.setSelected(true);
                    break;
            }
        }
        playlistEditForm.pack();
    }

    private void buttonActionListener() {
        ButtonGroup group = new ButtonGroup();
        group.add(playlistEditForm.standartRadioButton);
        group.add(playlistEditForm.torrentTvRadioButton);
        group.add(playlistEditForm.w3uRadioButton);

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
                else if (playlistEditForm.torrentTvRadioButton.isSelected())
                    type = 1;
                else
                    type = 2;
                if (name.length() == 0 || url.length() == 0) {
                    new WarningDialog(tService.getString("pleasefillallfields"));
                } else {
                    if (editAction.equals("modify")) {
                        playlistService.setActivePlaylistById(num, name, url, type);
                        addEditListener.change(name, num);
                        success = true;
                    } else if (editAction.equals("addNew") && playlistService.indexNameForActivePlaylist(name.toString()) == -1) {
                        playlistService.addToActivePlaylist(name, url, type, "", "");
                        addEditListener.addnew(name);
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

    public interface AddEditListener {
        void change(String name, int num);

        void addnew(String name);
    }

}
