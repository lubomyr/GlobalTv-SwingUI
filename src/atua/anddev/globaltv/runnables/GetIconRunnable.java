package atua.anddev.globaltv.runnables;

import atua.anddev.globaltv.entity.Channel;

import javax.swing.*;
import java.util.List;

import static atua.anddev.globaltv.Services.logoService;

public class GetIconRunnable implements Runnable {
    private JTable table;
    private List<Channel> list;

    public GetIconRunnable(JTable table, List<Channel> list) {
        this.table = table;
        this.list = list;
    }

    @Override
    public void run() {
        for (int row = 0; row < list.size(); row++) {
            Channel channel = list.get(row);
            ImageIcon imageIcon = logoService.getIcon(channel.getName());
            if (imageIcon == null)
                imageIcon = new ImageIcon("");
            table.setValueAt(imageIcon, row, 0);
        }
    }
}
