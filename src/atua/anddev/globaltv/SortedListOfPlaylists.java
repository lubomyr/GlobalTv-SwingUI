package atua.anddev.globaltv;


import atua.anddev.globaltv.entity.Playlist;
import atua.anddev.globaltv.form.SortedListofPlaylistsForm;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SortedListOfPlaylists implements Services {
    private SortedListofPlaylistsForm sortedListofPlaylistsForm;

    public SortedListOfPlaylists() {
        sortedListofPlaylistsForm = new SortedListofPlaylistsForm();
        showSortedDateOfPlaylist();
    }

    private void showSortedDateOfPlaylist() {
        List<Playlist> sortedList = playlistService.getSortedByDatePlaylists();
        List<String> name = new ArrayList<String>();
        List<String> date = new ArrayList<String>();
        String[] colNames;
        Object[][] data;
        int cols = 2;

        for (Playlist plst : sortedList) {
            Long longDate;
            String daysPassed;
            name.add(plst.getName());
            try {
                longDate = Long.parseLong(plst.getUpdate());
                daysPassed = getDiffDays(longDate);
            } catch (Exception e) {
                daysPassed = e.toString();
            }
            date.add(daysPassed);
        }

        colNames = new String[]{"name", "updated"};
        data = new Object[sortedList.size()][cols];
        for (int row = 0; row < sortedList.size(); row++) {
            data[row][0] = name.get(row);
            data[row][1] = date.get(row);
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        sortedListofPlaylistsForm.table1.setModel(model);
        sortedListofPlaylistsForm.sortedlistLabel.setText(tService.local("sortedListButton"));
        sortedListofPlaylistsForm.pack();
    }

    private String getDiffDays(long inputDate) {
        String tmpText;
        long currDate = (new Date()).getTime();
        long diffDate = currDate - inputDate;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(diffDate);
        int daysPassed = cal.get(Calendar.DAY_OF_YEAR);
        switch (daysPassed) {
            case 1:
                tmpText = tService.local("updated") + " " + new Date(inputDate).toLocaleString();
                break;
            case 2:
                tmpText = tService.local("updated") + " 1 " + tService.local("dayago");
                break;
            case 3:
            case 4:
            case 5:
                tmpText = tService.local("updated") + " " + (daysPassed - 1) + " " + tService.local("daysago");
                break;
            default:
                tmpText = tService.local("updated") + " " + (daysPassed - 1) + " " + tService.local("fivedaysago");
                break;
        }
        return tmpText;
    }
}
