import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GlobalFavoritesActivity extends MainActivity
{
	private static DefaultListModel<String> model;
	GlobalFavoritesActivity()
	{
		applyGlobalFavoritesLocals();
		showFavorites();
	}
	
	public static void applyGlobalFavoritesLocals() {
		GlobalFavoritesGui.globalFavoritesBackButton.setText(local.$back);
		GlobalFavoritesGui.globalFavoritesOpenButton.setText(local.$openChannel);
		GlobalFavoritesGui.globalFavoritesRemoveFavButton.setText(local.$removeFromFavorites);
	}
	
	public void showFavorites()
	{
		GlobalFavoritesGui.globalFavoritesLabel.setText(favoriteList.size()+local.$channels);
		model = new DefaultListModel<String>();
		for (int i=0; i<favoriteList.size(); i++)
		{
			model.addElement(favoriteList.get(i)+"            '"+favoriteProvList.get(i)+"'");
		}
		GlobalFavoritesGui.globalFavoritesList.setModel(model);	
	}
	
	static class GlobalFavoritesGui extends MyGui
	{
		static JButton globalFavoritesBackButton;
		static JLabel globalFavoritesLabel;
		static JButton globalFavoritesOpenButton;
		static JButton globalFavoritesRemoveFavButton;
		static JList<String> globalFavoritesList;
		static JScrollPane globalFavoritesScrollBar;
		
		public static void SetGlobalFavoritesActivity()
		{
			MyGui.mainFrame.setVisible(false);
			MyGui.catlistFrame.setVisible(false);
			MyGui.playlistFrame.setVisible(false);
			MyGui.globalSearchFrame.setVisible(false);
			MyGui.globalFavoritesFrame.setVisible(true);
			globalFavoritesOpenButton.setVisible(false);
			globalFavoritesRemoveFavButton.setVisible(false);
		}
		
		protected static void initialize()
		{
			globalFavoritesFrame = new JFrame();
			globalFavoritesFrame.setBounds(posx, posy, 450, 445);
			globalFavoritesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			globalFavoritesFrame.getContentPane().setLayout(null);
			
			globalFavoritesBackButton = new JButton("Back");
			globalFavoritesBackButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					globalfavoritesact=null;
					MainActivity.MainGui.SetMainActivity();
				}
			});
			globalFavoritesBackButton.setBounds(310, 11, 114, 50);
			globalFavoritesFrame.getContentPane().add(globalFavoritesBackButton);
			
			globalFavoritesOpenButton = new JButton("Open Channel");
			globalFavoritesOpenButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					runplayer rp = new runplayer();
					rp.start();
				}
			});
			globalFavoritesOpenButton.setBounds(38, 351, 159, 50);
			globalFavoritesFrame.getContentPane().add(globalFavoritesOpenButton);
			
			globalFavoritesRemoveFavButton = new JButton("Remove from Favorites");
			globalFavoritesRemoveFavButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					favoriteList.remove(selectedItem);
					favoriteProvList.remove(selectedItem);
					model.remove(selectedItem);
					saveFavorites();
				}
			});
			globalFavoritesRemoveFavButton.setBounds(237, 351, 159, 50);
			globalFavoritesFrame.getContentPane().add(globalFavoritesRemoveFavButton);
			
			globalFavoritesLabel = new JLabel("Select Channel");
			globalFavoritesLabel.setBounds(154, 69, 118, 23);
			globalFavoritesFrame.getContentPane().add(globalFavoritesLabel);
			
			globalFavoritesList = new JList<String>();
			globalFavoritesList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					int index = globalFavoritesList.getSelectedIndex();
					if (index!=-1)
					{
						selectedItem=index;
						selectedChannel = favoriteList.get(index);
						//selectedLink = globalSearchUrl.get(index);
						selectedPlaylist = favoriteProvList.get(index);
						
						String getProvFile = null;
						int getProvType = 0;
						String getProvName;
						getProvName=favoriteProvList.get(index);
						int numA=ActivePlaylist.indexOfName(getProvName);
						int numD=DisabledPlaylist.indexOfName(getProvName);
						if (numA >= 0)
						{
							getProvFile = ActivePlaylist.getFile(numA);
							getProvType = ActivePlaylist.getType(ActivePlaylist.indexOfName(getProvName));
						}
						else if (numD >= 0)
						{
							getProvFile = DisabledPlaylist.getFile(numD);
							getProvType = DisabledPlaylist.getType(DisabledPlaylist.indexOfName(getProvName));
						}
						else
						{
//							Toast.makeText(GlobalFavoriteActivity.this, getResources().getString(R.string.playlistnotexist), Toast.LENGTH_SHORT).show();
							return;
						}
						readPlaylist(getProvFile, getProvType);
						for (int j=0; j < channel.size(); j++)
						{
							if (channel.getName(j).equals(favoriteList.get(index)))
							{
								selectedLink = channel.getLink(j);
								break;
							}
						}
						
						globalFavoritesOpenButton.setVisible(true);
						globalFavoritesRemoveFavButton.setVisible(true);
					}
				}
			});
			globalFavoritesList.setBounds(38, 103, 340, 237);
			
			globalFavoritesScrollBar = new JScrollPane();
			globalFavoritesScrollBar.setBounds(38, 103, 340, 237);
			globalFavoritesFrame.getContentPane().add(globalFavoritesScrollBar, BorderLayout.CENTER);
			globalFavoritesScrollBar.setViewportView(globalFavoritesList);
		}
	}
}