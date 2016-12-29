package atua.anddev.globaltv;

import atua.anddev.globaltv.entity.GuideProv;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static atua.anddev.globaltv.OsCheck.OSType.Windows;
import static atua.anddev.globaltv.Services.guideService;
import static atua.anddev.globaltv.Services.tService;
import static atua.anddev.globaltv.service.GuideService.channelGuideList;
import static atua.anddev.globaltv.service.GuideService.guideProvList;

public class SettingsActivity {
    private SettingForm settingForm;
    private List<String> themeList = Arrays.asList("Metal", "Nimbus", "CDE/Motif", "GTK+", "Windows", "Windows Classic");
    private List<String> fontSizeList = Arrays.asList("12", "16", "20", "24");

    public SettingsActivity() {
        settingForm = new SettingForm();
        applyLocals();
        showSettings();
        fontSizeListActionAdapter();
        themeListActionAdapter();
        guideProvListActionAdapter();
        buttonActionListener();
        showGuideProvInfo(false);
    }

    private void applyLocals() {
        settingForm.saveButton.setText(tService.local("save"));
        settingForm.selectButton.setText(tService.local("select"));
        settingForm.selectButton1.setText(tService.local("select"));
        settingForm.selectButton2.setText(tService.local("select"));
        settingForm.useThisPlayerRadioButton.setText(tService.local("useThisPlayer"));
        settingForm.useThisPlayerRadioButton1.setText(tService.local("useThisPlayer"));
        settingForm.themeLabel.setText(tService.local("selectTheme"));
        settingForm.fontSizeLabel.setText(tService.local("selectFontSize"));
        settingForm.guideLabel.setText(tService.local("programGuideSource"));
        settingForm.downloadButton.setText(tService.local("update"));
        settingForm.fileSizeLabel.setText(tService.local("fileSize"));
        settingForm.channelsLabel.setText(tService.local("channels"));
        settingForm.TimePeriodLabel.setText(tService.local("period"));
    }

    private void showSettings() {
        settingForm.textField1.setText(Global.path_aceplayer);
        settingForm.textField2.setText(Global.path_vlc);
        settingForm.textField3.setText(Global.path_other);
        ButtonGroup group = new ButtonGroup();
        group.add(settingForm.useThisPlayerRadioButton);
        group.add(settingForm.useThisPlayerRadioButton1);
        if (Global.otherplayer)
            settingForm.useThisPlayerRadioButton1.setSelected(true);
        else
            settingForm.useThisPlayerRadioButton.setSelected(true);

        for (String str : themeList) {
            settingForm.comboBox1.addItem(str);
        }
        settingForm.comboBox1.setSelectedItem(Global.selectedTheme);

        for (String str : fontSizeList) {
            settingForm.comboBox2.addItem(str);
        }
        settingForm.comboBox2.setSelectedItem(Global.selectedFontSize);

        for (GuideProv guideProv : guideProvList) {
            settingForm.comboBox3.addItem(guideProv.getName());
        }
        settingForm.comboBox3.setSelectedIndex(Global.selectedGuideProv);
        setupMinWidth();
    }

    private void setupMinWidth() {
        switch (Global.selectedFontSize) {
            case "12":
                settingForm.setMinimumSize(new Dimension(394, settingForm.getHeight()));
                break;
            case "16":
                settingForm.setMinimumSize(new Dimension(458, settingForm.getHeight()));
                break;
        }
        settingForm.pack();
    }

    private void showGuideProvInfo(boolean parse) {
        GuideProv guideProv = guideProvList.get(Global.selectedGuideProv);
        File file = new File(guideProv.getFile());
        if (file.exists()) {
            if (parse) {
                settingForm.fileSizeTextField.setText("Parsing...");
                guideService.parseGuide();
            }
            settingForm.fileSizeTextField.setText(file.length() + " kb");
            settingForm.channelsNumTextField.setText(channelGuideList.size() + " pcs");
            settingForm.timePeriodTextField.setText(guideService.getTotalTimePeriod());
        } else {
            settingForm.fileSizeTextField.setText("File not exist");
            updateGuideProv();
        }
    }

    private void fontSizeListActionAdapter() {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                Global.selectedFontSize = itemEvent.getItem().toString();
            }
        };
        settingForm.comboBox2.addItemListener(itemListener);
    }

    private void themeListActionAdapter() {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                Global.selectedTheme = itemEvent.getItem().toString();
            }
        };
        settingForm.comboBox1.addItemListener(itemListener);
    }

    private void guideProvListActionAdapter() {
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String str = itemEvent.getItem().toString();
                for (int i = 0; i < guideProvList.size(); i++) {
                    if (str.equals(guideProvList.get(i).getName())) {
                        Global.selectedGuideProv = i;
                    }
                }
                if (itemEvent.getStateChange() == 1)
                    showGuideProvInfo(true);
            }
        };
        settingForm.comboBox3.addItemListener(itemListener);
    }

    private void setupExtension(JFileChooser chooser, String description) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("AcePlayer Executable", "exe");
        OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
        if (ostype == Windows)
            chooser.setFileFilter(filter);
    }

    private void buttonActionListener() {

        settingForm.selectButton.addActionListener(new ActionListener() {
            private Component parent;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                setupExtension(chooser, "AcePlayer Executable");
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
                setupExtension(chooser, "VLC Executable");
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
                setupExtension(chooser, "Player Executable");
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Global.path_other = chooser.getSelectedFile().getPath();
                    showSettings();
                }
            }
        });

        settingForm.downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGuideProv();
            }
        });

        settingForm.saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveSettings();
                settingForm.dispose();
            }
        });

    }

    private void updateGuideProv() {
        settingForm.fileSizeTextField.setText("Downloading...");
        GuideProv guideProv = guideProvList.get(Global.selectedGuideProv);
        try {
            MainActivity.saveUrl(guideProv.getFile(), guideProv.getUrl());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        showGuideProvInfo(true);
    }

    private void saveSettings() {
        Global.otherplayer = settingForm.useThisPlayerRadioButton1.isSelected();
        Global.path_aceplayer = settingForm.textField1.getText();
        Global.path_vlc = settingForm.textField2.getText();
        Global.path_other = settingForm.textField3.getText();
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

            // selected gui theme element
            Element theme = doc.createElement("theme");
            theme.appendChild(doc.createTextNode(Global.selectedTheme));
            rootElement.appendChild(theme);

            // selected gui theme element
            Element fontsize = doc.createElement("fontsize");
            fontsize.appendChild(doc.createTextNode(Global.selectedFontSize));
            rootElement.appendChild(fontsize);

            // selected language element
            Element language = doc.createElement("language");
            language.appendChild(doc.createTextNode(Global.lang));
            rootElement.appendChild(language);

            // selected gui theme element
            Element guideprov = doc.createElement("guideprov");
            guideprov.appendChild(doc.createTextNode(String.valueOf(Global.selectedGuideProv)));
            rootElement.appendChild(guideprov);

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
