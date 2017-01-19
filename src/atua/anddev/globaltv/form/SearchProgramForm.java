package atua.anddev.globaltv.form;

import javax.swing.*;

public class SearchProgramForm extends JFrame {
    public JTable table1;
    private JTextArea textArea1;
    private JPanel searchProgramPanel;

    public SearchProgramForm() {
        super("Search Program");
        setContentPane(searchProgramPanel);
        pack();
        setVisible(true);
    }
}
