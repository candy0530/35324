package data;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public interface Paintable {
  
  public void loadImg();
  public BufferedImage getImg();
  
  static BufferedImage loadOneImg( String path, int imgWidth, int imgHeight){
    
    assert path != null && path != "": "Try loading a wrong path image.";
    File file = new File(path);     
    assert( file != null && file.isFile() == true):"Not even a item img file.";
    
    try {
      
      BufferedImage img = ImageIO.read(file);
      if ( img.getHeight() != imgHeight || img.getWidth() != imgWidth){
        
        BufferedImage newImg = new BufferedImage(imgWidth, imgHeight, img.getType());
        Graphics g = newImg.getGraphics();
        g.drawImage(img, 0, 0, imgWidth, imgHeight, null);
        img = newImg;
        g.dispose();        
      }
      
      return img;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    //Generate a black img for the bad img conditions. 
    return getBlackImg(imgWidth, imgHeight);
  }
  
  static BufferedImage getBlackImg( int imgWidth, int imgHeight){
    
    BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics g = img.getGraphics();
    g.setColor( new Color(0, 0, 0));
    g.dispose();
    return img;
  }

}
