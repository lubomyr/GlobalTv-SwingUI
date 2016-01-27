import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SettingsActivity extends MainActivity {
    SettingsActivity() {
        SettingsActivity.SettingsGui.SetSettingsActivity();
        applySettingsLocals();
        showSettings();
    }

    public static void applySettingsLocals() {
        SettingsGui.btnNewButton_4.setText(local.$save);
        SettingsGui.btnNewButton_5.setText(local.$backToMainMenu);
        SettingsGui.btnNewButton.setText(local.$choose);
        SettingsGui.btnNewButton_1.setText(local.$choose);
        SettingsGui.btnNewButton_2.setText(local.$choose);
        SettingsGui.rdbtnNewRadioButton.setText(local.$useThisPlayer);
        SettingsGui.rdbtnNewRadioButton_1.setText(local.$useThisPlayer);
    }

    public static void showSettings() {
        SettingsGui.textField.setText(path_aceplayer);
        SettingsGui.textField_1.setText(path_vlc);
        SettingsGui.textField_2.setText(path_other);
    }

    public static void saveSettings() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // aceplayer path element
            Element path1 = doc.createElement("aceplayer");
            path1.appendChild(doc.createTextNode(path_aceplayer));
            rootElement.appendChild(path1);

            // vlc path element
            Element path2 = doc.createElement("vlcplayer");
            path2.appendChild(doc.createTextNode(path_vlc));
            rootElement.appendChild(path2);

            // other player path element
            Element path3 = doc.createElement("otherplayer");
            path3.appendChild(doc.createTextNode(path_other));
            rootElement.appendChild(path3);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("settings.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            // System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    static class SettingsGui extends MyGui {
        private static JTextField textField;
        private static JTextField textField_1;
        private static JTextField textField_2;
        private static JLabel lblNewLabel;
        private static JLabel lblNewLabel_1;
        private static JLabel lblNewLabel_2;
        private static JRadioButton rdbtnNewRadioButton;
        private static JRadioButton rdbtnNewRadioButton_1;
        private static JButton btnNewButton;
        private static JButton btnNewButton_1;
        private static JButton btnNewButton_2;
        private static JButton btnNewButton_4;
        private static JButton btnNewButton_5;

        public static void SetSettingsActivity() {
            MyGui.mainFrame.setVisible(false);
            MyGui.settingsFrame.setVisible(true);
        }

        protected static void initialize() {
            settingsFrame = new JFrame();
            settingsFrame.setBounds(posx, posy, 450, 460);
            settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            settingsFrame.getContentPane().setLayout(null);

            lblNewLabel = new JLabel("AcePlayer path");
            lblNewLabel.setBounds(30, 31, 87, 14);
            settingsFrame.getContentPane().add(lblNewLabel);

            textField = new JTextField();
            textField.setBounds(30, 50, 372, 20);
            settingsFrame.getContentPane().add(textField);
            textField.setColumns(10);

            lblNewLabel_1 = new JLabel("VLC path");
            lblNewLabel_1.setBounds(30, 98, 58, 14);
            settingsFrame.getContentPane().add(lblNewLabel_1);

            textField_1 = new JTextField();
            textField_1.setBounds(30, 118, 372, 20);
            settingsFrame.getContentPane().add(textField_1);
            textField_1.setColumns(10);

            lblNewLabel_2 = new JLabel("other player path");
            lblNewLabel_2.setBounds(30, 172, 87, 14);
            settingsFrame.getContentPane().add(lblNewLabel_2);

            textField_2 = new JTextField();
            textField_2.setBounds(30, 191, 372, 20);
            settingsFrame.getContentPane().add(textField_2);
            textField_2.setColumns(10);

            rdbtnNewRadioButton = new JRadioButton("use this player");
            rdbtnNewRadioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    otherplayer = false;
                }
            });
            rdbtnNewRadioButton.setBounds(138, 94, 159, 23);
            settingsFrame.getContentPane().add(rdbtnNewRadioButton);
            rdbtnNewRadioButton.setSelected(true);

            rdbtnNewRadioButton_1 = new JRadioButton("use this player");
            rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    otherplayer = true;
                }
            });
            rdbtnNewRadioButton_1.setBounds(138, 168, 159, 23);
            settingsFrame.getContentPane().add(rdbtnNewRadioButton_1);

            ButtonGroup group = new ButtonGroup();
            group.add(rdbtnNewRadioButton);
            group.add(rdbtnNewRadioButton_1);

            btnNewButton = new JButton("Choose");
            btnNewButton.addActionListener(new ActionListener() {
                private Component parent;

                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("AcePlayer Executable", "exe");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        path_aceplayer = chooser.getSelectedFile().getPath();
                        showSettings();
                    }

                }
            });
            btnNewButton.setBounds(313, 27, 89, 23);
            settingsFrame.getContentPane().add(btnNewButton);

            btnNewButton_1 = new JButton("Choose");
            btnNewButton_1.addActionListener(new ActionListener() {
                private Component parent;

                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("VLC Executable", "exe");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        path_vlc = chooser.getSelectedFile().getPath();
                        showSettings();
                    }

                }
            });
            btnNewButton_1.setBounds(313, 94, 89, 23);
            settingsFrame.getContentPane().add(btnNewButton_1);

            btnNewButton_2 = new JButton("Choose");
            btnNewButton_2.addActionListener(new ActionListener() {
                private Component parent;

                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Player Executable", "exe");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        path_other = chooser.getSelectedFile().getPath();
                        showSettings();
                    }

                }
            });
            btnNewButton_2.setBounds(313, 168, 89, 23);
            settingsFrame.getContentPane().add(btnNewButton_2);

            btnNewButton_4 = new JButton("Save");
            btnNewButton_4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    saveSettings();
                    settingsact = null;
                    MainActivity.MainGui.SetMainActivity();
                }
            });
            btnNewButton_4.setBounds(30, 327, 143, 36);
            settingsFrame.getContentPane().add(btnNewButton_4);

            btnNewButton_5 = new JButton("Cancel");
            btnNewButton_5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    settingsact = null;
                    MainActivity.MainGui.SetMainActivity();
                }
            });
            btnNewButton_5.setBounds(249, 327, 143, 36);
            settingsFrame.getContentPane().add(btnNewButton_5);
        }
    }

}