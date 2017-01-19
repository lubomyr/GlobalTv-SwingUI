package atua.anddev.globaltv.dialog;

import atua.anddev.globaltv.SearchProgramActivity;
import atua.anddev.globaltv.Services;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class SearchProgramDialog extends JDialog implements Services {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JLabel searchLabel;
    private int type;

    enum Search {ALL_PERIOD, TODAY, AFTER, NOW}

    public SearchProgramDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        type = 0;

        searchLabel.setText(tService.getString("pleaseentertext"));
        buttonOK.setText(tService.getString("search"));
        buttonCancel.setText(tService.getString("cancel"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        List<String> testList = Arrays.asList(tService.getString("all_period"), tService.getString("today"),
                tService.getString("after_moment"), tService.getString("now"));
        for (String str : testList)
            comboBox1.addItem(str);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        selectSearchTypeActionAdapter(testList);

        pack();
        setVisible(true);
    }

    private void onOK() {
        String searchString = textField1.getText();
        new SearchProgramActivity(searchString, type);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void selectSearchTypeActionAdapter(List<String> testList) {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                type = testList.indexOf(itemEvent.getItem());
            }
        };
        comboBox1.addItemListener(itemListener);
    }

}
