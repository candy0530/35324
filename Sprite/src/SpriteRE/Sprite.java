package SpriteRE;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

    private static BufferedImage spriteSheet;
    //
    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("image/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }
    
    public static BufferedImage getSprite(int xGrid, int yGrid, String pic) {
    	
        spriteSheet = loadSprite(pic);

        return spriteSheet.getSubimage(xGrid * 45, 2 + yGrid * 66, 45, 66);
    }
    
}