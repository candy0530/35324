
package sound;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javazoom.jl.player.Player;
public class EffectPlayer extends Thread{
  
  static HashMap< String, ByteArrayOutputStream> soundMap;
  InputStream inStream;
  
  static public void loadSound(){
    String[] path = {
        "sound/booking.mp3",
        "sound/eat.mp3",
        "sound/starGame.mp3",
        "sound/item1.mp3",
        "sound/item2.mp3",
        "sound/enter.mp3",
        "sound/rank.mp3",
        "sound/coGame.mp3",
        "sound/catch.mp3",
        "sound/ghostGo.mp3",
        "sound/itemEnd.mp3",
    };
    
    soundMap = new HashMap<>(path.length);
    ByteArrayOutputStream byteOutStream = null;
    
    for( int i=0; i < path.length; i++){
      
      try{
        FileInputStream fileInStream = new FileInputStream(path[i]);
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
      soundMap.put(path[i], byteOutStream);
    }
  }
  
  public EffectPlayer( String Path) {
    
    assert( soundMap.containsKey(Path)):"Wrong Sound Path";
    inStream = new ByteArrayInputStream( soundMap.get(Path).toByteArray());
  }
  
  @Override
  public void run() {

    Player player;
    try {

      player = new Player( inStream);
      player.play();
      inStream.close();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
