package atua.anddev.globaltv.runnables;

import atua.anddev.globaltv.Global;
import atua.anddev.globaltv.entity.Channel;

import javax.swing.*;
import java.util.List;

import static atua.anddev.globaltv.Services.guideService;

public class GetProgramTitleRunnable implements Runnable {
    private JTable table;
    private int column;
    private List<Channel> list;

    public GetProgramTitleRunnable(JTable table, int column, List<Channel> list) {
        this.table = table;
        this.column = column;
        this.list = list;
    }

    @Override
    public void run() {
        for (int row = 0; row < list.size(); row++) {
            if (Global.guideLoaded)
                table.setValueAt(guideService.getProgramTitle(list.get(row).getName()), row, column);
        }
    }
}
