package atua.anddev.globaltv.form;

import javax.swing.*;

public class SettingForm extends JFrame {
    public JButton selectButton;
    public JTextField textField1;
    public JRadioButton useThisPlayerRadioButton;
    public JButton selectButton1;
    public JTextField textField2;
    public JRadioButton useThisPlayerRadioButton1;
    public JButton selectButton2;
    public JButton saveButton;
    private JPanel settingsPanel;
    public JTextField textField3;

    public SettingForm() {
        super("Settings");
        setContentPane(settingsPanel);
        pack();
        setVisible(true);
    }
}
