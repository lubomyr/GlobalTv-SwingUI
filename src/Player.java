import java.io.*;
public class Player {

  public Player() {
	String path;
	if (MainActivity.selectedLink.endsWith("acelive") || MainActivity.selectedLink.startsWith("acestream:"))
		path=MainActivity.path_aceplayer;
	else if (!MainActivity.otherplayer)
		path=MainActivity.path_vlc;
	else
		path=MainActivity.path_other;
    File f = new File(path);
    if (!(f.exists()&&f.isFile())) {
      System.out.println("Incorrect path or not a file");
      return;
    }
    Runtime rt = Runtime.getRuntime();
    try {
      Process proc = rt.exec(path+" "+MainActivity.selectedLink);
      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), false);
      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), false);
      errorGobbler.start();
      outputGobbler.start();
      System.out.println("\n"+proc.waitFor());
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (InterruptedException ie) {
      ie.printStackTrace();
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
        String line=null;
        while ( (line = br.readLine()) != null)
          if(!discard)
            System.out.println(line);    
        }
      catch (IOException ioe) {
        ioe.printStackTrace();  
      }
    }
  }
}
