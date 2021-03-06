package map;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


import data.Paintable;

public class MapDetail implements Paintable{
    private boolean walkable;
    private boolean burger = false;
    private BufferedImage walkablePicture;
//    private BufferedImage burgerPicture;
    
    private static BufferedImage[] bgImg;
    private static BufferedImage[] blockImg;
    private static BufferedImage burgerImg;
    private int bgId = 0;
    private int blockId = 0;
    public double burgerDegree = 0;
    
    final static int imgWidth = 60;
    final static int imgHeight = 60;
    final static int bgTypes = 3;
    final static int blockTypes = 4;
    
    public MapDetail() {
        bgId = (int)( Math.random() * bgTypes);
    }

    public boolean getBurger() {
        return burger;
    }
    
    public void setBurger(boolean burger) {
      this.burger = burger;
  }

    public void setWalkable(boolean walkable) {
      
      this.walkable = walkable;
      
      if ( !walkable){
        //the last block type is preserve for some special case.
        blockId = 1 + (int)( Math.random() * (blockTypes -1));
      }
      else{
          blockId = 0;
      }
    }
    
    public void setBlockId(int blockId) {
      this.blockId = blockId;
    }

    public boolean getWalkable() {
        return walkable;
    }

    public BufferedImage getWalkablePicture() {
        return walkablePicture;
    }

    public BufferedImage getBurgerPicture() {
        if (burger)
          return burgerImg;
        
        return null;
    }

    @Override
    public void loadImg() {
      
      final int imgSize = 48;
      BufferedImage texture = loadTexturePack();
      loadBurgerImg();
      
      Point[] bg = { new Point(0, 0), new Point(1, 0), new Point(2, 0)/*, new Point(3, 0),*/ };     
      Point[] block = {
          null,
          new Point(0,34), new Point(1,34), new Point(2,34), /*new Point(5,34),*/ new Point(1,40),
      };
      
      assert bg.length == bgTypes: "Check the bg loaded numbers.";
      bgImg = new BufferedImage[bg.length];
      blockImg = new BufferedImage[block.length];
      
      for ( int i = 0; i < bg.length; i++){
        
        bgImg[i] = Paintable.scaleImg(  
            texture.getSubimage(bg[i].x*imgSize, bg[i].y*imgSize, imgSize, imgSize),
            imgWidth, imgHeight
            );
      }
      
      for ( int i = 1; i < block.length; i++){
        blockImg[i] = Paintable.scaleImg( 
            texture.getSubimage(block[i].x*imgSize, block[i].y*imgSize, imgSize, imgSize),
            imgWidth, imgHeight
            );
      }
    }

    @Override
    public BufferedImage getImg() {
      return getBgImg();
    }
    
    public BufferedImage getBgImg() {
      
      assert( bgId < bgImg.length):"Wrong Bg Id.";
      return bgImg[this.bgId];
    }
    
    public BufferedImage getBlockImg() {
      
      assert( blockId < blockImg.length):"Wrong Block Id.";
      return blockImg[this.blockId];
    }
    
    static void loadBurgerImg(){
      String path = "image/Map/burger3.png";
      burgerImg = Paintable.loadOneImg(path, imgWidth, imgHeight);
    }
    
    static BufferedImage getBurgerImg(){
      
      assert burgerImg != null: "Load image first!!";
      return burgerImg;
    }
    
    static BufferedImage loadTexturePack(){
      
      String filePath = "image/Map/map_all.png";
      //map texture size: 384*1968
      //do not ask why magic number here.
      return Paintable.loadOneImg(filePath, 384, 1968);
    }

    
}
