package atua.anddev.globaltv.form;

import javax.swing.*;

/**
 * Created by lubomyr on 29.03.16.
 */
public class CategoryForm extends JFrame {
    public JButton favoritesButton;
    public JButton searchButton;
    public JList list1;
    private JPanel categoryPanel;
    public JLabel catlistInfoLabel;

    public CategoryForm() {
        super("Category list");
        setContentPane(categoryPanel);
        pack();
        setVisible(true);
    }

}
