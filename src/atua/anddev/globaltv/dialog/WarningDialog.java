package atua.anddev.globaltv.dialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WarningDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel warnLabel;

    public WarningDialog(String text) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        warnLabel.setText(text);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        pack();
        setVisible(true);
    }

    private void onOK() {
// add your code here
        dispose();
    }

}
