import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

    private static BufferedImage spriteSheet;
    //Ū���ʵe��
    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("image/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }
    
    //�q�ʵe�Ϥ��ΥX�Q�n���Y�i�ʧ@�ʵe��
    public static BufferedImage getSprite(int xGrid, int yGrid) {

        if (spriteSheet == null) {
            spriteSheet = loadSprite("xpchar5");
        }

        return spriteSheet.getSubimage(xGrid * 32, yGrid * 48, 32, 48);
    }

}