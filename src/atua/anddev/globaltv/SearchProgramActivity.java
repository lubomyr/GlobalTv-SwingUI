package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Programme;
import atua.anddev.globaltv.form.SearchProgramForm;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SearchProgramActivity implements Services {
    private SearchProgramForm searchProgramForm;
    private List<Programme> searchList;

    public SearchProgramActivity(String searchStr, int type) {
        searchProgramForm = new SearchProgramForm();
        switch (type) {
            case 0:
                searchList = guideService.searchAllPeriod(searchStr);
                break;
            case 1:
                searchList = guideService.searchToday(searchStr);
                break;
            case 2:
                searchList = guideService.searchAfterMoment(searchStr);
                break;
            case 3:
                searchList = guideService.searchCurrentMoment(searchStr);
                break;
        }
        drawList();
    }

    private void drawList() {
        final DateFormat sdfStartOutput = new SimpleDateFormat("dd.MM HH:mm");
        final DateFormat sdfEndOutput = new SimpleDateFormat("HH:mm");
        String[] colNames;
        Object[][] data;
        int selected = -1;
        int cols = 3;
        colNames = new String[]{"channel", "date", "program"};
        data = new Object[searchList.size()][cols];
        for (int row = 0; row < searchList.size(); row++) {
            Programme programme = searchList.get(row);
            Calendar startTime = guideService.decodeDateTime(programme.getStart());
            Calendar stopTime = guideService.decodeDateTime(programme.getStop());
            String timeOutput = sdfStartOutput.format(startTime.getTime()) + " - " + sdfEndOutput.format(stopTime.getTime());
            data[row][0] = guideService.getChannelNameById(searchList.get(row).getChannel());
            data[row][1] = timeOutput;
            data[row][2] = guideService.decodeSymbols(searchList.get(row).getTitle());
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        searchProgramForm.table1.setModel(model);
        if (selected != -1) {
            searchProgramForm.table1.scrollRectToVisible(new Rectangle(searchProgramForm.table1.getCellRect(selected, 1, true)));
            searchProgramForm.table1.setRowSelectionInterval(selected, selected);
        }
        searchProgramForm.pack();
    }
}
