package atua.anddev.globaltv;

import java.io.*;

public class Player {

    public Player(String selectedLink) {
        String path;
        if (selectedLink.endsWith("acelive") || selectedLink.startsWith("acestream:"))
            path = Global.path_aceplayer;
        else if (!Global.otherplayer)
            path = Global.path_vlc;
        else
            path = Global.path_other;
        File f = new File(path);
        if (!(f.exists() && f.isFile())) {
            System.out.println("Incorrect path or not a file");
            return;
        }
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(path + " " + selectedLink);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), false);
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), false);
            errorGobbler.start();
            outputGobbler.start();
            System.out.println("\n" + proc.waitFor());
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
    }

    class StreamGobbler extends Thread {
        InputStream is;
        boolean discard;

        StreamGobbler(InputStream is, boolean discard) {
            this.is = is;
            this.discard = discard;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    if (!discard)
                        System.out.println(line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
