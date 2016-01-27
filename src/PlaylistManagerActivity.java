import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
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

public class PlaylistManagerActivity extends MainActivity {
	static DefaultListModel<String> model_a;
	static DefaultListModel<String> model_d;

	PlaylistManagerActivity() {
		PlaylistManagerGui.SetPlaylistManagerActivity();
		applyLocals();
		showPlayLists();
	}

	public static void applyLocals() {
		PlaylistManagerGui.plalistManagerBackButton.setText(local.$back);
		PlaylistManagerGui.btnRestoreButton.setText(local.$restore);
		PlaylistManagerGui.btnAddNewButton.setText(local.$addnew);
		PlaylistManagerGui.btnEditButton.setText(local.$edit);
		PlaylistManagerGui.lblNewLabel.setText(local.$activePlaylists);
		PlaylistManagerGui.lblNewLabel_1.setText(local.$disabledPlaylists);
	}

	public static void showPlayLists() {
		model_a = new DefaultListModel<String>();
		for (int i = 0; i < ActivePlaylist.size(); i++) {
			model_a.addElement(ActivePlaylist.getName(i));
		}
		PlaylistManagerGui.list_a.setModel(model_a);

		model_d = new DefaultListModel<String>();
		for (int i = 0; i < DisabledPlaylist.size(); i++) {
			model_d.addElement(DisabledPlaylist.getName(i));
		}
		PlaylistManagerGui.list_d.setModel(model_d);
	}

