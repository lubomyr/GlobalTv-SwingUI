package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.Programme;
import atua.anddev.globaltv.form.SearchProgramForm;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

class SearchProgramActivity implements Services {
    private SearchProgramForm searchProgramForm;
    private List<Programme> searchList;
    private int type;

    SearchProgramActivity() {
        searchProgramForm = new SearchProgramForm();
        searchProgramForm.buttonSearch.setText(tService.getString("search"));
        setUpComboBox();
        searchButtonActionlistener();
        actionSelector();
    }

    private void setUpComboBox() {
        List<String> testList = Arrays.asList(tService.getString("all_period"), tService.getString("today"),
                tService.getString("after_moment"), tService.getString("now"));
        for (String str : testList)
            searchProgramForm.comboBox1.addItem(str);
        selectSearchTypeActionAdapter(testList);
    }

    private void selectSearchTypeActionAdapter(List<String> testList) {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                type = testList.indexOf(itemEvent.getItem());
            }
        };
        searchProgramForm.comboBox1.addItemListener(itemListener);
    }


    private void searchButtonActionlistener() {
        searchProgramForm.buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchProgramForm.textField1.getText();
                switch (type) {
                    case 0:
                        searchList = guideService.searchAllPeriod(searchString);
                        break;
                    case 1:
                        searchList = guideService.searchToday(searchString);
                        break;
                    case 2:
                        searchList = guideService.searchAfterMoment(searchString);
                        break;
                    case 3:
                        searchList = guideService.searchCurrentMoment(searchString);
                        break;
                }
                drawList();
            }
        });
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
            searchProgramForm.table1.scrollRectToVisible(new Rectangle(searchProgramForm.table1.getCellRect(selected,
                    1, true)));
            searchProgramForm.table1.setRowSelectionInterval(selected, selected);
        }
        searchProgramForm.infoText.setText(searchList.size() + " " + tService.getString("pcs"));
        searchProgramForm.pack();
    }

    private void actionSelector() {
        searchProgramForm.table1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = searchProgramForm.table1.getSelectedRow();
                if (index != -1) {
                    String title = guideService.decodeSymbols(searchList.get(index).getTitle());
                    if ((title != null) && !title.isEmpty()) {
                        searchProgramForm.guideTextArea.setText(title);
                    }

                    String desc = guideService.decodeSymbols(searchList.get(index).getDesc());
                    if ((desc != null) && !desc.isEmpty()) {
                        searchProgramForm.guideTextArea.append("\n" + desc);
                        searchProgramForm.guideTextArea.setLineWrap(true);
                        searchProgramForm.guideTextArea.setWrapStyleWord(true);
                    }
                    searchProgramForm.pack();
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
