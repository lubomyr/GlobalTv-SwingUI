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
    public JTextField textField3;
    public JComboBox comboBox1;
    public JLabel themeLabel;
    public JComboBox comboBox2;
    public JLabel fontSizeLabel;
    private JPanel settingsPanel;
    public JComboBox comboBox3;
    public JButton downloadButton;
    public JLabel guideLabel;
    public JTextField fileSizeTextField;
    public JTextField channelsNumTextField;
    public JTextField timePeriodTextField;
    public JLabel fileSizeLabel;
    public JLabel channelsLabel;
    public JLabel TimePeriodLabel;

    public SettingForm() {
        super("Settings");
        setContentPane(settingsPanel);
        pack();
        setVisible(true);
    }
}
