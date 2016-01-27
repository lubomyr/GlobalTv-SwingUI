import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;


public class MyGui extends MainActivity {

	protected static JFrame mainFrame;
	protected static JFrame catlistFrame;
	protected static JFrame playlistFrame;
	protected static JFrame globalSearchFrame;
	protected static JFrame globalFavoritesFrame;
	protected static JFrame localFavoritesFrame;
	protected static JFrame settingsFrame;
	protected static JFrame playlistManagerFrame;
	protected static JFrame playlistEditFrame;
	protected static JFrame updateOutdatedPlaylistFrame;
	
	static MyGui window;
	
	static JLabel MainPlaylistLabel;
	static JComboBox<String> comboBox;
	static JButton MainUpdateButton;
	static JButton MainUpdateAllButton;
	static JButton MainOpenButton;
	static JButton MainFavoritesButton;
	static JButton MainSearchButton;
	static JButton MainPlaylistManagerButton;
	static JButton MainSettingsButton;
	static JComboBox<String> comboBox_1;
	static JLabel MainPlaylistInfoLabel;
	static JLabel MainWarningLabel;
	static JTextField MainSearchtextField;
	
	static CatlistActivity catlistact;
	static PlaylistActivity playlistact;
	static GlobalSearchActivity globalsearchact;
	static GlobalFavoritesActivity globalfavoritesact;
	static FavlistActivity favoritesact;
	static SettingsActivity settingsact;
	static PlaylistManagerActivity playmanact;
	static PlaylistEditActivity playeditact;
	static UpdateOutdatedPlaylistActivity updateoutdatedplaylistact;
	
	static int selectedItem;
	
	protected static int posx=100;
	protected static int posy=100;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MyGui();
					MyGui.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public MyGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the mainFrame.
	 */
	private void initialize() {
		CatlistActivity.CatlistGui.initialize();
		PlaylistActivity.PlaylistGui.initialize();
		GlobalFavoritesActivity.GlobalFavoritesGui.initialize();
		GlobalSearchActivity.GlobalSearchGui.initialize();
		FavlistActivity.FavlistGui.initialize();
		SettingsActivity.SettingsGui.initialize();
		PlaylistManagerActivity.PlaylistManagerGui.initialize();
		PlaylistEditActivity.PlaylistEditGui.initialize();
		UpdateOutdatedPlaylistActivity.UpdateOutdatedPlaylistGui.initialize();
		
		mainFrame = new JFrame();
		mainFrame.setBounds(posx, posy, 450, 460);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		MainPlaylistLabel = new JLabel("Playlist");
		MainPlaylistLabel.setBounds(184, 11, 70, 14);
		mainFrame.getContentPane().add(MainPlaylistLabel);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(111, 31, 200, 32);
		mainFrame.getContentPane().add(comboBox);

		
		MainUpdateButton = new JButton("Update Selected Playlist");
		MainUpdateButton.setBounds(22, 98, 182, 40);
		mainFrame.getContentPane().add(MainUpdateButton);
		
		MainUpdateAllButton = new JButton("Update Outdated Playlists");
		MainUpdateAllButton.setBounds(229, 98, 182, 40);
		mainFrame.getContentPane().add(MainUpdateAllButton);
		
		MainOpenButton = new JButton("Open Playlist");
		MainOpenButton.setBounds(111, 156, 200, 63);
		mainFrame.getContentPane().add(MainOpenButton);
		
		MainFavoritesButton = new JButton("Favorites");
		MainFavoritesButton.setBounds(22, 296, 182, 40);
		mainFrame.getContentPane().add(MainFavoritesButton);
		
		MainSearchButton = new JButton("Search");
		MainSearchButton.setBounds(229, 296, 182, 40);
		mainFrame.getContentPane().add(MainSearchButton);
		
		MainSettingsButton = new JButton("Settings");
		MainSettingsButton.setBounds(22, 237, 182, 40);
		mainFrame.getContentPane().add(MainSettingsButton);
		
		MainPlaylistManagerButton = new JButton("Playlist Manager");
		MainPlaylistManagerButton.setBounds(229, 237, 182, 40);
		mainFrame.getContentPane().add(MainPlaylistManagerButton);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(111, 378, 200, 32);
		mainFrame.getContentPane().add(comboBox_1);
		
		MainPlaylistInfoLabel = new JLabel("Playlist Info");
		MainPlaylistInfoLabel.setBounds(111, 73, 200, 14);
		mainFrame.getContentPane().add(MainPlaylistInfoLabel);
		
		MainWarningLabel = new JLabel("Warnings");
		MainWarningLabel.setEnabled(false);
		MainWarningLabel.setBounds(22, 347, 182, 20);
		mainFrame.getContentPane().add(MainWarningLabel);
		
		MainSearchtextField = new JTextField();
		MainSearchtextField.setBounds(229, 347, 182, 20);
		mainFrame.getContentPane().add(MainSearchtextField);
		MainSearchtextField.setColumns(10);
		
	}
}
