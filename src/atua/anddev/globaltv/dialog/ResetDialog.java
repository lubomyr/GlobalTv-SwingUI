package atua.anddev.globaltv.dialog;

import atua.anddev.globaltv.Services;

import javax.swing.*;
import java.awt.event.*;

public class ResetDialog extends JDialog implements Services {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel resetLabel;
    private ResetListener resetListener;

    public ResetDialog(ResetListener resetListener) {
        this.resetListener = resetListener;

        setContentPane(contentPane);
        setModal(true);

        getRootPane().setDefaultButton(buttonOK);
        resetLabel.setText(tService.getString("resetwarn"));
        buttonOK.setText(tService.getString("reset"));
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
    }

    private void onOK() {
        resetListener.resetAll();
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public interface ResetListener {
        void resetAll();
    }
}
