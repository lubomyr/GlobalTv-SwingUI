package atua.anddev.globaltv.form;

import javax.swing.*;

public class SearchProgramForm extends JFrame {
    public JTable table1;
    public JTextArea guideTextArea;
    private JPanel searchProgramPanel;
    public JTextField textField1;
    public JButton buttonSearch;
    public JComboBox comboBox1;
    public JLabel infoText;

    public SearchProgramForm() {
        super("Search Program");
        setContentPane(searchProgramPanel);
        pack();
        setVisible(true);
    }
}