	public static void saveData() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);

			Element torkey = doc.createElement("torrentkey");
			torkey.appendChild(doc.createTextNode(torrentKey));
			rootElement.appendChild(torkey);

			for (int j = 0; j < ActivePlaylist.size(); j++) {
				// favorites elements
				Element provider = doc.createElement("provider");
				rootElement.appendChild(provider);

				// name elements
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(ActivePlaylist.getName(j)));
				provider.appendChild(name);

				// url elements
				Element url = doc.createElement("url");
				url.appendChild(doc.createTextNode(ActivePlaylist.getUrl(j)));
				provider.appendChild(url);

				// type elements
				Element type = doc.createElement("type");
				type.appendChild(doc.createTextNode(ActivePlaylist.getTypeString(j)));
				provider.appendChild(type);

				// enable elements
				Element enable = doc.createElement("enable");
				enable.appendChild(doc.createTextNode("true"));
				provider.appendChild(enable);
			}

			for (int j = 0; j < DisabledPlaylist.size(); j++) {
				// favorites elements
				Element provider = doc.createElement("provider");
				rootElement.appendChild(provider);

				// name elements
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(DisabledPlaylist.getName(j)));
				provider.appendChild(name);

				// url elements
				Element url = doc.createElement("url");
				url.appendChild(doc.createTextNode(DisabledPlaylist.getUrl(j)));
				provider.appendChild(url);

				// file elements
				Element file = doc.createElement("file");
				file.appendChild(doc.createTextNode(DisabledPlaylist.getFile(j)));
				provider.appendChild(file);

				// type elements
				Element type = doc.createElement("type");
				type.appendChild(doc.createTextNode(DisabledPlaylist.getTypeString(j)));
				provider.appendChild(type);

				// enable elements
				Element enable = doc.createElement("enable");
				enable.appendChild(doc.createTextNode("false"));
				provider.appendChild(enable);
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("userdata.xml"));

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

	static class PlaylistManagerGui extends MyGui {
		static JButton btnRestoreButton;
		static JLabel lblNewLabel;
		static JLabel lblNewLabel_1;
		static JList<String> list_a;
		static JList<String> list_d;
		static JScrollPane list_a_scrollpane;
		static JScrollPane list_d_scrollpane;
		static JButton btnNewButton_1;
		static JButton btnNewButton_2;
		static JButton btnAddNewButton;
		static JButton btnEditButton;
		static JButton plalistManagerBackButton;

		public static void SetPlaylistManagerActivity() {
			MyGui.mainFrame.setVisible(false);
			MyGui.playlistEditFrame.setVisible(false);
			MyGui.playlistManagerFrame.setVisible(true);
		}

		protected static void initialize() {
			playlistManagerFrame = new JFrame();
			playlistManagerFrame.setBounds(posx, posy, 450, 460);
			playlistManagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			playlistManagerFrame.getContentPane().setLayout(null);

			btnRestoreButton = new JButton("Restore");
			btnRestoreButton.setBounds(298, 25, 105, 48);
			playlistManagerFrame.getContentPane().add(btnRestoreButton);

			lblNewLabel = new JLabel("Active Playlists");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(20, 101, 164, 14);
			playlistManagerFrame.getContentPane().add(lblNewLabel);

			lblNewLabel_1 = new JLabel("Disabled Playlists");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_1.setBounds(239, 101, 164, 14);
			playlistManagerFrame.getContentPane().add(lblNewLabel_1);

			list_a = new JList<String>();
			list_a.setBounds(20, 126, 164, 233);

			list_a_scrollpane = new JScrollPane();
			list_a_scrollpane.setBounds(20, 126, 164, 233);
			playlistManagerFrame.getContentPane().add(list_a_scrollpane);
			list_a_scrollpane.setViewportView(list_a);

			list_d = new JList<String>();
			list_d.setBounds(250, 126, 164, 233);

			list_d_scrollpane = new JScrollPane();
			list_d_scrollpane.setBounds(250, 126, 164, 233);
			playlistManagerFrame.getContentPane().add(list_d_scrollpane);
			list_d_scrollpane.setViewportView(list_d);

			btnNewButton_1 = new JButton("->");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = list_a.getSelectedIndex();
					if (index != -1) {
						model_a.removeElement(ActivePlaylist.getName(index));
						model_d.addElement(ActivePlaylist.getName(index));
						DisabledPlaylist.add(ActivePlaylist.getName(index), ActivePlaylist.getUrl(index),
								ActivePlaylist.getFile(index), ActivePlaylist.getType(index));
						MainGui.comboBox.removeItem(ActivePlaylist.getName(index));
						ActivePlaylist.remove(index);
						saveData();
					}
				}
			});
			btnNewButton_1.setBounds(192, 198, 50, 23);
			playlistManagerFrame.getContentPane().add(btnNewButton_1);

			btnNewButton_2 = new JButton("<-");
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = list_d.getSelectedIndex();
					if (index != -1) {
						model_d.removeElement(DisabledPlaylist.getName(index));
						model_a.addElement(DisabledPlaylist.getName(index));
						ActivePlaylist.add(DisabledPlaylist.getName(index), DisabledPlaylist.getUrl(index),
								DisabledPlaylist.getFile(index), DisabledPlaylist.getType(index));
						MainGui.comboBox.addItem(DisabledPlaylist.getName(index));
						DisabledPlaylist.remove(index);
						saveData();
					}
				}
			});
			btnNewButton_2.setBounds(192, 262, 50, 23);
			playlistManagerFrame.getContentPane().add(btnNewButton_2);

			btnAddNewButton = new JButton("Add New");
			btnAddNewButton.setBounds(20, 25, 105, 48);
			btnAddNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					playeditact = new PlaylistEditActivity("add");
				}
			});
			playlistManagerFrame.getContentPane().add(btnAddNewButton);

			btnEditButton = new JButton("Edit");
			btnEditButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (list_a.getSelectedIndex()!=-1)
						playeditact = new PlaylistEditActivity("edit",list_a.getSelectedIndex());
				}
			});
			btnEditButton.setBounds(158, 25, 105, 48);
			playlistManagerFrame.getContentPane().add(btnEditButton);

			plalistManagerBackButton = new JButton("Back to MainMenu");
			plalistManagerBackButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					playmanact = null;
					MainActivity.MainGui.SetMainActivity();
				}
			});
			plalistManagerBackButton.setBounds(138, 375, 145, 38);
			playlistManagerFrame.getContentPane().add(plalistManagerBackButton);
		}

	}

}