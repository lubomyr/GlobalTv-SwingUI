import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateOutdatedPlaylistActivity extends MainActivity {
    UpdateOutdatedPlaylistActivity() {
        UpdateOutdatedPlaylistGui.SetUpdateOutdatedPlaylistActivity();
        updateAll();
    }

    public static void updateAll() {
        UpdateOutdatedPlaylistGui.textArea.setText("");
        for (int i = 0; i < ActivePlaylist.size(); i++) {
            checkPlaylistFile(ActivePlaylist.getFile(i));
            if (needUpdate)
                downloadOutdatedPlaylist(i);
        }
    }

    public static void downloadOutdatedPlaylist(final int num) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String path = myPath + ActivePlaylist.getFile(num);
                    saveUrl(path, ActivePlaylist.getUrl(num));

                    needUpdate = false;
                    checkPlaylistFile(ActivePlaylist.getFile(num));
                    UpdateOutdatedPlaylistGui.textArea.append(ActivePlaylist.getName(num) + " - updated\n");
                } catch (Exception e) {
                    UpdateOutdatedPlaylistGui.textArea.append(ActivePlaylist.getName(num) + " - failed\n" + e.toString() + "\n");
                    // Log.i("SDL", "Error: " + e.toString());
                }
            }
        }).start();
    }

    static class UpdateOutdatedPlaylistGui extends MyGui {
        static JTextArea textArea;
        static JScrollPane textArea_scrollpane;
        static JButton btnNewButton;

        public static void SetUpdateOutdatedPlaylistActivity() {
            //MyGui.mainFrame.setVisible(false);
            MyGui.updateOutdatedPlaylistFrame.setVisible(true);
        }

        protected static void initialize() {
            updateOutdatedPlaylistFrame = new JFrame();
            updateOutdatedPlaylistFrame.setBounds(posx, posy, 450, 460);
            updateOutdatedPlaylistFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            updateOutdatedPlaylistFrame.getContentPane().setLayout(null);

            textArea = new JTextArea();
            textArea.setBounds(30, 30, 370, 300);

            textArea_scrollpane = new JScrollPane();
            textArea_scrollpane.setBounds(30, 30, 370, 300);
            updateOutdatedPlaylistFrame.getContentPane().add(textArea_scrollpane);
            textArea_scrollpane.setViewportView(textArea);

            btnNewButton = new JButton("Back");
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    updateoutdatedplaylistact = null;
                    updateOutdatedPlaylistFrame.setVisible(false);
                }
            });
            btnNewButton.setBounds(159, 350, 109, 35);
            updateOutdatedPlaylistFrame.getContentPane().add(btnNewButton);
        }
    }
}
/*
URL u = new URL ( "http://www.example.com/");
HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
        huc.connect () ;
        int code = huc.getResponseCode() ;
        System.out.println(code);*/