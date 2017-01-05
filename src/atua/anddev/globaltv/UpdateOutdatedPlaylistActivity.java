package atua.anddev.globaltv;

import atua.anddev.globaltv.form.UpdateOutdatedPlaylistForm;

import javax.swing.table.DefaultTableModel;
import java.util.Date;

public class UpdateOutdatedPlaylistActivity implements Services {
    private UpdateOutdatedPlaylistForm updateOutdatedPlaylistForm;
    private DefaultTableModel model;
    private int updNum;

    public UpdateOutdatedPlaylistActivity() {
        updateOutdatedPlaylistForm = new UpdateOutdatedPlaylistForm();
        updateAll();
    }

    private void updateAll() {
        String[] colNames;
        Object[][] data;
        int cols = 2;
        updNum = 0;
        colNames = new String[] { "name", "result" };
        data = new Object[playlistService.sizeOfActivePlaylist()][cols];
        for (int i = 0; i < playlistService.sizeOfActivePlaylist(); i++) {
            downloadOutdatedPlaylist(i);
        }
        model = new DefaultTableModel(data, colNames);
        updateOutdatedPlaylistForm.table1.setModel(model);
        updateOutdatedPlaylistForm.pack();
    }

    private void downloadOutdatedPlaylist(int num) {
        DownloadPlaylist dplst = new DownloadPlaylist(num);
        Thread threadDp = new Thread(dplst);
        threadDp.start();
    }

    private class DownloadPlaylist implements Runnable {
        private int num;

        DownloadPlaylist(int num) {
            this.num = num;
        }

        public void run() {
            try {
                String path = myPath + playlistService.getActivePlaylistById(num).getFile();
                MainActivity.saveUrl(path, playlistService.getActivePlaylistById(num).getUrl());

                String oldMd5 = playlistService.getActivePlaylistById(num).getMd5();
                String newMd5 = MainActivity.getMd5OfFile(path);
                if (!newMd5.equals(oldMd5)) {
                    playlistService.setMd5(num, newMd5);
                    playlistService.setUpdateDate(num, new Date().getTime());
                    playlistService.saveData();
                    updateOutdatedPlaylistForm.table1.setValueAt(playlistService.getActivePlaylistById(num).getName(),updNum,0);
                    updateOutdatedPlaylistForm.table1.setValueAt(tService.getString("updated"), updNum, 1);
                    if (num == MainActivity.selectedProvider) {
                        MainActivity.checkPlaylistFile(MainActivity.selectedProvider);
                    }
                    updNum++;
                }
            } catch (Exception e) {
                updateOutdatedPlaylistForm.table1.setValueAt(playlistService.getActivePlaylistById(num).getName(),updNum,0);
                updateOutdatedPlaylistForm.table1.setValueAt(tService.getString("failed"), updNum, 1);
                updNum++;
                System.out.println("Error: " + e.toString());
            }
        }
    }
}
