package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Programme;
import atua.anddev.globaltv.form.GuideForm;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lyubomyr on 26.12.16.
 */

class GuideActivity implements Services {
    private GuideForm guideForm;

    GuideActivity(String chName) {
        guideForm = new GuideForm();

        showChannelName(chName);
        showChannelGuide(chName);
    }

    private void showChannelName(String chName) {
        guideForm.channelTextField.setText(chName);
    }

    private void showChannelGuide(String chName) {
        List<Programme> guideList = guideService.getChannelGuide(chName);
        Calendar currentTime = Calendar.getInstance();
        final DateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmmss Z");
        final DateFormat sdfStartOutput = new SimpleDateFormat("dd.MM HH:mm");
        final DateFormat sdfEndOutput = new SimpleDateFormat("HH:mm");
        String[] colNames;
        Object[][] data;
        int selected = -1;
        int cols = 2;
        colNames = new String[]{"date", "program"};
        data = new Object[guideList.size()][cols];
        for (int row = 0; row < guideList.size(); row++) {
            Programme programme = guideList.get(row);
            String startDateInput = programme.getStart();
            String endDateInput = programme.getStop();
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            try {
                if (!startDateInput.isEmpty())
                    startDate.setTime(sdfInput.parse(startDateInput));
                if (!endDateInput.isEmpty())
                    endDate.setTime(sdfInput.parse(endDateInput));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String timeOutput = sdfStartOutput.format(startDate.getTime()) + " - " + sdfEndOutput.format(endDate.getTime());
            data[row][0] = timeOutput;
            data[row][1] = guideService.decodeSymbols(guideList.get(row).getTitle());
            if (currentTime.after(startDate) && currentTime.before(endDate)) {
                data[row][0] += " *";
                selected = row;
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        guideForm.table1.setModel(model);
        if (selected != -1) {
            guideForm.table1.scrollRectToVisible(new Rectangle(guideForm.table1.getCellRect(selected, 0, true)));
            guideForm.table1.setRowSelectionInterval(selected, selected);
        }
        guideForm.pack();
        actionSelector(guideList);
    }

    private void actionSelector(List<Programme> guideList) {
        guideForm.descPanel.setVisible(false);
        guideForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = guideForm.table1.getSelectedRow();
                if (index != -1) {
                    String title = guideService.decodeSymbols(guideList.get(index).getTitle());
                    if ((title != null) && !title.isEmpty()) {
                        guideForm.descPanel.setVisible(true);
                        guideForm.descTextArea.setText(title);
                    } else
                        guideForm.descPanel.setVisible(false);
                    String desc = guideService.decodeSymbols(guideList.get(index).getDesc());
                    if ((desc != null) && !desc.isEmpty()) {
                        guideForm.descTextArea.append("\n" + desc);
                        guideForm.descTextArea.setLineWrap(true);
                        guideForm.descTextArea.setWrapStyleWord(true);
                    }
                    guideForm.pack();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
