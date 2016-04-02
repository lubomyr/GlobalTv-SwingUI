package atua.anddev.globaltv.dialog;

import atua.anddev.globaltv.GlobalSearchActivity;
import atua.anddev.globaltv.SearchListActivity;
import atua.anddev.globaltv.Services;

import javax.swing.*;
import java.awt.event.*;

public class SearchDialog extends JDialog implements Services {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JLabel searchLabel;
    private String type;
    private String searchString;

    public SearchDialog(String type) {
        this.type = type;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        searchLabel.setText(tService.local("pleaseentertext"));
        buttonOK.setText(tService.local("search"));
        buttonCancel.setText(tService.local("cancel"));

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

        pack();
        setVisible(true);
    }

    private void onOK() {
        searchString = textField1.getText();
        if (type.equals("global"))
        {
            new GlobalSearchActivity(searchString);
        } else {
            new SearchListActivity(searchString);
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
