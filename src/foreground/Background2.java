package foreground;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Background2 extends JLabel {

    public Background2() {
        ImageIcon background = new ImageIcon(new ImageIcon("image/title2.png").getImage().getScaledInstance(195, 130, Image.SCALE_DEFAULT));
        setIcon(background);
        setLocation(-10, -195);
        setSize(800, 600);
    }

}
