package sound;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class SoundPlayer extends Thread{
  
  ByteArrayOutputStream byteOutStream;
  Player basicSound;
  String Path;
  boolean repeat = false;
  
  public SoundPlayer(String path) {
    this(path,false);
  }
  
  public SoundPlayer(String path, boolean repeat) {
    super();
    this.Path = path;
    this.repeat = repeat;

    try{
      FileInputStream fileInStream = new FileInputStream(path);
      byteOutStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[512];
      int len;
      while ((len = fileInStream.read(buffer)) > -1 ) {
          byteOutStream.write(buffer, 0, len);
      }
      byteOutStream.flush();
      fileInStream.close();
    }
    catch(Exception exc){
      exc.printStackTrace();
      System.out.println("Failed to play the file.");
    }  
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      while (repeat){
        InputStream inStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        basicSound = new Player( inStream);
        basicSound.play();
        inStream.close();
        sleep(1);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void close(){
    
    repeat = false;
    if (basicSound != null)
      basicSound.close();
  }


}
