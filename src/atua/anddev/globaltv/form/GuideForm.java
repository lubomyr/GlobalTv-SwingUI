package atua.anddev.globaltv.form;

import javax.swing.*;

/**
 * Created by lyubomyr on 26.12.16.
 */
public class GuideForm extends JFrame {
    public JTextField channelTextField;
    public JTable table1;
    public JTextArea descTextArea;
    public JScrollPane descPanel;
    private JPanel guidePanel;

    public GuideForm() {
        super("Program Guide");
        setContentPane(guidePanel);
        pack();
        setVisible(true);
    }
}
