package atua.anddev.globaltv;

import atua.anddev.globaltv.form.SettingForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingsActivity implements Services {
    private SettingForm settingForm;

    public SettingsActivity() {
        settingForm = new SettingForm();
        applyLocals();
        showSettings();
        buttonActionListener();
    }

    private void applyLocals() {
        settingForm.saveButton.setText(tService.local("save"));
        settingForm.selectButton.setText(tService.local("select"));
        settingForm.selectButton1.setText(tService.local("select"));
        settingForm.selectButton2.setText(tService.local("select"));
    }

    private void showSettings() {
        settingForm.textField1.setText(Global.path_aceplayer);
        settingForm.textField2.setText(Global.path_vlc);
        settingForm.textField3.setText(Global.path_other);
    }

    private void buttonActionListener() {
        settingForm.selectButton.addActionListener(new ActionListener() {
            private Component parent;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("AcePlayer Executable", "exe");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Global.path_aceplayer = chooser.getSelectedFile().getPath();
                    showSettings();
                }
            }
        });

        settingForm.selectButton1.addActionListener(new ActionListener() {
            private Component parent;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("VLC Executable", "exe");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Global.path_vlc = chooser.getSelectedFile().getPath();
                    showSettings();
                }
            }
        });

        settingForm.selectButton2.addActionListener(new ActionListener() {
            private Component parent;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Player Executable", "exe");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Global.path_other = chooser.getSelectedFile().getPath();
                    showSettings();
                }
            }
        });

        settingForm.saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveSettings();
            }
        });

    }

    public static void saveSettings() {
        if (Global.path_aceplayer == null)
            Global.path_aceplayer = "";
        if (Global.path_vlc == null)
            Global.path_vlc = "";
        if (Global.path_other == null)
            Global.path_other = "";
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // aceplayer path element
            Element path1 = doc.createElement("aceplayer");
            path1.appendChild(doc.createTextNode(Global.path_aceplayer));
            rootElement.appendChild(path1);

            // vlc path element
            Element path2 = doc.createElement("vlcplayer");
            path2.appendChild(doc.createTextNode(Global.path_vlc));
            rootElement.appendChild(path2);

            // other player path element
            Element path3 = doc.createElement("otherplayer");
            path3.appendChild(doc.createTextNode(Global.path_other));
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

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }

    }
}
